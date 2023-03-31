package models

import addIfNotNull

class HexCell(row: Int, column: Int) : Cell(row, column) {
    var northEast: HexCell? = null
    var northWest: HexCell? = null
    var southEast: HexCell? = null
    var southWest: HexCell? = null

    override val neighbours: List<Cell>
        get() = buildList {
            addIfNotNull(northWest)
            addIfNotNull(north)
            addIfNotNull(northEast)
            addIfNotNull(southWest)
            addIfNotNull(south)
            addIfNotNull(southEast)
        }

    fun isLinkedToNorthEast(): Boolean {
        val northEast = northEast
        return northEast != null && isLinked(northEast)
    }

    fun isLinkedToSouthEast(): Boolean {
        val southEast = southEast
        return southEast != null && isLinked(southEast)
    }
}