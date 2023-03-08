package pt.isel.battleshipAndroid.game

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pt.isel.battleshipAndroid.lobby.AllShips
import pt.isel.battleshipAndroid.lobby.Layout
import pt.isel.battleshipAndroid.lobby.SetFleetInputModel
import pt.isel.battleshipAndroid.ui.theme.BattleshipAndroidTheme

@Composable
fun FleetView(
    fleet: MutableList<SetFleetInputModel>,
    onClickFleetLayout: (String,String) -> Unit = { _: String, _: String -> }
) {
    fleet.map {
        val shipAndLayout = it
        val ship = shipAndLayout.shipType
        Text(text = ship)
        Row {
            Layout.values().map {
                val layout = it.name
                Button(
                    onClick = { onClickFleetLayout(ship,layout) } ,
                    enabled = layout!=shipAndLayout.shipLayout
                ) {
                    Text(text = layout)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ButtonsFleetView() {
    FleetView(fleet = AllShips().shipList.getOrNull()!!)
}