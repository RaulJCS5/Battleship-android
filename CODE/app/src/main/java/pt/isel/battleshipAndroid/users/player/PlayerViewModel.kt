package pt.isel.battleshipAndroid.users.player

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.main.LocalTokenDto
import pt.isel.battleshipAndroid.users.model.UserOutputModel
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel

class PlayerViewModel(private val iPlayerService: IPlayerService) : ViewModel() {

    private var _user by mutableStateOf<Result<UserOutputModel>?>(null)
    val user: Result<UserOutputModel>?
        get() = _user


    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    //to handle problem response
    var problem by mutableStateOf<ProblemOutputModel?>(null)

    fun fetchUserMe(receivedExtra: LocalTokenDto) {
        viewModelScope.launch {
            problem = null
            _isLoading = true
            _user =
                try {
                    if (receivedExtra.token != null) {
                        Result.success(iPlayerService.fetchUserMe(receivedExtra.token))
                    } else {
                        Result.failure(Exception("Invalid token!"))
                    }
                } catch (e: ProblemException) {
                    problem = e.problem
                    Result.failure(e)
                } catch (e: Exception) {
                    Result.failure(e)
                }
            _isLoading = false
        }
    }
}

