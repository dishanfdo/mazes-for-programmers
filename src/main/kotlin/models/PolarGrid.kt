package models

import java.awt.Color
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class PolarGrid(rowCount: Int) : Grid(rowCount, 1) {

    override fun prepareGrid(): List<List<Cell?>> {
        val rows = List(rowCount) { mutableListOf<PolarCell>() }
        val rowHeight = 1.0 / rowCount
        rows[0].add(PolarCell(0, 0))

        for (row in 1 until rowCount) {
            val radius = row.toFloat() / rowCount
            val circumference = 2 * PI * radius

            val previousCount = rows[row - 1].size
            val estimatedCellWidth = circumference / previousCount
            val ratio = (estimatedCellWidth / rowHeight).roundToInt()

            val cells = previousCount * ratio
            repeat(cells) { col -> rows[row].add(PolarCell(row, col))}
        }

        return rows
    }

    override fun configureCells(cells: List<List<Cell?>>) {
        for (cell in cells.flatten().filterNotNull().filterIsInstance<PolarCell>()) {
            val row = cell.row
            val col = cell.column
            if (row > 0) {
                val totalColumns = cells[row].size
                cell.cw = cells[row].getOrNull((col + 1).mod(totalColumns)) as PolarCell?
                cell.ccw = cells[row].getOrNull((col - 1).mod(totalColumns)) as PolarCell?

                val ratio = totalColumns / cells[row - 1].size
                val parent = cells[row - 1][col / ratio] as PolarCell
                parent.outward.add(cell)
                cell.inward = parent
            }
        }
    }

    override fun randomCell(): Cell {
        val row = (0 until rowCount).random()
        val col = (0  until grid[row].size).random()
        return grid[row][col]!!
    }

    override fun toImage(cellSize: Int): BufferedImage {
        val imgSize = 2 * rowCount * cellSize + 1

        val background = Color.WHITE
        val wall = Color.BLACK

        val image = BufferedImage(imgSize, imgSize, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        graphics.paint = background
        graphics.fillRect(0, 0, imgSize, imgSize)
        val center = imgSize / 2

        graphics.paint = wall
        for (cell in cells) {
            cell as PolarCell? ?: error("$cell is not a PolarCell")
            if (cell.row == 0) continue

            val theta = 2 * PI / grid[cell.row].size
            val innerRadius = cell.row * cellSize
            val outerRadius = innerRadius + cellSize
            val thetaCcw = cell.column * theta
            val thetaCw = thetaCcw + theta

            val ax = center + (innerRadius * cos(thetaCcw)).toInt()
            val ay = center + (innerRadius * sin(thetaCcw)).toInt()
            val bx = center + (outerRadius * cos(thetaCcw)).toInt()
            val by = center + (outerRadius * sin(thetaCcw)).toInt()

            val cx = center + (innerRadius * cos(thetaCw)).toInt()
            val cy = center + (innerRadius * sin(thetaCw)).toInt()
            val dx = center + (outerRadius * cos(thetaCw)).toInt()
            val dy = center + (outerRadius * sin(thetaCw)).toInt()

            if (!cell.isLinkedToInward()) {
                graphics.drawLine(ax, ay, cx, cy)
            }
            if (!cell.isLinkedToCw()) {
                graphics.drawLine(cx, cy, dx, dy)
            }
        }
        val size = 2 * rowCount * cellSize
        graphics.drawOval(0, 0, size, size)

        return image
    }
}