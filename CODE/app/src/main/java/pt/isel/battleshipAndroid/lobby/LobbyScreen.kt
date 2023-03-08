package pt.isel.battleshipAndroid.lobby

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.ui.RefreshFab
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.model.GameOutputModel
import pt.isel.battleshipAndroid.model.GamePhase

data class LobbyScreenState(
    val maxShot: String = "",
    val game: GameOutputModel? = null,
    val gamePhase: GamePhase? = null,
    val loadingState: RefreshingState = RefreshingState.Idle,
    val loadingQuitState: RefreshingState = RefreshingState.Idle,
    val loadingStateGamePhase: RefreshingState = RefreshingState.Idle,
    val loadingCheckGameState: RefreshingState = RefreshingState.Idle,
)



@Composable
fun LobbyScreen(
    state: LobbyScreenState = LobbyScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    maxShotChange: (String) -> Unit = { },
    onLobbyRequest: (() -> Unit)? = null,
    onCheckIfUserInGame: (() -> Unit)? = null,
    onQuitRequest: (() -> Unit)? = null,
    onCheckCurrentGamePhase: (() -> Unit)? = null,
) {
    BattleshipAndroidTheme() {
        Scaffold(
            topBar = { TopBar(onNavigationRequested, R.string.app_lobby) },
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                Text(
                    text = "Game id = ${state.game?.gameId.toString()}",
                    modifier = Modifier
                        .padding(it.calculateTopPadding()),
                )
                Text(
                    text = "Game phase = ${state.gamePhase?.name.toString()}",
                    modifier = Modifier
                        .padding(it.calculateTopPadding()),
                )
                if (state.game == null) {
                    Box(
                        modifier = Modifier
                            .padding(it.calculateTopPadding())
                    )
                    {
                        TextField(
                            value = state.maxShot,
                            onValueChange = maxShotChange,
                            label = { Text(stringResource(id = R.string.app_maxShot_label)) },
                            modifier = Modifier
                                .padding(it.calculateTopPadding()),
                            keyboardOptions =
                            KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)

                        )
                    }
                    if (onLobbyRequest != null) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Box(
                            modifier = Modifier
                                .padding(it.calculateTopPadding())
                        )
                        {
                            RefreshFab(
                                onClick = onLobbyRequest,
                                state = state.loadingState,
                                find = R.string.app_join_lobby_screen_button
                            )
                        }
                    }
                    if (onQuitRequest != null) {
                        Spacer(modifier = Modifier.padding(10.dp))
                        Box(
                            modifier = Modifier
                                .padding(it.calculateTopPadding())
                        )
                        {
                            RefreshFab(
                                onClick = onQuitRequest,
                                state = state.loadingQuitState,
                                find = R.string.app_quit_lobby_screen_button
                            )
                        }
                    }
                }
                if (onCheckIfUserInGame != null) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Box(
                        modifier = Modifier
                            .padding(it.calculateTopPadding())
                    )
                    {
                        RefreshFab(
                            onClick = onCheckIfUserInGame,
                            state = state.loadingCheckGameState,
                            find = R.string.app_check_if_user_in_game_screen_button
                        )

                    }
                }
                if (state.game != null && onCheckCurrentGamePhase != null) {
                    Spacer(modifier = Modifier.padding(10.dp))
                    Box(
                        modifier = Modifier
                            .padding(it.calculateTopPadding())
                    )
                    {
                        RefreshFab(
                            onClick = onCheckCurrentGamePhase,
                            state = state.loadingStateGamePhase,
                            find = R.string.app_current_game_phase_screen_button
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LobbyMaxShotsPreview() {
    LobbyScreen(
        maxShotChange = {},
        onLobbyRequest = {},
        onCheckCurrentGamePhase = {},
        onQuitRequest = {},
        onCheckIfUserInGame = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun LobbyPreview() {
    LobbyScreen(
        state = LobbyScreenState(
            game = GameOutputModel(99),
            gamePhase = GamePhase(1,"LAYOUT")
        ),
        maxShotChange = {},
        onLobbyRequest = {},
        onCheckCurrentGamePhase = {},
        onQuitRequest = {},
        onCheckIfUserInGame = {},
    )
}
