package pt.isel.battleshipAndroid.credentials.register

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pt.isel.battleshipAndroid.credentials.login.LoginScreenState
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.TopBar
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme
import pt.isel.battleshipAndroid.utils.*
import pt.isel.battleshipAndroid.R

data class RegisterScreenState(
    val userName: String = "",
    val password: String = "",
    val email: String = "",
    val isLoading: RefreshingState = RefreshingState.Idle
)

@Composable
fun RegisterScreen(
    userNameChange: (String) -> Unit = { },
    passwordChange: (String) -> Unit = { },
    emailChange: (String) -> Unit = { },
    onNavigationRequested: NavigationHandlers = NavigationHandlers(),
    state: RegisterScreenState = RegisterScreenState(),
    onRegisterRequest: () -> Unit = { },
) {
    BattleshipAndroidTheme() {
        Scaffold(
            topBar = { TopBar(onNavigationRequested, R.string.app_register_screen_title) }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it.calculateTopPadding())
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                ) {
                    TextField(
                        value = state.userName,
                        onValueChange = userNameChange,
                        label = { Text(stringResource(id = R.string.app_username_label)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    TextField(
                        value = state.email,
                        onValueChange = emailChange,
                        label = { Text(stringResource(id = R.string.app_email_label)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    TextField(
                        value = state.password,
                        onValueChange = passwordChange,
                        label = { Text(stringResource(id = R.string.app_password_label)) },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        visualTransformation = PasswordVisualTransformation(),
                    )
                    LoadingButton(
                        onRegisterRequest,
                        state.isLoading,
                        R.string.app_register_screen_button,
                        Modifier
                            .align(Alignment.CenterHorizontally)
                            .testTag(NavigateRegisterButtonTestTag),
                        register = state
                    )
                }
            }
        }
    }
}

@Composable
fun LoadingButton(
    onUpdateRequest: () -> Unit,
    loading: RefreshingState,
    appLoginScreenButton: Int,
    modifier: Modifier,
    login: LoginScreenState? = null,
    register: RegisterScreenState? = null,
) {
    val check: Boolean = loading == RefreshingState.Idle
    var keepCheck = true
    if (login != null) {
        keepCheck =
            (login.userName.isNotBlank() && login.password.isNotBlank()) || login.email.isNotBlank()
    } else if (register != null) {
        keepCheck =
            register.userName.isNotBlank() && register.password.isNotBlank() && register.email.isNotBlank()
    }
    Button(
        onClick = onUpdateRequest,
        enabled = check && keepCheck,
        modifier = modifier,
    ) {
        val buttonTextId = if (loading == RefreshingState.Idle) appLoginScreenButton
        else R.string.fetch_button_text_loading
        Text(text = stringResource(id = buttonTextId))
    }
}

@Preview(showBackground = true)
@Composable
private fun RegisterInScreenPreview() {
    val register = RegisterScreenState()
    RegisterScreen(state = register)
}
