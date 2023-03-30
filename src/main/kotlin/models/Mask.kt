package models

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class Mask(val rows: Int, val columns: Int) {
    private val bits = MutableList(rows) { MutableList(columns) { true } }

    companion object {
        fun fromTxt(filePath: String): Mask {
            val lines = File(filePath).readLines().filter { it.isNotEmpty() }
            val rows = lines.size
            val columns = lines.first().length

            val mask = Mask(rows, columns)
            for (row in 0 until mask.rows) {
                for (col in 0 until mask.columns) {
                    mask[row, col] = if (lines[row][col] == 'X') false else true
                }
            }

            return mask
        }

        fun fromPng(filePath: String): Mask {
            /** For some reason, the image is read as rotated (at least in mac)
             * TODO: Find a way to figure out the rotation from the image itself
             */
            fun BufferedImage.isRotated(): Boolean = true

            val image = ImageIO.read(File(filePath))
            val isRotated = image.isRotated()
            val width = if (!isRotated) image.width else image.height
            val height = if (!isRotated) image.height else image.width
            val mask = Mask(width, height)
            for (row in 0 until mask.rows) {
                for (col in 0 until mask.columns) {
                    val pixel = if (!isRotated) image.getRGB(row, col) else image.getRGB(col, row)
                    mask[row, col] = pixel != Color.BLACK.rgb
                }
            }
            return mask
        }
    }

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