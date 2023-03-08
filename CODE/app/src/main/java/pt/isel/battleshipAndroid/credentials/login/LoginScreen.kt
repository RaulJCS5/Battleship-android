package pt.isel.battleshipAndroid.credentials.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import pt.isel.battleshipAndroid.credentials.register.LoadingButton
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import pt.isel.battleshipAndroid.utils.*
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.users.model.UserLoginOutputModel


data class LoginScreenState(
    val userName: String = "",
    val password: String = "",
    val email: String = "",
    var userLoggedData: UserLoginOutputModel? = null,
    val isLoading: RefreshingState = RefreshingState.Idle,
    val isRecovery: Boolean = false,
    val isRecoveryLoading: RefreshingState = RefreshingState.Idle
)

@Composable
fun LoginScreen(
    userNameChange: (String) -> Unit = { },
    passwordChange: (String) -> Unit = { },
    emailChange: (String) -> Unit = { },
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    state: LoginScreenState = LoginScreenState(),
    onLoginRequest: () -> Unit = { },
    isRecoveryRequest: () -> Unit = { },
    buttonIsRecovery: () -> Unit = { }
) {
    BattleshipAndroidTheme() {
        Scaffold(
            topBar = { TopBar(onNavigationRequested, R.string.app_login_screen_title) }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it.calculateTopPadding())
            )
            {
                if (state.userLoggedData == null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Center)
                    ) {

                        if (!state.isRecovery) {

                            TextField(
                                value = state.userName,
                                onValueChange = userNameChange,
                                label = { Text(stringResource(id = R.string.app_username_label)) },
                                modifier = Modifier.align(CenterHorizontally)
                            )
                            TextField(
                                value = state.password,
                                onValueChange = passwordChange,
                                label = { Text(stringResource(id = R.string.app_password_label)) },
                                modifier = Modifier.align(CenterHorizontally),
                                visualTransformation = PasswordVisualTransformation(),
                            )
                            LoadingButton(
                                onLoginRequest,
                                state.isLoading,
                                R.string.app_login_screen_button,
                                Modifier
                                    .align(CenterHorizontally)
                                    .testTag(NavigateLoginButtonTestTag),
                                login = state
                            )
                            Button(
                                modifier = Modifier.align(CenterHorizontally),
                                onClick = buttonIsRecovery,
                            ) {
                                Text(stringResource(id = R.string.app_recovery_button))
                            }
                        } else {
                            TextField(
                                value = state.email,
                                onValueChange = emailChange,
                                label = { Text(stringResource(id = R.string.app_email_label)) },
                                modifier = Modifier.align(CenterHorizontally)
                            )
                            LoadingButton(
                                isRecoveryRequest,
                                state.isRecoveryLoading,
                                R.string.app_recovery_button,
                                Modifier
                                    .align(CenterHorizontally)
                                    .testTag(NavigateLoginButtonTestTag),
                                login = state
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoggedInScreenPreview() {
    val login = LoginScreenState()
    LoginScreen(state = login)
}