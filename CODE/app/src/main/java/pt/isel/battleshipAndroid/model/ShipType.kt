package pt.isel.battleshipAndroid.model

enum class ShipType(val size: Int, name: String) {
    CARRIER(5, "CARRIER"),
    BATTLESHIP(4, "BATTLESHIP"),
    SUBMARINE(3, "SUBMARINE"),
    CRUISER(3, "CRUISER"),
    DESTROYER(2, "DESTROYER")
}

fun getSizeByName(name: String): Int {
    return ShipType.values().find { it.name == name }!!.size
}