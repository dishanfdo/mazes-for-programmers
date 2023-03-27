fun main() {
    val grid = DistanceGrid(5, 5)
    BinaryTree.on(grid)

    val start = grid[0, 0] ?: error("Start cell cannot be null")
    val distances = start.distances()
    val (newStart, _) = distances.max()
    val newDistances = newStart.distances()
    val (goal, _) = newDistances.max()

    grid.distances = newDistances.pathTo(goal)
    println(grid)
}