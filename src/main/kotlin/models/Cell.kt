package models

import addIfNotNull
import java.util.LinkedList

open class Cell(val row: Int, val column: Int) {
    var north: Cell? = null
    var south: Cell? = null
    var east: Cell? = null
    var west: Cell? = null

    private val _links: MutableMap<Cell, Boolean> = mutableMapOf()

    open fun link(cell: Cell, bidirectional: Boolean = true) {
        _links[cell] = true
        if (bidirectional) {
            cell.link(this, bidirectional = false)
        }
    }

    open fun unlink(cell: Cell, bidirectional: Boolean = true) {
        _links.remove(cell)
        if (bidirectional) {
            cell.unlink(this, bidirectional = false)
        }
    }

    val links: List<Cell>
        get() = _links.keys.toList()

    fun isLinked(cell: Cell): Boolean = cell in _links

    fun isLinkedToNorth(): Boolean {
        val north = north
        return north != null && isLinked(north)
    }

    fun isLinkedToEast(): Boolean {
        val east = east
        return east != null && isLinked(east)
    }

    fun isLinkedToWest(): Boolean {
        val west = west
        return west != null && isLinked(west)
    }

    fun isLinkedToSouth(): Boolean {
        val south = south
        return south != null && isLinked(south)
    }

    open val neighbours: List<Cell>
        get() = buildList {
            addIfNotNull(north)
            addIfNotNull(south)
            addIfNotNull(east)
            addIfNotNull(west)
        }

    open fun distances(): Distances {
        val distances = Distances(this)
        val frontier = LinkedList<Cell>()
        frontier.offer(this)

        while (frontier.isNotEmpty()) {
            val cell = frontier.poll()
            for (linked in cell.links) {
                if (linked !in distances) {
                    distances[linked] = distances[cell]!! + 1
                    frontier.offer(linked)
                }
            }
        }

        return distances
    }
}