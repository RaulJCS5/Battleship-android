package pt.isel.battleshipAndroid.users.ranking

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import pt.isel.battleshipAndroid.users.model.GameRankTotals
import pt.isel.battleshipAndroid.users.player.IPlayerService
import pt.isel.battleshipAndroid.utils.ProblemException
import pt.isel.battleshipAndroid.utils.model.ProblemOutputModel

class RakingListScreenViewModel(
    private val iPlayerService: IPlayerService
) : ViewModel() {

    private var _isLoading by mutableStateOf(false)
    val isLoading: Boolean
        get() = _isLoading

    private var _rankingState by mutableStateOf<Result<List<GameRankTotals>>?>(null)
    val rakingState: Result<List<GameRankTotals>>?
        get() = _rankingState

    //to handle problem response
    var problem by mutableStateOf<ProblemOutputModel?>(null)

    fun fetchRanking() {
        viewModelScope.launch {
            problem = null
            _isLoading = true
            _rankingState =
                try {
                    Result.success(iPlayerService.fetchRanking())
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