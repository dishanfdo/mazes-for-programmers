data class Cell(val row: Int, val column: Int) {
    var north: Cell? = null
    var south: Cell? = null
    var east: Cell? = null
    var west: Cell? = null

    private val _links: MutableMap<Cell, Boolean> = mutableMapOf()

    fun link(cell: Cell, bidirectional: Boolean = true) {
        _links[cell] = true
        if (bidirectional) {
            cell.link(this, bidirectional = false)
        }
    }

    fun unlink(cell: Cell, bidirectional: Boolean = true) {
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

    val neighbours: List<Cell>
        get() = buildList {
            addIfNotNull(north)
            addIfNotNull(south)
            addIfNotNull(east)
            addIfNotNull(west)
        }
}