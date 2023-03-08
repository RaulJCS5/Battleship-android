package pt.isel.battleshipAndroid.lobby

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.game.IGameService
import pt.isel.battleshipAndroid.game.model.Board
import pt.isel.battleshipAndroid.game.model.Position
import pt.isel.battleshipAndroid.game.model.PositionStateBoard
import pt.isel.battleshipAndroid.model.*
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel

class AllShips() {
    val shipList = Result.success(mutableListOf<SetFleetInputModel>())
    init {
        shipList.getOrNull()?.add(SetFleetInputModel("CARRIER",Layout.NONE.name,null))
        shipList.getOrNull()?.add(SetFleetInputModel("BATTLESHIP",Layout.NONE.name,null))
        shipList.getOrNull()?.add(SetFleetInputModel("SUBMARINE",Layout.NONE.name,null))
        shipList.getOrNull()?.add(SetFleetInputModel("CRUISER",Layout.NONE.name,null))
        shipList.getOrNull()?.add(SetFleetInputModel("DESTROYER",Layout.NONE.name,null))
    }
}

class LobbyViewModel(
    private val iLobbyService: ILobbyService,
    private val iGameService: IGameService
) : ViewModel() {
    private val DEFINE_FLEET: String = "Define your fleet"
    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    private var _isLoadingGamePhase by mutableStateOf(false)
    val isLoadingGamePhase: Boolean
        get() = _isLoadingGamePhase

    private var _isQuitLoading by mutableStateOf(false)
    val isQuitLoading: Boolean
        get() = _isQuitLoading

    private var _isloadingCheckGameState by mutableStateOf(false)
    val isloadingCheckGameState: Boolean
        get() = _isloadingCheckGameState

    private var _maxShot by mutableStateOf<Result<String>?>(null)
    var maxShot: Result<String>?
        get() = _maxShot
        set(value) {
            _maxShot = value
        }

    private var _giveUpLobby by mutableStateOf<Result<DefaultAnswerModel>?>(null)
    val giveUpLobby: Result<DefaultAnswerModel>?
        get() = _giveUpLobby

    private var _newGame by mutableStateOf<Result<String>?>(null)
    val newGame: Result<String>?
        get() = _newGame

    private var _setFleetAnswer by mutableStateOf<Result<String>?>(null)
    val setFleetAnswer: Result<String>?
        get() = _setFleetAnswer

    private var _game by mutableStateOf<Result<GameOutputModel>?>(null)
    val game: Result<GameOutputModel>?
        get() = _game

    private var _allShipsAndLayouts by mutableStateOf<Result<MutableList<SetFleetInputModel>>?>(AllShips().shipList)
    val allShipsAndLayouts: Result<MutableList<SetFleetInputModel>>?
        get() = _allShipsAndLayouts

    private var _gamePhase by mutableStateOf<Result<GamePhase>?>(null)
    val gamePhase: Result<GamePhase>?
        get() = _gamePhase

    private var _fleetBoard by mutableStateOf<Result<Board>?>(Result.success(Board()))
    val fleetBoard: Result<Board>?
        get() = _fleetBoard

    private var _shootBoard by mutableStateOf<Result<Board>?>(Result.success(Board()))
    val shootBoard: Result<Board>?
        get() = _shootBoard

    private var _myBoard by mutableStateOf<Result<Board>?>(Result.success(Board()))
    val myBoard: Result<Board>?
        get() = _myBoard

    //to handle problem response
    var problem by mutableStateOf<ProblemOutputModel?>(null)

    fun fetchNewGame(maxShot: Int, tokenAuthorize: String?) {
        viewModelScope.launch {
            problem = null
            _isLoading = true
            _newGame =
                try {
                    //delay(2000)
                    Result.success(
                        iLobbyService.fetchNewGame(maxShot, tokenAuthorize ?: "")
                    )
                } catch (e: ProblemException) {
                    problem = e.problem
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            _isLoading = false
        }
    }

    fun fetchGiveUpLobby(tokenAuthorize: String?) {
        viewModelScope.launch {
            problem = null
            _isQuitLoading = true
            _giveUpLobby =
                try {
                    //delay(2000)
                    Result.success(
                        iLobbyService.fetchGiveUpLobby(tokenAuthorize ?: "")
                    )
                } catch (e: ProblemException) {
                    problem = e.problem
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            _isQuitLoading = false
        }
    }

    fun fetchGetUserCurrentGame(tokenAuthorize: String?) {
        viewModelScope.launch {
            problem = null
            _isloadingCheckGameState = true
            _game =
                try {
                    //delay(2000)
                    Result.success(
                        iGameService.fetchUserCurrentGame(tokenAuthorize ?: "")
                    )
                } catch (e: ProblemException) {
                    problem = e.problem
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            _isloadingCheckGameState = false
        }
    }

    fun fetchGetCurrentGamePhase(gameId: Int?, tokenAuthorize: String?) {
        viewModelScope.launch {
            problem = null
            _isLoadingGamePhase = true
            _gamePhase =
                try {
                    if (gameId != null)
                        Result.success(
                            iGameService.fetchGamePhase(gameId, tokenAuthorize ?: "")
                        )
                    else
                        throw Exception()
                } catch (e: ProblemException) {
                    problem = e.problem
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            _isLoadingGamePhase = false
        }
    }

    fun setShipLayout(ship: String, layout: String) {
        val updated = _allShipsAndLayouts?.getOrNull()?.map {
            if (it.shipType == ship&&it.referencePoint==null) {
                it.copy(shipLayout = layout)
            } else {
                it
            }
        }
        if (updated!=null)
            _allShipsAndLayouts = Result.success(updated.toMutableList())
    }

    private fun setShipLayoutReferenceLayout(at:Position) {
        val updated = _allShipsAndLayouts?.getOrNull()?.map {
            if (!it.shipLayout.equals("NONE")&&it.referencePoint==null) {
                it.copy(referencePoint = at)
            } else {
                it
            }
        }
        if (updated!=null)
            _allShipsAndLayouts = Result.success(updated.toMutableList())
    }

    fun deleteAll() {
        _allShipsAndLayouts = AllShips().shipList
        _fleetBoard = Result.success(Board())
    }

    fun updateFleetBoard(at: Position) {
        val listListMarker = fleetBoard?.getOrNull()?.tiles
        allShipsAndLayouts?.getOrNull()?.map {
            if (it.referencePoint == null) {
                val shipSize = getSizeByName(it.shipType)-1
                listListMarker?.mapIndexed { row, listMarker ->
                    if (it.shipLayout.equals("UP")) {
                        for (i in 0..shipSize) {
                            if (row == at.row + i) {
                                listMarker[at.col] = PositionStateBoard(at,
                                    wasShoot = false,
                                    wasShip = true,
                                    null,
                                    null
                                )
                            }
                        }
                    } else if (it.shipLayout.equals("DOWN")) {
                        for (i in 0..shipSize) {
                            if (row == at.row - i) {
                                listMarker[at.col] = PositionStateBoard(at,
                                    wasShoot = false,
                                    wasShip = true,
                                    null,
                                    null
                                )
                            }
                        }
                    } else if (it.shipLayout.equals("LEFT")) {
                        for (i in 0..shipSize) {
                            if (row == at.row) {
                                listMarker[at.col + i] = PositionStateBoard(at,
                                    wasShoot = false,
                                    wasShip = true,
                                    null,
                                    null
                                )
                            }
                        }
                    } else if (it.shipLayout.equals("RIGHT")) {
                        for (i in 0..shipSize) {
                            if (row == at.row) {
                                listMarker[at.col - i] = PositionStateBoard(at,
                                    wasShoot = false,
                                    wasShip = true,
                                    null,
                                    null
                                )
                            }
                        }
                    }
                }
            }
        }
        setShipLayoutReferenceLayout(at)
        if (listListMarker != null) {
            _fleetBoard = Result.success(Board(listListMarker))
        }
    }

    fun fetchSetFleet(gameId: Int?, token: String?, setFleetInputModel: MutableList<SetFleetInputModel>?) {
        viewModelScope.launch {
            problem = null
            _isLoading = true
            _setFleetAnswer = try {
                if (gameId != null)
                    Result.success(
                        iGameService.fetchSetFleet(gameId, token ?: "", setFleetInputModel)
                    )
                else{
                    throw Exception()
                }
            } catch (e: ProblemException) {
                problem = e.problem
                Result.failure(e)
            }catch (e:Exception){
                Result.failure(e)
            }
            _isLoading = false
        }
    }

    private var _setShoot by mutableStateOf<Result<DefaultAnswerModel>?>(null)
    val setShoot: Result<DefaultAnswerModel>?
        get() = _setShoot

    private var _isLoadingShoot by mutableStateOf(false)
    val isLoadingShoot: Boolean
        get() = _isLoadingShoot

    fun fetchSetShoot(gameId: Int?, token: String?, at:Position?) {
        viewModelScope.launch {
            problem = null
            _isLoadingShoot = true
            _setShoot = try {
                if (gameId != null)
                    Result.success(
                        iGameService.fetchSetShoot(gameId, at!!,token ?: "")
                    )
                else{
                    throw Exception()
                }
            } catch (e: ProblemException) {
                problem = e.problem
                Result.failure(e)
            }catch (e:Exception){
                Result.failure(e)
            }
            _isLoadingShoot = false
        }
    }

    private var _giveUpGame by mutableStateOf<Result<DefaultAnswerModel>?>(null)
    val giveUpGame: Result<DefaultAnswerModel>?
        get() = _giveUpGame

    private var _isLoadingGiveUpGame by mutableStateOf(false)
    val isLoadingGiveUpGame: Boolean
        get() = _isLoadingGiveUpGame

    fun giveUpGame(token: String?) {
        viewModelScope.launch {
            problem = null
            _isLoadingGiveUpGame = true
            _giveUpGame = try {
                Result.success(
                    iGameService.fetchGiveUpGame(token ?: "")
                )
            } catch (e: ProblemException) {
                problem = e.problem
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
            _isLoadingGiveUpGame = false
        }
    }


    private var _isLoadingMyBoardFleet by mutableStateOf(false)
    val isLoadingMyBoardFleet: Boolean
        get() = _isLoadingMyBoardFleet

    fun fetchCheckFleet(gameId:Int,myBoard:Boolean,token: String?) {
        viewModelScope.launch {
            problem = null
            _isLoadingMyBoardFleet = true
            if (myBoard) {
                _myBoard = try {
                    Result.success(
                        Board(iGameService.fetchCheckFleet(gameId, MyBoardModel(myBoard), token ?: ""))
                    )
                } catch (e: ProblemException) {
                    problem = e.problem
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }else {
                _shootBoard = try {
                    Result.success(
                        Board(iGameService.fetchCheckFleet(gameId, MyBoardModel(myBoard), token ?: ""))
                    )
                } catch (e: ProblemException) {
                    problem = e.problem
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            }
            _isLoadingMyBoardFleet = false
        }
    }
}