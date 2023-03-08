package pt.isel.battleshipAndroid.users.model

data class UserOutputModel(
    val id: Int,
    val username: String,
    val email: String
)

data class UserLoginOutputModel(
    var userId: Int,
    var token: String,
)

data class GameRankTotals(
    val user: UserOutputModel,
    val playedGames: Int,
    val winGames: Int,
    val lostGames: Int,
    val rankPoints: Int
)