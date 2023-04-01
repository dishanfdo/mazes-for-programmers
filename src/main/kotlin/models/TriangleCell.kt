package models

import addIfNotNull
import isEven

class TriangleCell(row: Int, column: Int) : Cell(row, column) {
    val upRight = (row + column).isEven()

    override val neighbours: List<Cell>
        get() = buildList {
            addIfNotNull(west)
            addIfNotNull(east)
            if (!upRight) addIfNotNull(north)
            if (upRight) addIfNotNull(south)
        }
}