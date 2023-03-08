package pt.isel.battleshipAndroid.http

object Uris {

    const val RANKING = "/public/ranking/"

    object Users {
        const val HOME = "/me"
        const val REGISTER = "/users/register"
        const val LOGIN = "/users/auth"
        const val RECOVERY = "/users/recovery"
        const val LOGOUT = "/users/logout"
    }

    object Game {
        const val NEW_GAME = "/users/game/new"
        const val GIVE_UP_LOBBY = "/users/game/giveUpLobby"
        const val GIVE_UP_GAME = "/users/game/giveUp"
        const val LIST_GAME_HISTORY = "/users/game/history"
        const val GET_USER_CURRENT_GAME = "/users/game"
        fun getCurrentGamePhase(gameId: Int) = "/users/game/${gameId}/getCurrentPhase"
        fun setShoot(gameId: Int) = "/users/game/${gameId}/shoot"
        fun setLayoutFleet(gameId: Int) = "/users/game/${gameId}/setFleet"
        fun checkFleet(gameId: Int) = "/users/game/${gameId}/getFleet"
    }
}