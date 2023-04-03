package models

import models.Grid.PaintMode.*
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

open class WeaveGrid protected constructor(rowCount: Int, colCount: Int) : Grid(rowCount, colCount) {

    private val underCells = mutableListOf<UnderCell>()

    companion object {
        operator fun invoke(rowCount: Int, colCount: Int): WeaveGrid {
            return WeaveGrid(rowCount, colCount).apply { initGrid() }
        }
    }

    override fun prepareGrid(): List<List<Cell?>> {
        return List(rowCount) { row ->
            List(colCount) { col ->
                OverCell(row, col, this)
            }
        }
    }

    fun tunnelUnder(overCell: OverCell) {
        val underCell = UnderCell(overCell)
        underCells.add(underCell)
    }

    override val cells: Sequence<Cell>
        get() = super.cells + underCells.asSequence()

    override fun toImage(cellSize: Int, insetFraction: Double): BufferedImage {
        val modifiedInset = if (insetFraction == 0.0) 0.1 else insetFraction
        return super.toImage(cellSize, modifiedInset)
    }

    override fun toImageWithInset(
        graphics: Graphics2D,
        cell: Cell,
        mode: PaintMode,
        cellSize: Int,
        wall: Color,
        x: Int,
        y: Int,
        inset: Int,
        defaultBackground: Color
    ) {
        if (cell is OverCell) {
            super.toImageWithInset(graphics, cell, mode, cellSize, wall, x, y, inset, defaultBackground)
        } else {
            cell as UnderCell
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
                    // do nothing
                }
                Walls -> {
                    graphics.paint = wall
                    if (cell.isVerticalPassage()) {
                        graphics.drawLine(x2, y1, x2, y2)
                        graphics.drawLine(x3, y1, x3, y2)
                        graphics.drawLine(x2, y3, x2, y4)
                        graphics.drawLine(x3, y3, x3, y4)
                    } else {
                        graphics.drawLine(x1, y2, x2, y2)
                        graphics.drawLine(x1, y3, x2, y3)
                        graphics.drawLine(x3, y2, x4, y2)
                        graphics.drawLine(x3, y3, x4, y3)

                    }
                }
            }
        }
    }
}