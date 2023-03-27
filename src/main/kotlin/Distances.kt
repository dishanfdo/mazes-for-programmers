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

    fun pathTo(goal: Cell): Distances {
        val breadcrumbs = Distances(root)
        val goalDist = distances[goal] ?: error("Distance of goal cannot be null")

        var current = goal
        breadcrumbs[current] = goalDist
        while (current != root) {
            val distOfCurrent = distances[current] ?: error("Distance of $current cannot be null")
            val (next, nextDist) = current.links
                .map { cell ->
                    val dist = distances[cell] ?: error("Distance of $cell cannot be null")
                    cell to dist
                }.firstOrNull { (_, dist) -> dist < distOfCurrent } ?: error("Cannot find the path")

            breadcrumbs[next] = nextDist
            current = next
        }

        return breadcrumbs
    }
}