package pt.isel.battleshipAndroid.users.author

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.main.LocalAuthorDto
import pt.isel.battleshipAndroid.users.player.Author
import pt.isel.battleshipAndroid.users.player.IPlayerService

class AuthorViewModel(private val authorService: IPlayerService) : ViewModel() {

    private var _author by mutableStateOf<Result<Author>?>(null)
    val author: Result<Author>?
        get() = _author

    fun setAuthors(receivedExtra: LocalAuthorDto) {

        viewModelScope.launch {
            _author =
                try {
                    Result.success(
                        Author(receivedExtra)
                    )
                } catch (e: Exception) {
                    Result.failure(e)
                }
        }
    }
}