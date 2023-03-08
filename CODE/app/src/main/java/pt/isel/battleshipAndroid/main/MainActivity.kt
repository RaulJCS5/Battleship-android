package pt.isel.battleshipAndroid.main

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import pt.isel.battleshipAndroid.R
import pt.isel.battleshipAndroid.credentials.login.LoginActivity
import pt.isel.battleshipAndroid.credentials.register.RegisterActivity
import pt.isel.battleshipAndroid.lobby.LobbyActivity
import pt.isel.battleshipAndroid.ui.components.NavigationHandlers
import pt.isel.battleshipAndroid.users.listAuthors.AuthorsListActivity
import pt.isel.battleshipAndroid.users.player.PlayerActivity
import pt.isel.battleshipAndroid.users.ranking.RakingListActivity
import pt.isel.battleshipAndroid.utils.NavigateAux
import pt.isel.battleshipAndroid.utils.viewModelInit


class MainActivity : BaseActivity() {
    companion object {
        //This is for login and register navigate from login to main with success login
        const val TOKEN_EXTRA =
            "TOKEN_EXTRA"

        //From a list of players select one and go for his information
        const val PLAYER_EXTRA =
            "PLAYER_EXTRA"

        //From a list of authors select one and go for his information
        const val AUTHOR_EXTRA =
            "AUTHOR_EXTRA"
    }

    private val viewModel: MainScreenViewModel by viewModels {
        viewModelInit {
            MainScreenViewModel(dependencies.playerService)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //init share preferences to store login information
            val sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this)

            var receivedExtra = tokenExtra

            //check if is necessary check shared preferences
            if (receivedExtra?.token == null) {
                val sharedToken = sharedPreferences.getString("token", null)
                val sharedUserId = sharedPreferences.getInt("userId", 0)
                //if exists login stored, use them
                if (sharedToken != null && sharedUserId > 0) {
                    receivedExtra = LocalTokenDto(sharedToken, sharedUserId)
                }
            }

            if ((receivedExtra?.token != null) && receivedExtra.token!!.isNotBlank()) {

                viewModel.token = Result.success(receivedExtra.token!!)
                MainScreen(
                    navigateToLeaderBoard = { NavigateAux.navigateTo<RakingListActivity>(this) },
                    navigateToAuthor = { NavigateAux.navigateTo<AuthorsListActivity>(this) },
                    navigateToLobby = {
                        NavigateAux.navigateTo<LobbyActivity>(
                            this,
                            TOKEN_EXTRA,
                            receivedExtra
                        )
                                      },
                    onLogoutRequest = {
                        viewModel.fetchLogout(receivedExtra.token)
                        intent.removeExtra(TOKEN_EXTRA)
                        // ** start remove user info from shared preferences on logout **
                        val editor: SharedPreferences.Editor = sharedPreferences.edit()
                        editor.remove("token")
                        editor.remove("userId")
                        editor.apply()
                        // ** end remove user info from shared preferences on logout**
                        this.recreate()
                    },
                    onNavigationRequested = NavigationHandlers(
                        onLoggedIn = {
                            NavigateAux.navigateTo<PlayerActivity>(
                                this,
                                TOKEN_EXTRA,
                                receivedExtra
                            )
                        },
                    )
                )
            } else {
                MainScreen(
                    navigateToRegister = {
                        NavigateAux.navigateTo<RegisterActivity>(this)
                    },
                    navigateToLogin = {
                        NavigateAux.navigateTo<LoginActivity>(this)
                        finish()
                    },
                    navigateToLeaderBoard = { NavigateAux.navigateTo<RakingListActivity>(this) },
                    navigateToAuthor = { NavigateAux.navigateTo<AuthorsListActivity>(this) },
                    onNavigationRequested = NavigationHandlers(
                        onNotLoggedIn = {
                            val text = R.string.app_not_login
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(applicationContext, text, duration)
                            toast.show()
                        }
                    )
                )
            }
        }
    }


    @Suppress("deprecation")
    private val tokenExtra: LocalTokenDto?
        get() =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                intent.getParcelableExtra(TOKEN_EXTRA, LocalTokenDto::class.java)
            else
                intent.getParcelableExtra(TOKEN_EXTRA)
}