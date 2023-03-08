package pt.isel.battleshipAndroid.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.game.model.Board
import pt.isel.battleshipAndroid.lobby.SetFleetInputModel
import pt.isel.battleshipAndroid.model.DefaultAnswerModel
import pt.isel.battleshipAndroid.model.GamePhase
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme

data class PlayerWonScreenState(
    val gamePhase: GamePhase? = null,
)
@Composable
fun PlayerWonScreen(
    state: PlayerWonScreenState,
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    ) {
    BattleshipAndroidTheme {
        Scaffold(
            topBar = { TopBar(onNavigationRequested, R.string.app_game_winner) },
        ) { innerPadding ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {
                Text(text = "${state.gamePhase?.name}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayGamePreview() {
    PlayerWonScreen(
        state = PlayerWonScreenState(
            gamePhase = GamePhase(2,"SHOOTING_PLAYER_ONE")
        )
    )
}