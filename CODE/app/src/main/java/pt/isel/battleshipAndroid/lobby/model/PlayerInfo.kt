package pt.isel.battleshipAndroid.lobby.model

import pt.isel.battleshipAndroid.users.player.Player
import java.util.*

/**
 * Data type that characterizes the player information while he's in the lobby
 * @property info   The information entered by the user
 * @property id     An identifier used to distinguish players in the lobby
 */
data class PlayerInfo(val info: Player, val id: UUID = UUID.randomUUID())