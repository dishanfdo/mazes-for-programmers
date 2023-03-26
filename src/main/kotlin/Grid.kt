import java.awt.Color
import java.awt.image.BufferedImage
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

    override fun toString(): String = buildString {
        append("+${"---+".repeat(colCount)}\n")
        for (row in rows) {
            var top = "|"
            var bottom = "+"

            for (cell in row) {
                val body = "   " // <-- that's THREE (3) spaces!
                val east = cell.east
                val eastBoundary = if (east != null && cell.isLinked(east)) " " else "|"
                top += body + eastBoundary

                val south = cell.south
                // three spaces below, too >>------------------------------->> >...<
                val southBoundary = if (south != null && cell.isLinked(south)) "   " else "---"
                val corner = "+"
                bottom += southBoundary + corner
            }

            append("$top\n")
            append("$bottom\n")
        }
    }

    fun toImage(cellSize: Int = 10): BufferedImage {
        val imgWidth = cellSize * colCount + 1
        val imgHeight = cellSize * rowCount + 1

        val background = Color.WHITE
        val wall = Color.BLACK

        val image = BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()
        graphics.paint = background
        graphics.fillRect(0, 0, imgWidth, imgHeight)

        graphics.paint = wall
        for (cell in cells) {
            val x1 = cell.column * cellSize
            val y1 = cell.row * cellSize
            val x2 = x1 + cellSize
            val y2 = y1 + cellSize

            if (cell.north == null) {
                graphics.drawLine(x1, y1, x2, y1)
            }
            if (cell.west == null) {
                graphics.drawLine(x1, y1, x1, y2)
            }
            if (!cell.isLinkedToEast()) {
                graphics.drawLine(x2, y1, x2, y2)
            }
            if (!cell.isLinkedToSouth()) {
                graphics.drawLine(x1, y2, x2, y2)
            }
        }

        return image
    }

}