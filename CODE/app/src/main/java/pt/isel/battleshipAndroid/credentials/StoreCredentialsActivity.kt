package pt.isel.battleshipAndroid.credentials

import androidx.activity.ComponentActivity
import pt.isel.battleshipAndroid.credentials.login.LoginScreenViewModel
import pt.isel.battleshipAndroid.main.LocalTokenDto
import pt.isel.battleshipAndroid.main.MainActivity
import pt.isel.battleshipAndroid.utils.NavigateAux

open class StoreCredentialsActivity : ComponentActivity() {
    fun onLoggedIn(viewModel: LoginScreenViewModel) {
        val getUserLoggedData = viewModel.userLoggedData?.getOrNull()
        if (getUserLoggedData != null)
            NavigateAux.navigateTo<MainActivity>(
                ctx = this,
                MainActivity.TOKEN_EXTRA,
                obj = LocalTokenDto(
                    token = getUserLoggedData.token,
                    userId = getUserLoggedData.userId
                )
            )
        else
            NavigateAux.navigateTo<MainActivity>(ctx = this)
        finish()
    }
}
