package pt.isel.battleshipAndroid.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import pt.isel.battleshipAndroid.utils.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar

@Composable
fun MainScreen(
    navigateToRegister: (() -> Unit)? =null,
    navigateToLogin: (() -> Unit)? =null,
    navigateToLeaderBoard: (() -> Unit)? =null,
    navigateToAuthor: (() -> Unit)? =null,
    navigateToLobby: (() -> Unit)? =null,
    onLogoutRequest: (() -> Unit)? =null,
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
) {
    BattleshipAndroidTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MainScreenView(
                    navigateToRegister,
                    navigateToLogin,
                    navigateToLeaderBoard,
                    navigateToAuthor,
                    navigateToLobby,
                    onLogoutRequest,
                    onNavigationRequested
                )
            }
        }
    }
}

@Composable
fun MainScreenView(
    navigateToRegister: (() -> Unit)?,
    navigateToLogin: (() -> Unit)?,
    navigateToLeaderBoard: (() -> Unit)?,
    navigateToAuthor: (() -> Unit)?,
    navigateToLobby: (() -> Unit)?,
    onLogoutRequest: (() -> Unit)?,
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
) {
    Scaffold(
        topBar = { TopBar(onNavigationRequested, R.string.app_name) }) {
        Box(
            modifier = Modifier
                .padding(it.calculateTopPadding())
        )
        {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.clipart68049),
                    contentDescription = "",
                    modifier = Modifier.sizeIn(
                        80.dp, 80.dp,
                        200.dp, 200.dp
                    ).align(Alignment.CenterHorizontally)
                )
                if (navigateToRegister != null)
                    Button(
                        onClick = navigateToRegister,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag(NavigateMainRegisterButtonTestTag)
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_register_screen_button),
                            textAlign = TextAlign.Center
                        )
                    }
                if (navigateToLogin != null)
                    Button(
                        onClick = navigateToLogin,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag(NavigateMainLoginButtonTestTag)
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_login_screen_button),
                            textAlign = TextAlign.Center
                        )
                    }
                if (navigateToLeaderBoard != null)
                    Button(
                        onClick = navigateToLeaderBoard,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag(NavigateMainLeaderboardButtonTestTag)
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_leaderboard_screen_button),
                            textAlign = TextAlign.Center
                        )
                    }
                if (navigateToAuthor != null)
                    Button(
                        onClick = navigateToAuthor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag(NavigateMainAuthorButtonTestTag)
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_author_screen_button),
                            textAlign = TextAlign.Center
                        )
                    }
                if (navigateToLobby != null)
                    Button(
                        onClick = navigateToLobby,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag(NavigateMainGameButtonTestTag)
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_lobby_screen_button),
                            textAlign = TextAlign.Center
                        )
                    }
                if (onLogoutRequest != null)
                    Button(
                        onClick = onLogoutRequest,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                            .testTag(NavigateMainLogoutButtonTestTag)
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_logout_screen_button),
                            textAlign = TextAlign.Center
                        )
                    }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainPreview() {
    MainScreen(
        navigateToRegister = {},
        navigateToLogin = {},
        navigateToLeaderBoard = {},
        navigateToAuthor = {},
        onLogoutRequest = {}
    )
}