package pt.isel.battleshipAndroid.users.listAuthors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.battleshipAndroid.DependenciesContainer
import pt.isel.battleshipAndroid.users.author.AuthorActivity
import pt.isel.battleshipAndroid.users.player.toLocalAuthorDto
import pt.isel.battleshipAndroid.main.MainActivity.Companion.AUTHOR_EXTRA
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.utils.NavigateAux
import pt.isel.battleshipAndroid.utils.viewModelInit

/**
 * The activity that hosts the screen for displaying a list of quotes. The
 * list to be displayed is fetched from the API and it bears this week's quotes.
 */
class AuthorsListActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: AuthorsListScreenViewModel by viewModels {
        viewModelInit {
            AuthorsListScreenViewModel(dependencies.playerService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (viewModel.authors == null)
                viewModel.fetchAllAuthors()
            val authors = viewModel.authors?.getOrNull() ?: emptyList()
            AuthorsListScreen(
                state = AuthorsListScreenState(authors),
                onAuthorSelected = {
                    NavigateAux.navigateTo<AuthorActivity>(
                        ctx = this, AUTHOR_EXTRA,
                        it.toLocalAuthorDto()
                    )
                },
                onNavigationRequested = NavigationHandlers(
                    goHome = { finish() },
                )
            )
        }
    }
}