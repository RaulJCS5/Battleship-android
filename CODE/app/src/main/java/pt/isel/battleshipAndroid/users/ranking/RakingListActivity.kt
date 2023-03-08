package pt.isel.battleshipAndroid.users.ranking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import pt.isel.battleshipAndroid.DependenciesContainer
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.ProblemAlert
import pt.isel.battleshipAndroid.utils.viewModelInit

/**
 * The activity that hosts the screen for displaying a list of ranking players.
 * */
class RakingListActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: RakingListScreenViewModel by viewModels {
        viewModelInit {
            RakingListScreenViewModel(dependencies.playerService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val loadingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            val rankList = viewModel.rakingState?.getOrNull() ?: emptyList()
            RankingListScreen(
                state = PlayersListScreenState(rankList, loadingState),
                onUpdateRequest = { viewModel.fetchRanking() },
                onNavigationRequested = NavigationHandlers(
                    goHome = { finish() },
                )
            )
            ProblemAlert(
                state = viewModel.rakingState,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )
        }
    }
}