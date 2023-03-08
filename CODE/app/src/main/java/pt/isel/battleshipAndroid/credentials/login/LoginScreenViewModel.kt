package pt.isel.battleshipAndroid.credentials.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.model.DefaultAnswerModel
import pt.isel.battleshipAndroid.users.model.UserLoginOutputModel
import pt.isel.battleshipAndroid.users.player.IPlayerService
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel

class LoginScreenViewModel(
    private val iPlayerService: IPlayerService
) : ViewModel() {

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    private var _isRecoveryLoading by mutableStateOf(false)
    val isRecoveryLoading: Boolean
        get() = _isRecoveryLoading

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

    private var _userLoggedData by mutableStateOf<Result<UserLoginOutputModel>?>(null)
    var userLoggedData: Result<UserLoginOutputModel>?
        get() = _userLoggedData
        set(value) {
            _userLoggedData = value
        }

    private var _userRecoveryData by mutableStateOf<Result<DefaultAnswerModel>?>(null)
    var userRecoveryData: Result<DefaultAnswerModel>?
        get() = _userRecoveryData
        set(value) {
            _userRecoveryData = value
        }

    private var _isRecovery by mutableStateOf(false)
    var isRecovery: Boolean
        get() = _isRecovery
        set(value) {
            _isRecovery = value
        }

    //to handle problem response
    var problem by mutableStateOf<ProblemOutputModel?>(null)

    fun fetchLogin() {
        viewModelScope.launch {
            problem = null
            _isLoading = true
            _userLoggedData = try {
                Result.success(
                    iPlayerService.fetchLogin(
                        username?.getOrNull()!!,
                        password?.getOrNull()!!
                    )
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

    fun fetchRecovery() {
        viewModelScope.launch {
            problem = null
            _isRecoveryLoading = true
            _userRecoveryData = try {
                Result.success(
                    iPlayerService.fetchRecovery(
                        email?.getOrNull()!!
                    )
                )
                //to handle problem exception
            } catch (e: ProblemException) {
                problem = e.problem
                Result.failure(e)
            } catch (e: Exception) {
                Result.failure(e)
            }
            _isRecoveryLoading = false
        }
    }
}