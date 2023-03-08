package pt.isel.battleshipAndroid.credentials.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.model.DefaultAnswerModel
import pt.isel.battleshipAndroid.users.player.IPlayerService
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel

class RegisterScreenViewModel(
    private val iPlayerService: IPlayerService
) : ViewModel() {

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    private var _username by mutableStateOf<Result<String>?>(null)
    var username: Result<String>?
        get() = _username
        set(value) {
            _username = value
        }

    private var _email by mutableStateOf<Result<String>?>(null)
    var email: Result<String>?
        get() = _email
        set(value) {
            _email = value
        }

    private var _password by mutableStateOf<Result<String>?>(null)
    var password: Result<String>?
        get() = _password
        set(value) {
            _password = value
        }

    private var _userRegisterResult by mutableStateOf<Result<DefaultAnswerModel>?>(null)
    var userRegisterResult: Result<DefaultAnswerModel>?
        get() = _userRegisterResult
        set(value) {
            _userRegisterResult = value
        }

    //to handle problem response
    var problem by mutableStateOf<ProblemOutputModel?>(null)

    fun fetchRegister() {
        viewModelScope.launch {
            problem = null
            _isLoading = true
            _userRegisterResult = try {
                val success = iPlayerService.fetchRegister(
                    username?.getOrNull()!!,
                    password?.getOrNull()!!,
                    email?.getOrNull()!!
                )
                Result.success(
                    success
                )
                //to handle problem exception
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