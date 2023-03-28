fun main() {
    repeat(1) {
        val grid = Grid(20, 20)
        BinaryTree.on(grid)
        println(grid)
        grid.toImage().saveAsPng("./mazes/binary_tree.png")
    }
}