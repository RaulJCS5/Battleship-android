package pt.isel.battleshipAndroid.game

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.game.model.PositionStateBoard
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme

@Composable
fun TileView(
    move: PositionStateBoard?,
    onSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (move != null) {
        val marker = getTile(move)
        Box(modifier = modifier
            .background(marker)
            .fillMaxSize(1.0f)
            .padding(4.dp)
            .clickable(enabled = !move.wasShoot) { onSelected() }
        ) {
        }
    }
}

fun getTile(move: PositionStateBoard): Color {
    if (move.wasShoot){
        if (move.wasShip==true){
            return Color.Red
        }
        else{
            return Color.White
        }
    }else{
        if (move.wasShip==true){
            return Color.Gray
        }
        else{
            return Color.Blue
        }
    }
}



@Preview(showBackground = true)
@Composable
private fun TileViewEmptyPreview() {
    BattleshipAndroidTheme {
        TileView(move = null, onSelected = { })
    }
}
