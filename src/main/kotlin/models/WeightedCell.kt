package models

import java.util.PriorityQueue

class WeightedCell(row: Int, column: Int, var weight: Int = 1) : Cell(row, column) {

    override fun distances(): Distances {
        val weights = Distances(this)
        val pending = PriorityQueue<WeightedCell>(compareBy { it.weight })
        pending.offer(this)

        while (pending.isNotEmpty()) {
            val cell = pending.poll()
            for (linked in cell.links) {
                linked as WeightedCell
                val totalWeight = weights[cell]!! + linked.weight
                weights[linked].let {
                    if (it == null || totalWeight < it) {
                        weights[linked] = totalWeight
                        pending.offer(linked)
                    }
                }
            }
        }
        return weights
    }
}