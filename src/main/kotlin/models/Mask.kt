package models

class Mask(val rows: Int, val columns: Int) {
    private val bits = MutableList(rows) { MutableList(columns) { true } }

    operator fun get(row: Int, column: Int): Boolean {
        return if (row in 0 until rows && column in 0 until columns) {
            bits[row][column]
        } else {
            false
        }
    }

    operator fun set(row: Int, column: Int, isOn: Boolean) {
        bits[row][column] = isOn
    }

    val count: Int
        get() = bits.flatten().count { isOn -> isOn }

    fun randomLocation(): Pair<Int, Int> {
        while (true) {
            val row = (0 until rows).random()
            val col = (0 until columns).random()
            if (bits[row][col]) {
                return row to col
            }
        }
    }
}