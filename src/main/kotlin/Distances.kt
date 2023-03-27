class Distances(val root: Cell) {
    private val distances = mutableMapOf<Cell, Int>()

    init {
        distances[root] = 0
    }

    operator fun get(cell: Cell): Int? = distances[cell]

    operator fun set(cell: Cell, distance: Int) {
        distances[cell] = distance
    }

    operator fun contains(cell: Cell): Boolean = cell in distances

    val cells: List<Cell>
        get() = distances.keys.toList()
}