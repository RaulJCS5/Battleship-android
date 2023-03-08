package pt.isel.battleshipAndroid.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.game.model.Board
import pt.isel.battleshipAndroid.game.model.Position
import pt.isel.battleshipAndroid.lobby.AllShips
import pt.isel.battleshipAndroid.lobby.SetFleetInputModel
import pt.isel.battleshipAndroid.model.DefaultAnswerModel
import pt.isel.battleshipAndroid.model.GamePhase

data class GameScreenState(
    val shootBoard: Board? = null,
    val myBoard: Board? = null,
    val setFleetBoard: Board? = null,
    val allShips: MutableList<SetFleetInputModel>? = null,
    val setShootInfo: DefaultAnswerModel? = null,
    val gamePhase: GamePhase? = null,
)


@Composable
fun GameScreen(
    state: GameScreenState,
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    startButton: () -> Unit = { },
    fetchSetFleet: () -> Unit = { },
    deleteAll: () -> Unit = { },
    updateMyBoard: () -> Unit = { },
    onClickGiveUpGame: () -> Unit = { },
    shootOpponentBoard: (at: Position) -> Unit = { _ -> },
    updateOpponentBoard: () -> Unit = { },
    onClickFleetLayout: (String,String) -> Unit = { _: String, _: String -> },
    onPositionDefineFleet: ( Position) -> Unit = { _:Position -> },
) {
    BattleshipAndroidTheme() {
        Scaffold(
            topBar = { TopBar(onNavigationRequested, R.string.app_game) },
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(text = "${state.gamePhase?.name}")
                if (state.shootBoard != null) {
                    if (state.setShootInfo == null) {
                        Text(text = "Shoot board")
                    } else {
                        Text(text = "${state.setShootInfo.message}")
                    }
                    BoardView(
                        board = state.shootBoard,
                        onTileSelected = {
                            shootOpponentBoard(it)
                        },
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1.0f, true)
                            .fillMaxSize()
                    )
                    Button(onClick = updateOpponentBoard) {
                        Text(text = "Update opponent board")
                    }
                }
                if (state.myBoard != null) {
                    Text(text = "My board")
                    BoardView(
                        board = state.myBoard,
                        onTileSelected = { },
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1.0f, true)
                            .fillMaxSize()
                    )
                    Button(onClick = updateMyBoard) {
                        Text(text = "Update my board")
                    }
                }
                if (state.setFleetBoard != null && state.allShips != null) {
                    BoardFleetView(
                        board = state.setFleetBoard,
                        onPositionDefineFleet = onPositionDefineFleet,
                        modifier = Modifier
                            .padding(16.dp)
                            .weight(1.0f, true)
                            .fillMaxSize(),
                    )
                    FleetView(
                        fleet = state.allShips,
                        onClickFleetLayout = onClickFleetLayout
                    )
                    Row {
                        Button(onClick = deleteAll) {
                            Text(text = "Delete all")
                        }
                        if (state.allShips.find { it.referencePoint == null } == null) {
                            Spacer(modifier = Modifier.width(16.dp))
                            Button(onClick = fetchSetFleet) {
                                Text(text = "Set fleet")
                            }
                        }
                    }
                }
                Row {
                    Button(onClick = startButton) {
                        Text(text = "Refresh game phase")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(onClick = onClickGiveUpGame) {
                        Text(text = "Quit game")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DefineFleetPreview() {
    GameScreen(
        state = GameScreenState(
            setFleetBoard =  aBoard,
            allShips = AllShips().shipList.getOrNull(),
            gamePhase = GamePhase(1,"LAYOUT")
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun PlayGamePreview() {
    GameScreen(
        state = GameScreenState(
            shootBoard = aBoard,
            myBoard = aBoard,
            gamePhase = GamePhase(2,"SHOOTING_PLAYER_ONE")
        )
    )
}

private val aBoard = Board()