package pt.isel.battleshipAndroid

import android.app.Application
import com.google.gson.Gson
import okhttp3.OkHttpClient
import pt.isel.battleshipAndroid.users.player.IPlayerService
import pt.isel.battleshipAndroid.game.IGameService
import pt.isel.battleshipAndroid.game.GameService
import pt.isel.battleshipAndroid.lobby.ILobbyService
import pt.isel.battleshipAndroid.lobby.LobbyService
import pt.isel.battleshipAndroid.users.player.PlayerService

private const val API_HOME = "http://10.0.2.2:8080/api"

class BattleshipApplication : DependenciesContainer, Application() {

    override val playerService: IPlayerService by lazy {
        PlayerService(
            httpClient = httpClient,
            gson = jsonEncoder,
            homeUrl = API_HOME
        )
    }
    override val gameService: IGameService by lazy {
        GameService(
            httpClient = httpClient,
            gson = jsonEncoder,
            homeUrl = API_HOME
        )
    }

    override val lobbyService: ILobbyService by lazy {
        LobbyService(
            httpClient = httpClient,
            gson = jsonEncoder,
            homeUrl = API_HOME
        )
    }

    private val jsonEncoder: Gson by lazy {
        Gson()
    }
    private val httpClient by lazy {
        OkHttpClient()
    }
}

interface DependenciesContainer {
    val playerService: IPlayerService
    val gameService: IGameService
    val lobbyService: ILobbyService
}
