@file:Exercise(2020, 3)
package advent2020

import common.*

// Link for the exercise: https://adventofcode.com/2020/day/3
fun main() {
    val lines = "2020/03".getLines()
    val data = lines.map { line ->
        line.map {
            when (it) {
                '.' -> false
                else -> true
            }
        }.toBooleanArray()
    }

    println(data.countTree(3, 1))
    val res = listOf(
        data.countTree(1, 1),
        data.countTree(3, 1),
        data.countTree(5, 1),
        data.countTree(7, 1),
        data.countTree(1, 2)
    )
    println(res)
    println(res.fold(1L) { acc, value -> acc * value })
}

private fun List<BooleanArray>.countTree(stepX: Int, stepY: Int): Int {
    val width = this[0].size
    var x = -stepX
    return (0 until size step stepY).count { y ->
        x += stepX
        y < size && this[y][x % width]
    }
}

