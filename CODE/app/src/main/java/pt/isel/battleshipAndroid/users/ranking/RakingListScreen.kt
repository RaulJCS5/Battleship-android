package pt.isel.battleshipAndroid.users.ranking

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FabPosition
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.battleshipAndroid.ui.ShowPlayerRankView
import pt.isel.battleshipAndroid.ui.RefreshFab
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.utils.*
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.users.model.GameRankTotals
import pt.isel.battleshipAndroid.users.model.UserOutputModel
import pt.isel.battleshipAndroid.users.player.Player
import pt.isel.battleshipAndroid.users.player.PlayerScreen
import pt.isel.battleshipAndroid.users.player.PlayerScreenState

data class PlayersListScreenState(
    val rankings: List<GameRankTotals> = emptyList(),
    val isLoading: RefreshingState = RefreshingState.Idle
)

@Composable
fun RankingListScreen(
    state: PlayersListScreenState = PlayersListScreenState(),
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    onUpdateRequest: () -> Unit = { }
) {
    BattleshipAndroidTheme {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .testTag(NavigatePlayersListScreenTestTag),
            backgroundColor = MaterialTheme.colors.background,
            topBar = {
                TopBar(
                    navigation = onNavigationRequested,
                    R.string.app_players_list_screen_title
                )
            },
            floatingActionButton = {
                RefreshFab(
                    onClick = onUpdateRequest,
                    state = state.isLoading,
                    find = R.string.app_find_players_screen_button
                )
            },
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true
        ) { innerPadding ->
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(state.rankings) {
                    ShowPlayerRankView(
                        ranks = it,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun RankingListScreenPreview() {
    val rankings = mutableListOf<GameRankTotals>()
    rankings.add(GameRankTotals(UserOutputModel(99,"xpto","xpto@hotmail.com"),9,7,3,300))
    RankingListScreen(
        state = PlayersListScreenState(
                rankings = rankings
        )
    )
}