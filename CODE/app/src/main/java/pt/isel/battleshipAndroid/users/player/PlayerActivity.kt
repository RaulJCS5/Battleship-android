package pt.isel.battleshipAndroid.users.player

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.res.stringResource
import androidx.preference.PreferenceManager
import pt.isel.battleshipAndroid.DependenciesContainer
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.main.LocalTokenDto
import pt.isel.battleshipAndroid.main.MainActivity
import pt.isel.battleshipAndroid.ui.RefreshingState
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.ui.components.ProblemAlert
import pt.isel.battleshipAndroid.utils.viewModelInit
import java.net.URI

class PlayerActivity : ComponentActivity() {

    private val dependencies by lazy { application as DependenciesContainer }
    private val viewModel: PlayerViewModel by viewModels {
        viewModelInit {
            PlayerViewModel(dependencies.playerService)
        }
    }
    private var fetchFail = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val receivedExtra = tokenExtra

            if (receivedExtra != null && !fetchFail) {
                if (viewModel.user?.getOrNull() == null) {
                    viewModel.fetchUserMe(receivedExtra)
                }
            }
            val loadingState =
                if (viewModel.isLoading) RefreshingState.Refreshing
                else RefreshingState.Idle

            val fetchData = viewModel.user?.getOrNull()
            var player: Player? = null

            if (fetchData != null) {
                if (fetchData.username.isNotBlank() && fetchData.email.isNotBlank()) {
                    player = Player(
                        id = fetchData.id,
                        username = fetchData.username,
                        email = fetchData.email
                    )
                }
            }
            PlayerScreen(
                state = PlayerScreenState(
                    player,
                    loadingState
                ),
                onNavigationRequested = NavigationHandlers(
                    onBackRequested = { finish() }
                ),
            )

            ProblemAlert(
                state = viewModel.user,
                title = viewModel.problem?.title
                    ?: stringResource(R.string.app_api_title_error),
                message = viewModel.problem?.detail
                    ?: stringResource(R.string.app_api_label_error),
                buttonText = R.string.app_ok_label,
                onDismiss = {},
                onButton = {}
            )

            //Remove token extra if DAW API is now invalid
            if (viewModel.user != null) {
                if (viewModel.user!!.isFailure) {
                    fetchFail = true

                    if (viewModel.problem != null && viewModel.problem!!.type == URI(
                            stringResource(
                                R.string.app_api_invalid_token_type
                            )
                        )
                    ) {
                        invalidateSession()
                    }
                }
            }
        }
    }

    //Invalidate session
    private fun invalidateSession() {
        val sharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(this)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.remove("token")
        editor.remove("userId")
        editor.apply()
        //remove intent
        intent.removeExtra(MainActivity.TOKEN_EXTRA)
    }

    @Suppress("deprecation")
    private val tokenExtra: LocalTokenDto?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(MainActivity.TOKEN_EXTRA, LocalTokenDto::class.java)
            else
                intent.getParcelableExtra(MainActivity.TOKEN_EXTRA)

}