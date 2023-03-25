import kotlin.random.Random

data class Grid(val rowCount: Int, val colCount: Int) {
    val size = rowCount * colCount

    private val grid: List<List<Cell>>
    private val random: Random = Random(System.currentTimeMillis())

    init {
        grid = prepareGrid()
        configureCells()
    }

    private fun prepareGrid(): List<List<Cell>> {
        return List(rowCount) { row ->
            List(colCount) { col ->
                Cell(row, col)
            }
        }
    }

    private fun configureCells() {
        for (cell in cells) {
            val row = cell.row
            val col = cell.column

            cell.north = this[row - 1, col]
            cell.south = this[row + 1, col]
            cell.west = this[row, col - 1]
            cell.east = this[row, col + 1]
        }
    }

    val rows: Sequence<List<Cell>>
        get() = grid.asSequence()

    val cells: Sequence<Cell>
        get() = rows.map { row -> row.asSequence() }.flatten()

    operator fun get(row: Int, col: Int): Cell? {
        if (row !in 0 until rowCount) return null
        if (col !in 0 until colCount) return null
        return grid[row][col]
    }

    val randomCell: Cell
        get() {
            val row = random.nextInt(rowCount)
            val col = random.nextInt(grid[row].size)
            return this[row, col]!!
        }


}