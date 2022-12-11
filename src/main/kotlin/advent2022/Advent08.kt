@file:Puzzle(2022, 8)

package advent2022

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2022/day/8
fun main() {
    val lines = "2022/08".getLines()
    part1(lines.toForest())
    part2(lines.toForest())
}


private fun part1(forest: Forest) {
    val visible = mutableSetOf<Tree>()
    visible.addAll(forest.lineAt(forest.first))
    visible.addAll(forest.lineAt(forest.last))
    visible.addAll(forest.columnAt(forest.first))
    visible.addAll(forest.columnAt(forest.last))

    for (y in (forest.first + 1) until forest.last) {
        val range = ((forest.first + 1) until forest.last)
        var maxVisible = forest.at(0, y)
        range.forEach { x ->
            val current = forest.at(x, y)
            if (current.height > maxVisible.height) {
                visible.add(current)
                maxVisible = current
            }
        }
        maxVisible = forest.at(forest.last, y)
        range.reversed().forEach { x ->
            val current = forest.at(x, y)
            if (current.height > maxVisible.height) {
                visible.add(current)
                maxVisible = current
            }
        }
    }
    for (x in (forest.first + 1) until forest.last) {
        val range = ((forest.first + 1) until forest.last)
        var maxVisible = forest.at(x, 0)
        range.forEach { y ->
            val current = forest.at(x, y)
            if (current.height > maxVisible.height) {
                visible.add(current)
                maxVisible = current
            }
        }
        maxVisible = forest.at(x, forest.last)
        range.reversed().forEach { y ->
            val current = forest.at(x, y)
            if (current.height > maxVisible.height) {
                visible.add(current)
                maxVisible = current
            }
        }
    }
    println(visible.size)
}

private fun part2(forest: Forest) {
    val innerRange = IntRange(forest.first + 1, forest.last - 1)
    var max = 0
    for (y in innerRange) {
        for (x in innerRange) {
            val current = forest.at(x, y)
            val left = visibleUntil(current.height, IntRange(forest.first, x - 1).reversed()) { forest.at(it, y) }
            val right = visibleUntil(current.height, IntRange(x + 1, forest.last)) { forest.at(it, y) }
            val top = visibleUntil(current.height, IntRange(forest.first, y - 1).reversed()) { forest.at(x, it) }
            val bottom = visibleUntil(current.height, IntRange(y + 1, forest.last)) { forest.at(x, it) }
            val weight = left.size * right.size * top.size * bottom.size
            max = max.coerceAtLeast(weight)
        }
    }
    println(max)
}

private fun visibleUntil(targetHeight: Int, range: IntProgression, at: (i: Int) -> Tree): MutableList<Tree> {
    val treeSize = mutableListOf<Tree>()
    range.forEach {
        val current = at(it)
        if (current.height < targetHeight) {
            treeSize.add(current)
        } else {
            treeSize.add(current)
            return treeSize
        }
    }
    return treeSize
}


private fun List<String>.toForest() = Forest(mapIndexed { y, line ->
    line.mapIndexed { x, height ->
        Tree(height.toString().toInt(), x, y)
    }
})

private class Forest(private val trees: List<List<Tree>>) {
    val size = trees.size
    val first = 0
    val last = size - 1

    fun at(x: Int, y: Int): Tree {
        if (x in trees.indices && y in trees.indices) {
            return trees[y][x]
        }
        TODO("No tree at $x:$y")
    }

    fun lineAt(y: Int) = trees[y]
    fun columnAt(x: Int) = (0 until size).map { trees[it][x] }
}

private data class Tree(val height: Int, val x: Int, val y: Int)
