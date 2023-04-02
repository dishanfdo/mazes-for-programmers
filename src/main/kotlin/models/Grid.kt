package models

import models.Grid.PaintMode.*
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.random.Random

open class Grid protected constructor(val rowCount: Int, val colCount: Int) {
    open val size = rowCount * colCount
    protected lateinit var grid: List<List<Cell?>>

    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): Grid = Grid(rowCount, colCount).apply { initGrid() }
    }

    fun initGrid() {
        grid = prepareGrid()
        configureCells()
    }

    private val random: Random = Random(System.currentTimeMillis())

    protected open fun prepareGrid(): List<List<Cell?>> {
        return List(rowCount) { row ->
            List(colCount) { col ->
                Cell(row, col)
            }
        }
    }

    open fun configureCells() {
        for (cell in cells) {
            val row = cell.row
            val col = cell.column

            cell.north = this[row - 1, col]
            cell.south = this[row + 1, col]
            cell.west = this[row, col - 1]
            cell.east = this[row, col + 1]
        }
    }

    val rows: Sequence<List<Cell?>>
        get() = grid.asSequence()

    val cells: Sequence<Cell>
        get() = rows.map { row -> row.asSequence() }.flatten().filterNotNull()

    open operator fun get(row: Int, col: Int): Cell? {
        if (row !in 0 until rowCount) return null
        if (col !in 0 until colCount) return null
        return grid[row][col]
    }

    open fun randomCell(): Cell {
        val row = random.nextInt(rowCount)
        val col = random.nextInt(grid[row].size)
        return this[row, col]!!
    }

    open fun contentsOf(cell: Cell): String = " "

    open fun backgroundColorOf(cell: Cell): Color? = null

    override fun toString(): String = buildString {
        append("+${"---+".repeat(colCount)}\n")
        for (row in rows) {
            var top = "|"
            var bottom = "+"

            for (possibleCell in row) {
                val cell = possibleCell ?: Cell(-1, -1)

                val body = " ${contentsOf(cell)} " // <-- that's THREE (3) spaces!
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

    protected enum class PaintMode {
        Background, Walls
    }

    open fun toImage(cellSize: Int = 10): BufferedImage {
        val imgWidth = cellSize * colCount + 1
        val imgHeight = cellSize * rowCount + 1

        val background = Color.WHITE
        val wall = Color.BLACK

        val image = BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()
        graphics.paint = background
        graphics.fillRect(0, 0, imgWidth, imgHeight)

        listOf(Background, Walls).forEach { mode ->
            for (cell in cells) {
                val x1 = cell.column * cellSize
                val y1 = cell.row * cellSize
                val x2 = x1 + cellSize
                val y2 = y1 + cellSize

                when (mode) {
                    Background -> {
                        graphics.paint = backgroundColorOf(cell) ?: background
                        graphics.fillRect(x1, y1, x2 - x1, y2 - y1)
                    }

                    Walls -> {
                        graphics.paint = wall
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
                }
            }
        }
        return image
    }

    fun deadEnds(): List<Cell> = cells.filter { it.links.size == 1 }.toList()

    fun braid(p: Double = 1.0) {
        for (cell in deadEnds().shuffled()) {
            if (cell.links.size != 1 || random.nextDouble() > p) continue

            val neighbours = cell.neighbours.filter { !it.isLinked(cell) }
            val best = neighbours.filter { it.links.size == 1 }.ifEmpty { neighbours }

            val neighbour = best.random()
            cell.link(neighbour)
        }
    }
}