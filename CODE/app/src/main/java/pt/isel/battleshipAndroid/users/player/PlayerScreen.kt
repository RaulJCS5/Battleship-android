package pt.isel.battleshipAndroid.users.player

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.utils.*

data class PlayerScreenState(
    val player: Player? = null,
    val isLoading: RefreshingState = RefreshingState.Idle
)

@Composable
fun PlayerScreen(
    state: PlayerScreenState = PlayerScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
) {
    BattleshipAndroidTheme() {
        Scaffold(
            topBar = { TopBar(onNavigationRequested, R.string.app_player_home) },
        ) { innerPadding ->
            Column(
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
            ) {

                if (state.isLoading == RefreshingState.Refreshing) {
                    Text(stringResource(R.string.fetch_button_text_loading))
                } else {
                    if (state.player != null) {
                        PlayerHomeView(
                            id = state.player.id,
                            username = state.player.username,
                            email = state.player.email,
                            ic = R.drawable.clipart3240117,
                            modifier = Modifier
                                .padding(16.dp)
                                .testTag(NavigateAuthorTestTag)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PlayerHomeView(id: Int, username: String, email: String, ic: Int?, modifier: Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        var icPicture = ic
        if (icPicture == null)
            icPicture = R.drawable.clipart3240117
        Image(
            painter = painterResource(id = icPicture),
            contentDescription = null,
            modifier = Modifier.sizeIn(100.dp, 100.dp, 200.dp, 200.dp)
        )
        Text(text = id.toString(), style = MaterialTheme.typography.h5)
        Text(text = username, style = MaterialTheme.typography.h5)
        Text(text = email, style = MaterialTheme.typography.h5)
    }
}

@Preview(showBackground = true)
@Composable
private fun PlayerPreview() {
    val state = PlayerScreenState(
        Player(
            id = 999,
            username = "Unknown1",
            email = "Unknown1@alunos.isel.pt",
        )
    )
    PlayerScreen(
        state = state
    )
}