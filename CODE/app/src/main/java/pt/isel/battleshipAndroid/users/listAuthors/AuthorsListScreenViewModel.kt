package pt.isel.battleshipAndroid.users.listAuthors

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.users.player.Author
import pt.isel.battleshipAndroid.users.player.IPlayerService

class AuthorsListScreenViewModel(
    private val authorService: IPlayerService
) : ViewModel() {

    private var _authors by mutableStateOf<Result<List<Author>>?>(null)
    val authors: Result<List<Author>>?
        get() = _authors

    fun fetchAllAuthors() {
        viewModelScope.launch {
            _authors =
                try {
                    Result.success(authorService.getAllAuthors())
                }
                catch (e: Exception) { Result.failure(e) }
        }
    }
}