package pt.isel.battleshipAndroid.lobby

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import pt.isel.battleshipAndroid.DependenciesContainer
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.game.GameScreen
import pt.isel.battleshipAndroid.game.GameScreenState
import pt.isel.battleshipAndroid.game.PlayerWonScreen
import pt.isel.battleshipAndroid.game.PlayerWonScreenState
import pt.isel.battleshipAndroid.game.model.Position
import pt.isel.battleshipAndroid.main.LocalTokenDto
import pt.isel.battleshipAndroid.main.MainActivity
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.InfoAlert
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.ProblemAlert
import pt.isel.battleshipAndroid.utils.viewModelInit

enum class Layout{
    LEFT,RIGHT,UP,DOWN,NONE
}
data class SetFleetInputModel(
    val shipType: String,
    var shipLayout: String,
    val referencePoint: Position?
)

class LobbyActivity : ComponentActivity() {
    private val dependencies by lazy { application as DependenciesContainer }
    private val viewModel: LobbyViewModel by viewModels {
        viewModelInit {
            LobbyViewModel(dependencies.lobbyService,dependencies.gameService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //for LOBBY
            val loadingState: RefreshingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle
            val loadingStateGamePhase: RefreshingState =
                if (viewModel.isLoadingGamePhase) RefreshingState.Refreshing
                else RefreshingState.Idle
            val loadingQuitState: RefreshingState =
                if (viewModel.isQuitLoading) RefreshingState.Refreshing
                else RefreshingState.Idle
            val loadingCheckGameState: RefreshingState =
                if (viewModel.isloadingCheckGameState) RefreshingState.Refreshing
                else RefreshingState.Idle
            val receivedExtra = tokenExtra
            val maxShot = viewModel.maxShot?.getOrNull() ?: ""
            val game = viewModel.game?.getOrNull()
            val gamePhase = viewModel.gamePhase?.getOrNull()
            //for GAME
            val shootBoard = viewModel.shootBoard?.getOrNull()
            val myFleetBoard = viewModel.myBoard?.getOrNull()
            val setFleetBoard = viewModel.fleetBoard?.getOrNull()
            val allShips = viewModel.allShipsAndLayouts?.getOrNull()
            val setShootInfo = viewModel.setShoot?.getOrNull()

            if (gamePhase == null) {
                LobbyScreen(
                    state = LobbyScreenState(
                        maxShot = maxShot,
                        game = game,
                        loadingState = loadingState,
                        loadingQuitState = loadingQuitState,
                        loadingStateGamePhase = loadingStateGamePhase,
                        loadingCheckGameState = loadingCheckGameState
                    ),
                    onLobbyRequest = {
                        if (maxShot != "") {
                            viewModel.fetchNewGame(maxShot.toInt(), receivedExtra?.token)
                            /*Toast.makeText(
                                applicationContext,
                                "${viewModel.message?.getOrNull()}",
                                Toast.LENGTH_SHORT
                            ).show()*/
                        }
                    },
                    onCheckIfUserInGame = {
                        viewModel.fetchGetUserCurrentGame(receivedExtra?.token)
                    }
                    ,
                    onQuitRequest = {
                        viewModel.fetchGiveUpLobby(receivedExtra?.token)
                    },
                    onCheckCurrentGamePhase = {
                        viewModel.fetchGetCurrentGamePhase(
                            game?.gameId,
                            receivedExtra?.token
                        )
                    },
                    maxShotChange = {
                        viewModel.maxShot = Result.success(it)
                    },
                    onNavigationRequested = NavigationHandlers(
                        goHome = { finish() }
                    )
                )
            } else if (gamePhase.name == "LAYOUT") {
                GameScreen(
                    state = GameScreenState(
                        setFleetBoard = setFleetBoard,
                        allShips = allShips,
                        gamePhase = gamePhase
                    ),
                    startButton = {
                        viewModel.fetchGetCurrentGamePhase(
                            game?.gameId,
                            receivedExtra?.token
                        )
                    },
                    deleteAll = {
                        viewModel.deleteAll()
                    },
                    onClickFleetLayout = { ship: String, layout: String ->
                        viewModel.setShipLayout(ship, layout)
                    },
                    onPositionDefineFleet = { at: Position ->
                        viewModel.updateFleetBoard(at)
                    },
                    fetchSetFleet = {
                        viewModel.fetchSetFleet(
                            game?.gameId,
                            receivedExtra?.token,
                            allShips
                        )
                    },
                    onNavigationRequested = NavigationHandlers(
                        goHome = { finish() }
                    )
                )
            }else if (
                gamePhase.name == "SHOOTING_PLAYER_ONE" ||
                gamePhase.name == "SHOOTING_PLAYER_TWO"
            ) {
                GameScreen(
                    state = GameScreenState(
                        shootBoard=shootBoard,
                        myBoard = myFleetBoard,
                        setShootInfo = setShootInfo,
                        gamePhase = gamePhase,
                    ),
                    updateMyBoard = {
                        if (game?.gameId!=null) {
                            viewModel.fetchCheckFleet(game.gameId, true, receivedExtra?.token)
                        }
                                    },
                    shootOpponentBoard = {
                        viewModel.fetchSetShoot(game?.gameId,
                            receivedExtra?.token,it)
                    },
                    updateOpponentBoard = {
                        if (game?.gameId!=null) {
                            viewModel.fetchCheckFleet(game.gameId, false, receivedExtra?.token)
                        }
                    },
                    onClickGiveUpGame = {
                        viewModel.giveUpGame(receivedExtra?.token)
                    },
                    startButton = {
                        viewModel.fetchGetCurrentGamePhase(
                            game?.gameId,
                            receivedExtra?.token
                        )
                    },
                    onNavigationRequested = NavigationHandlers(
                        goHome = { finish() }
                    )
                )
            }else if(
                gamePhase.name == "PLAYER_ONE_WON" ||
                gamePhase.name == "PLAYER_TWO_WON"){
                PlayerWonScreen(
                    state = PlayerWonScreenState(
                        gamePhase = gamePhase,
                    ),
                    onNavigationRequested = NavigationHandlers(
                        goHome = { finish() }
                    )
                )
            }
            ProblemAlert(
                state = viewModel.newGame,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            InfoAlert(
                state = viewModel.newGame,
                title = stringResource(R.string.app_label_info_title),
                message = viewModel.newGame?.getOrNull() ?: "",
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            ProblemAlert(
                state = viewModel.giveUpLobby,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            InfoAlert(
                state = viewModel.giveUpLobby,
                title = stringResource(R.string.app_label_info_title),
                message = viewModel.giveUpLobby?.getOrNull()?.message ?: "",
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            ProblemAlert(
                state = viewModel.game,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            ProblemAlert(
                state = viewModel.gamePhase,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            ProblemAlert(
                state = viewModel.setFleetAnswer,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            InfoAlert(
                state = viewModel.setFleetAnswer,
                title = stringResource(R.string.app_label_info_title),
                message = viewModel.setFleetAnswer?.getOrNull() ?: "",
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            ProblemAlert(
                state = viewModel.setShoot,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            ProblemAlert(
                state = viewModel.giveUpGame,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
            InfoAlert(
                state = viewModel.giveUpGame,
                title = stringResource(R.string.app_label_info_title),
                message = viewModel.giveUpGame?.getOrNull()?.message ?: "",
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {finish()}
            )
        }
    }

    @Suppress("deprecation")
    private val tokenExtra: LocalTokenDto?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(MainActivity.TOKEN_EXTRA, LocalTokenDto::class.java)
            else
                intent.getParcelableExtra(MainActivity.TOKEN_EXTRA)

}
