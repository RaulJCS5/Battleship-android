package pt.isel.battleshipAndroid.game.model

import pt.isel.battleshipAndroid.model.ShipType

data class PositionStateBoard(
    val boardPosition: Position,
    var wasShoot: Boolean,
    var wasShip: Boolean?,
    var shipType: ShipType?,
    var shipLayout: String?
)