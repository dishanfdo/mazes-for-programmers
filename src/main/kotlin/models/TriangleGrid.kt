package models

import models.Grid.PaintMode.*
import java.awt.Color
import java.awt.Polygon
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import kotlin.math.sqrt

class TriangleGrid private constructor(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {
    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): TriangleGrid {
            return TriangleGrid(rowCount, colCount).apply { initGrid() }
        }
    }

    override fun prepareGrid(): List<List<Cell?>> {
        return List(rowCount) { row ->
            List(colCount) { col -> 
                TriangleCell(row, col)
            }
        }
    }

    override fun configureCells() {
        for (cell in cells) {
            cell as TriangleCell
            
            val row = cell.row
            val col = cell.column
            
            cell.west = this[row, col - 1]
            cell.east = this[row, col + 1]
            if (cell.upRight) { 
                cell.south = this[row + 1, col]
            } else {
                cell.north = this[row - 1, col]
            }
        }
    }

    override fun toImage(cellSize: Int, insetFraction: Double): BufferedImage {
        val halfWidth = cellSize / 2.0
        val height = cellSize * sqrt(3.0) / 2
        val halfHeight = height / 2.0
        
        val imgWidth = (cellSize * (colCount + 1) / 2.0).toInt()
        val imgHeight = (height * rowCount).toInt()

        val background = Color.WHITE
        val wall = Color.BLACK

        val image = BufferedImage(imgWidth + 1, imgHeight + 1, BufferedImage.TYPE_INT_ARGB)
        val graphics = image.createGraphics()
        graphics.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        )
        graphics.paint = background
        graphics.fillRect(0, 0, imgWidth + 1, imgHeight + 1)

        listOf(Background, Walls).forEach { mode ->
            for (cell in cells) {
                cell as TriangleCell

                val cx = halfWidth + cell.column * halfWidth
                val cy = halfHeight + cell.row * height

                val westX = (cx - halfWidth).toInt()
                val midX = cx.toInt()
                val eastX = (cx + halfWidth).toInt()

                val apexY = if (cell.upRight) (cy - halfHeight).toInt() else (cy + halfHeight).toInt()
                val baseY = if (cell.upRight) (cy + halfHeight).toInt() else (cy - halfHeight).toInt()

                when (mode) {
                    Background -> {
                        graphics.paint = backgroundColorOf(cell)
                        val triangle = Polygon().apply {
                            addPoint(westX, baseY)
                            addPoint(midX, apexY)
                            addPoint(eastX, baseY)
                        }
                        graphics.fillPolygon(triangle)
                    }

                    Walls -> {
                        graphics.paint = wall
                        if (cell.west == null) { graphics.drawLine(westX, baseY, midX, apexY) }
                        if (!cell.isLinkedToEast()) { graphics.drawLine(eastX, baseY, midX, apexY) }

                        val noSouth = cell.upRight && cell.south == null
                        val notLinked = !cell.upRight && !cell.isLinkedToNorth()
                        if (noSouth || notLinked) {
                            graphics.drawLine(eastX, baseY, westX, baseY)
                        }
                    }
                }
            }
        }
        return image
    }
}