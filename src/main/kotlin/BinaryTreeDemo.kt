fun main() {
    repeat(1) {
        val grid = Grid(32, 32)
        BinaryTree.on(grid)
        println(grid)
        grid.toImage().saveAsPng("./mazes/binary_tree.png")
    }
}