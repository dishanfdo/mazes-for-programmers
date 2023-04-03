package models

import models.Grid.PaintMode.*
import java.awt.Color
import java.awt.Graphics2D
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

    open val cells: Sequence<Cell>
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

    open fun toImage(cellSize: Int = 10, insetFraction: Double = 0.0): BufferedImage {
        val imgWidth = cellSize * colCount + 1
        val imgHeight = cellSize * rowCount + 1
        val inset = (cellSize * insetFraction).toInt()

        val background = Color.WHITE
        val wall = Color.BLACK

        val image = BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()
        graphics.paint = background
        graphics.fillRect(0, 0, imgWidth, imgHeight)

        listOf(Background, Walls).forEach { mode ->
            for (cell in cells) {
                val x = cell.column * cellSize
                val y = cell.row * cellSize

                if (inset > 0) {
                    toImageWithInset(graphics, cell, mode, cellSize, wall, x, y, inset, background)
                } else {
                    toImageWithoutInset(graphics, cell, mode, cellSize, wall, x, y, background)
                }
            }
        }
        return image
    }

    protected open fun toImageWithInset(graphics: Graphics2D, cell: Cell, mode: PaintMode, cellSize: Int, wall: Color, x: Int, y: Int, inset: Int, defaultBackground: Color) {
        val coordinates = cellCoordinatesWithInset(x, y, cellSize, inset)
        val x1 = coordinates[0]
        val x2 = coordinates[1]
        val x3 = coordinates[2]
        val x4 = coordinates[3]
        val y1 = coordinates[4]
        val y2 = coordinates[5]
        val y3 = coordinates[6]
        val y4 = coordinates[7]

        when (mode) {
            Background -> {
                graphics.paint = backgroundColorOf(cell) ?: defaultBackground
                graphics.fillRect(x2, y2, x3 - x2, y3 - y2)
                if (cell.isLinkedToNorth()) {
                    graphics.fillRect(x2, y1, x3 - x2, y2 - y1)
                }

                if (cell.isLinkedToSouth()) {
                    graphics.fillRect(x2, y3, x3 - x2, y4 - y3)
                }

                if (cell.isLinkedToWest()) {
                    graphics.fillRect(x1, y2, x2 - x1, y3 - y2)
                }

                if (cell.isLinkedToEast()) {
                    graphics.fillRect(x3, y2, x4 - x3, y3 - y2)
                }
            }
            Walls -> {
                graphics.paint = wall
                if (cell.isLinkedToNorth()) {
                    graphics.drawLine(x2, y1, x2, y2)
                    graphics.drawLine(x3, y1, x3, y2)
                } else {
                    graphics.drawLine(x2, y2, x3, y2)
                }

                if (cell.isLinkedToSouth()) {
                    graphics.drawLine(x2, y3, x2, y4)
                    graphics.drawLine(x3, y3, x3, y4)
                } else {
                    graphics.drawLine(x2, y3, x3, y3)
                }

                if (cell.isLinkedToWest()) {
                    graphics.drawLine(x1, y2, x2, y2)
                    graphics.drawLine(x1, y3, x2, y3)
                } else {
                    graphics.drawLine(x2, y2, x2, y3)
                }

                if (cell.isLinkedToEast()) {
                    graphics.drawLine(x3, y2, x4, y2)
                    graphics.drawLine(x3, y3, x4, y3)
                } else {
                    graphics.drawLine(x3, y2, x3, y3)
                }
            }
        }
    }

    protected open fun toImageWithoutInset(graphics: Graphics2D, cell: Cell, mode: PaintMode, cellSize: Int, wall: Color, x: Int, y: Int, defaultBackground: Color) {
        val x1 = x
        val y1 = y
        val x2 = x1 + cellSize
        val y2 = y1 + cellSize

        when (mode) {
            Background -> {
                graphics.paint = backgroundColorOf(cell) ?: defaultBackground
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

    protected fun cellCoordinatesWithInset(x: Int, y: Int, cellSize: Int, inset: Int): List<Int> {
        val x1 = x
        val x4 = x + cellSize
        val x2 = x1 + inset
        val x3 = x4 - inset

        val y1 = y
        val y4 = y + cellSize
        val y2 = y1 + inset
        val y3 = y4 - inset

        return listOf(x1, x2, x3, x4, y1, y2, y3, y4)
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