package models

class MaskedGrid(val mask: Mask) : Grid(mask.rows, mask.columns) {

    override val size = mask.count

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