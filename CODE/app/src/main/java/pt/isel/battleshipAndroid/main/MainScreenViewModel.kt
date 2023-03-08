package pt.isel.battleshipAndroid.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.users.player.IPlayerService

class MainScreenViewModel(
    private val service: IPlayerService
) : ViewModel() {

    private var _token by mutableStateOf<Result<String>?>(null)
    var token: Result<String>?
        get() = _token
        set(value) {
            _token = value
        }

    fun fetchLogout(tokenAuthorize: String?) {
        viewModelScope.launch {
            try {
                Result.success(
                    service.fetchLogout(tokenAuthorize ?: "")
                )
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}