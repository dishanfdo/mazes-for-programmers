package models

import addIfNotNull

abstract class WeaveCell(row: Int, column: Int) : Cell(row, column) {
    abstract fun isHorizontalPassage(): Boolean
    abstract fun isVerticalPassage(): Boolean
}

class OverCell(row: Int, column: Int, val grid: WeaveGrid) : WeaveCell(row, column) {

    override val neighbours: List<Cell>
        get() = buildList {
            addAll(super.neighbours)
            if (canTunnelNorth()) addIfNotNull(north?.north)
            if (canTunnelSouth()) addIfNotNull(south?.south)
            if (canTunnelEast()) addIfNotNull(east?.east)
            if (canTunnelWest()) addIfNotNull(west?.west)
        }

    override fun isHorizontalPassage(): Boolean =
        isLinkedToEast() && isLinkedToWest() && !isLinkedToNorth() && !isLinkedToSouth()

    override fun isVerticalPassage(): Boolean =
        isLinkedToNorth() && isLinkedToSouth() && !isLinkedToEast() && !isLinkedToWest()

    private fun canTunnelNorth(): Boolean {
        val north = north as WeaveCell?
        val northOfNorth = north?.north as WeaveCell?
        return north != null && northOfNorth != null && north.isHorizontalPassage()
    }

    private fun canTunnelSouth(): Boolean {
        val south = south as WeaveCell?
        val southOfSouth = south?.south as WeaveCell?
        return south != null && southOfSouth != null && south.isHorizontalPassage()
    }

    private fun canTunnelEast(): Boolean {
        val east = east as WeaveCell?
        val eastOfEast = east?.east as WeaveCell?
        return east != null && eastOfEast != null && east.isVerticalPassage()
    }

    private fun canTunnelWest(): Boolean {
        val west = west as WeaveCell?
        val westOfWest = west?.west as WeaveCell?
        return west != null && westOfWest != null && west.isVerticalPassage()
    }

    override fun link(cell: Cell, bidirectional: Boolean) {
        var neighbour: OverCell? = null
        if (north != null && north == cell.south) {
            neighbour = north as OverCell
        } else if (south != null && south == cell.north) {
            neighbour = south as OverCell
        } else if (east != null && east == cell.west) {
            neighbour = east as OverCell
        } else if (west != null && west == cell.east) {
            neighbour = west as OverCell
        }

        if(neighbour != null) {
            grid.tunnelUnder(neighbour)
        } else {
            super.link(cell, bidirectional)
        }
    }
}

class UnderCell(overCell: OverCell) : WeaveCell(overCell.row, overCell.column) {
    init {
        if (overCell.isHorizontalPassage()) {
            this.north = overCell.north
            overCell.north?.south = this
            this.south = overCell.south
            overCell.south?.north = this

            val north = north
            if (north != null) link(north)
            val south = south
            if (south != null) link(south)
        } else {
            this.east = overCell.east
            overCell.east?.west = this
            this.west = overCell.west
            overCell.west?.east = this

            val east = east
            if (east != null) link(east)
            val west = west
            if (west != null) link(west)
        }
    }

    override fun isHorizontalPassage(): Boolean = east != null || west != null

    override fun isVerticalPassage(): Boolean = north != null || south != null
}
