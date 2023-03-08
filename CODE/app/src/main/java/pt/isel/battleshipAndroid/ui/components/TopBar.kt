package pt.isel.battleshipAndroid.ui.components

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.utils.*

/**
 * Used to aggregate [TopBar] navigation handlers.
 */
data class NavigationHandlers(
    val onBackRequested: (() -> Unit)? = null,
    val onLoggedIn: (() -> Unit)? = null,
    val onNotLoggedIn: (() -> Unit)? = null,
    val goHome: (() -> Unit)? = null,
)

@Composable
fun TopBar(navigation: NavigationHandlers = NavigationHandlers(), navigationId: Int) {
    TopAppBar(
        title = { Text(text = stringResource(id = navigationId)) },
        navigationIcon = {
            if (navigation.onBackRequested != null) {
                IconButton(
                    onClick = navigation.onBackRequested,
                    modifier = Modifier.testTag(NavigateBackTestTag)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = stringResource(id = R.string.top_bar_go_back)
                    )
                }
            }
            if (navigation.goHome != null) {
                IconButton(
                    onClick = navigation.goHome,
                    modifier = Modifier.testTag(NavigateToHomeTestTag)
                ) {
                    Icon(
                        Icons.Default.Home,
                        contentDescription = stringResource(id = R.string.top_bar_go_home)
                    )
                }
            }
        },
        actions = {
            if (navigation.onNotLoggedIn != null) {
                IconButton(
                    onClick = navigation.onNotLoggedIn,
                    modifier = Modifier.testTag(NavigateToInfoTestTag)
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = stringResource(id = R.string.top_bar_not_logged_in)
                    )
                }
            }
            if (navigation.onLoggedIn != null) {
                IconButton(
                    onClick = navigation.onLoggedIn,
                    modifier = Modifier.testTag(NavigateToInfoTestTag)
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = stringResource(id = R.string.top_bar_logged_in)
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun TopBarPreviewBack() {
    BattleshipAndroidTheme {
        TopBar(NavigationHandlers(onBackRequested = { }), R.string.top_bar_not_logged_in)
    }
}

@Preview
@Composable
private fun TopBarPreviewHome() {
    BattleshipAndroidTheme {
        TopBar(NavigationHandlers(goHome = { }), R.string.top_bar_go_home)
    }
}
