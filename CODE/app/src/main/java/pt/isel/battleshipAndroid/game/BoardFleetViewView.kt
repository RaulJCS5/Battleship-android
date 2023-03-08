package pt.isel.battleshipAndroid.game

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pt.isel.battleshipAndroid.game.model.Board
import pt.isel.battleshipAndroid.game.model.Position

@Composable
fun BoardFleetView(
    board: Board,
    onPositionDefineFleet: (Position) -> Unit,
    modifier: Modifier,
) {
    BoardView(
        board = board,
        onTileSelected = {
            onPositionDefineFleet(it)
        },
        modifier = modifier
    )
}
