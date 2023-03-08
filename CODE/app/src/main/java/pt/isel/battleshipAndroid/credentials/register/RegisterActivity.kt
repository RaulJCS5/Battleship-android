package pt.isel.battleshipAndroid.credentials.register

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import pt.isel.battleshipAndroid.DependenciesContainer
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.credentials.StoreCredentialsActivity
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.InfoAlert
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.ProblemAlert
import pt.isel.battleshipAndroid.utils.viewModelInit

class RegisterActivity : StoreCredentialsActivity() {

    private val dependencies by lazy { application as DependenciesContainer }

    private val viewModel: RegisterScreenViewModel by viewModels {
        viewModelInit {
            RegisterScreenViewModel(dependencies.playerService)
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

            RegisterScreen(
                { viewModel.username = Result.success(it) },
                { viewModel.password = Result.success(it) },
                { viewModel.email = Result.success(it) },
                onNavigationRequested = NavigationHandlers(
                    goHome = {
                        finish()
                    },
                ),
                state = RegisterScreenState(userName, password, email, loadingState),
                onRegisterRequest = {
                    viewModel.fetchRegister()
                    viewModel.username = null
                    viewModel.password = null
                    viewModel.email = null
                }
            )

            //get register result from view model
            val userRegisterState = viewModel.userRegisterResult

            ProblemAlert(
                state = userRegisterState,
                title = viewModel.problem?.title ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )

            if (userRegisterState != null) {
                InfoAlert(
                    state = userRegisterState,
                    title = stringResource(R.string.app_label_info_title),
                    message = userRegisterState.getOrNull()?.message ?: "",
                    buttonText = R.string.app_ok_label,
                    onDismiss = {},
                    onButton = {}
                )
            }
        }
    }
}
