package pt.isel.battleshipAndroid.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.game.model.BOARD_SIDE
import pt.isel.battleshipAndroid.game.model.Board
import pt.isel.battleshipAndroid.game.model.Position
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme

@Composable
fun BoardView(
    modifier: Modifier = Modifier,
    board: Board,
    onTileSelected: (at: Position) -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        repeat(BOARD_SIDE) { row ->
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(weight = 1.0f, fill = true)
            ) {
                repeat(BOARD_SIDE) { column ->
                    val at = Position(row, column)
                    TileView(
                        move = board[at],
                        modifier = Modifier.weight(weight = 1.0f, fill = true),
                        onSelected = { onTileSelected(at) },
                    )
                    if (column != BOARD_SIDE - 1) {
                        VerticalSeparator()
                    }
                }
            }
            if (row != BOARD_SIDE - 1) {
                HorizontalSeparator()
            }
        }
    }

}

@Composable
private fun HorizontalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(2.dp)
        .background(MaterialTheme.colors.background)
    )
}

@Composable
private fun VerticalSeparator() {
    Spacer(modifier = Modifier
        .fillMaxHeight()
        .width(2.dp)
        .background(MaterialTheme.colors.background)
    )
}


@Preview(showBackground = true)
@Composable
private fun EmptyBoardViewPreview() {
    BattleshipAndroidTheme {
        BoardView(
            board = Board(),
            onTileSelected = { })
    }
}
