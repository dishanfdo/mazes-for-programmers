package models

class MaskedGrid private constructor(val mask: Mask) : Grid(mask.rows, mask.columns) {

    override val size = mask.count

    companion object {
        operator fun invoke(mask: Mask): MaskedGrid = MaskedGrid(mask).apply { initGrid() }
    }

    override fun prepareGrid(): List<List<Cell?>> {
        return List(rowCount) { row ->
            List(colCount) { col ->
                if (mask[row, col]) Cell(row, col) else null
            }
        }
    }

    override fun randomCell(): Cell {
        val (row, col) = mask.randomLocation()
        return this[row, col] ?: error("Cell at ($row x $col) cannot be null")
    }
}