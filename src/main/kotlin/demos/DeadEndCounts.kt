package demos

import models.Grid
import algorithms.*

fun main() {
    val algorithms = listOf(BinaryTree, Sidewinder, AldousBroder, Wilsons, HuntAndKill)

    fun Algorithm.name(): String = this::class.qualifiedName?.removeSuffix(".Companion")!!

    val tries = 20
    val size = 20

    val averages = mutableMapOf<Algorithm, Int>()
    for (algorithm in algorithms) {
        println("running ${algorithm.name()}...")

        val deadEndCounts = mutableListOf<Int>()
        repeat(tries) {
            val grid = Grid(size, size)
            algorithm.on(grid)
            deadEndCounts.add(grid.deadEnds().size)
        }

        val totalDeadEnds = deadEndCounts.sum()
        averages[algorithm] = totalDeadEnds / deadEndCounts.size
    }

    val totalCells = size * size
    println()
    println("Average dead-ends per ${size}x${size} maze ($totalCells cells):")
    println()

    val sortedAlgorithms = algorithms.sortedByDescending { averages[it] }
    for (algorithm in sortedAlgorithms) {
        val percentage = averages[algorithm]!! * 100 / (size * size)
        val name = algorithm.name().padStart(14)
        val averageDeadEnds = averages[algorithm].toString().padStart(3)
        println("$name : $averageDeadEnds/$totalCells $percentage%")
    }
}