package models

import isEven
import models.Grid.PaintMode.*
import java.awt.Color
import java.awt.Polygon
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import kotlin.math.sqrt

class HexGrid private constructor(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {
    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): HexGrid {
            return HexGrid(rowCount, colCount).apply { initGrid() }
        }
    }

    override fun prepareGrid(): List<List<Cell?>> {
        return List(rowCount) { row ->
            List(colCount) { col ->
                HexCell(row, col)
            }
        }
    }

    override fun configureCells() {
        for (cell in cells) {
            cell as HexCell
            val row = cell.row
            val col = cell.column

            val northDiagonal = if (col.isEven()) row - 1 else row
            val southDiagonal = if (col.isEven()) row else row + 1

            cell.northWest = this[northDiagonal, col - 1] as HexCell?
            cell.north = this[row - 1, col]
            cell.northEast = this[northDiagonal, col + 1] as HexCell?
            cell.southWest = this[southDiagonal, col - 1] as HexCell?
            cell.south = this[row + 1, col]
            cell.southEast = this[southDiagonal, col + 1] as HexCell?
        }
    }

    override fun toImage(cellSize: Int, insetFraction: Double): BufferedImage {
        val a = cellSize / 2.0
        val b = cellSize * sqrt(3.0) / 2.0
        val height = b * 2

        val imgWidth = (3 * a * colCount + a + 0.5).toInt()
        val imgHeight = (height * rowCount + b + 0.5).toInt()

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
                cell as HexCell

                val cx = cellSize + 3 * a * cell.column
                val cy = cell.row * height + if (cell.column.isEven()) b else 2 * b

                // f/n = far/near
                // n/s/e/w = north/south/east/west
                val xfw = (cx - cellSize).toInt()
                val xnw = (cx - a).toInt()
                val xne = (cx + a).toInt()
                val xfe = (cx + cellSize).toInt()

                // m=middle
                val yn = (cy - b).toInt()
                val ym = cy.toInt()
                val ys = (cy + b).toInt()

                when (mode) {
                    Background -> {
                        graphics.paint = backgroundColorOf(cell)
                        val hexagon = Polygon().apply {
                            addPoint(xfw, ym)
                            addPoint(xnw, yn)
                            addPoint(xne, yn)
                            addPoint(xfe, ym)
                            addPoint(xne, ys)
                            addPoint(xnw, ys)
                        }
                        graphics.fillPolygon(hexagon)
                    }

                    Walls -> {
                        graphics.paint = wall
                        if (cell.southWest == null) { graphics.drawLine(xfw, ym, xnw, ys) }
                        if (cell.northWest == null) { graphics.drawLine(xfw, ym, xnw, yn) }
                        if (cell.north == null) { graphics.drawLine(xnw, yn, xne, yn) }

                        if (!cell.isLinkedToNorthEast()) { graphics.drawLine(xne, yn, xfe, ym) }
                        if (!cell.isLinkedToSouthEast()) { graphics.drawLine(xne, ys, xfe, ym) }
                        if (!cell.isLinkedToSouth()) { graphics.drawLine(xnw, ys, xne, ys) }
                    }
                }
            }
        }
        return image
    }

}