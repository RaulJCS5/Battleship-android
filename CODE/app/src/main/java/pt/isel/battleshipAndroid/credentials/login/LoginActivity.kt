package pt.isel.battleshipAndroid.credentials.login

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.PreferenceManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import pt.isel.battleshipAndroid.DependenciesContainer
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.credentials.StoreCredentialsActivity
import pt.isel.battleshipAndroid.main.MainActivity
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.InfoAlert
import pt.isel.battleshipAndroid.ui.components.ProblemAlert
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.utils.NavigateAux
import pt.isel.battleshipAndroid.utils.viewModelInit

class LoginActivity : StoreCredentialsActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: LoginScreenViewModel by viewModels {
        viewModelInit {
            LoginScreenViewModel(dependencies.playerService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContent {
            val loadingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            val userName = viewModel.username?.getOrNull() ?: ""
            val password = viewModel.password?.getOrNull() ?: ""
            val email = viewModel.email?.getOrNull() ?: ""
            val userLoggedData = viewModel.userLoggedData?.getOrNull()
            val isRecovery = viewModel.isRecovery

            val recoveryLoading =
                if (viewModel.isRecoveryLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            LoginScreen(
                { viewModel.username = Result.success(it) },
                { viewModel.password = Result.success(it) },
                onNavigationRequested = NavigationHandlers(
                    goHome = {
                        finish()
                    },
                ),
                state = LoginScreenState(
                    userName,
                    password,
                    email,
                    userLoggedData,
                    loadingState,
                    isRecovery,
                    isRecoveryLoading = recoveryLoading
                ),

                onLoginRequest = {
                    viewModel.fetchLogin()
                },
                isRecoveryRequest = {
                    viewModel.fetchRecovery()
                },
                emailChange = {
                    viewModel.email = Result.success(it)
                },
                buttonIsRecovery = {
                    viewModel.isRecovery = true
                }
            )

            //if login success
            if (userLoggedData != null) {
                //store login data in sharedPreferences
                val sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(this)
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString("token", userLoggedData.token)
                editor.putInt("userId", userLoggedData.userId)
                editor.apply()
                //then navigate to MainActivity
                NavigateAux.navigateTo<MainActivity>(this)
            }

            ProblemAlert(
                state = viewModel.userLoggedData,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )

            //get recovery result from view model
            val userRecoveryDataState = viewModel.userRecoveryData

            ProblemAlert(
                state = userRecoveryDataState,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )

            if (userRecoveryDataState != null) {
                InfoAlert(
                    state = userRecoveryDataState,
                    title = stringResource(R.string.app_label_info_title),
                    message = userRecoveryDataState.getOrNull()?.message ?: "",
                    buttonText = R.string.app_ok_label,
                    onDismiss = {},
                    onButton = {}
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
        onLoggedIn(viewModel)
    }

    override fun onStop() {
        super.onStop()
    }
}
