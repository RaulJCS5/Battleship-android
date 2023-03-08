package pt.isel.battleshipAndroid

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import io.mockk.coEvery
import io.mockk.mockk
import pt.isel.battleshipAndroid.game.IGameService
import pt.isel.battleshipAndroid.lobby.ILobbyService
import pt.isel.battleshipAndroid.users.player.Author
import pt.isel.battleshipAndroid.users.player.IPlayerService
import pt.isel.battleshipAndroid.users.player.Player

class BattleshipTestApplication : DependenciesContainer, Application() {
    override var playerService: IPlayerService = mockk {
        coEvery { getPlayer() } returns
                Player(username = "Test user", email = "Test@alunos.isel.pt")
        coEvery { getAuthor() } returns//TODO: NOT DONE
                Author(name = "", number = 0, email = "", username = "", github_http = "", linkedin_http = "", ic_author = 0)

        coEvery { fetchRanking() } returns
                buildList {
                    for (count in 1..5) {
                        add(
                            Player(
                                username = "Test user $count",
                                email = "Test$count@alunos.isel.pt"
                            )
                        )
                    }
                }
        coEvery { getAllAuthors() } returns//TODO: NOT DONE
                buildList {
                    for (count in 1..5) {
                        add(
                            Author(
                                name = "",
                                number = 0,
                                email = "",
                                username = "",
                                github_http = "",
                                linkedin_http = "",
                                ic_author = 0)
                        )
                    }
                }
    }
    override val gameService: IGameService
        get() = TODO("Not yet implemented")
    override val lobbyService: ILobbyService
        get() = TODO("Not yet implemented")
}

@Suppress("unused")
class BattleshipTestRunner : AndroidJUnitRunner() {
    override fun newApplication(cl: ClassLoader, className: String, context: Context): Application {
        return super.newApplication(cl, BattleshipTestApplication::class.java.name, context)
    }
}
