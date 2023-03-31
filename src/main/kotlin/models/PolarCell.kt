package models

import addIfNotNull

class PolarCell(row: Int, column: Int) : Cell(row, column) {
    var cw: PolarCell? = null
    var ccw: PolarCell? = null
    var inward: PolarCell? = null
    val outward: MutableList<PolarCell> = mutableListOf()

    override val neighbours: List<Cell>
        get() = buildList {
            addIfNotNull(cw)
            addIfNotNull(ccw)
            addIfNotNull(inward)
            addAll(outward)
        }

    fun isLinkedToInward(): Boolean {
        val inward = inward
        return inward != null && isLinked(inward)
    }

    fun isLinkedToCw(): Boolean {
        val cw = cw
        return cw != null && isLinked(cw)
    }
}