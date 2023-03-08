package pt.isel.battleshipAndroid.game.model

data class Board(
    val tiles: MutableList<MutableList<PositionStateBoard>>? =
        MutableList(
            size = BOARD_SIDE,
            init = { row->
                MutableList(size = BOARD_SIDE, init = { col ->
                PositionStateBoard(
                    boardPosition = Position(row, col),
                    wasShoot = false, wasShip = false, shipType = null, shipLayout = null
                )
            }) }
        )
) {
    operator fun get(at: Position): PositionStateBoard? = getMove(at)

    fun getMove(at: Position): PositionStateBoard? = tiles?.get(at.row)?.get(at.col)
}
