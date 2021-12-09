@file:Puzzle(2021, 9)

package advent2021

import common.Puzzle
import common.getLines
import java.util.*

private data class Point2D(val x: Int, val y: Int)

// Link for the exercise: https://adventofcode.com/2021/day/9
fun main() {
    val lines = "2021/09".getLines()
    val input = lines.flatMapIndexed { y, line ->
        line.mapIndexed { x, num ->
            Point2D(x, y) to num.toString().toInt()
        }
    }.toMap()
    val (x, y) = lines[0].length to lines.size
    solution1(input, x, y)
    solution2(input, x, y)
}

private fun Point2D.adjacents() = listOf(
    Point2D(x, y - 1),
    Point2D(x, y + 1),
    Point2D(x - 1, y),
    Point2D(x + 1, y)
)

private fun solution1(input: Map<Point2D, Int>, maxX: Int, maxY: Int) {
    fun Point2D.value(): Int = if (this in input) input[this]!! else 9
    buildList {
        (0 until maxY).forEach { y ->
            (0 until maxX).forEach { x ->
                val current = Point2D(x, y)
                val value = current.value()
                if (current.adjacents().all { it.value() > value }) {
                    add(value + 1)
                }
            }
        }
    }.let { println(it.sum()) }
}


private fun solution2(input: Map<Point2D, Int>, maxX: Int, maxY: Int) {
    val marked = mutableSetOf<Point2D>()
    fun Point2D.value() = if (this in input && this !in marked) input[this]!! else 9

    buildList {
        for (y in 0 until maxY) {
            (0 until maxX).forEach { x ->
                val root = Point2D(x, y)

                // is lowest
                if (root.value() == 9 || root.adjacents().any { it.value() < root.value() }) return@forEach

                // discover basin
                val stack = LinkedList<Point2D>().apply { add(root) }
                val result = mutableSetOf(root)
                while (stack.isNotEmpty()) {
                    val current = stack.pop()
                    val v0 = current.value()
                    current.adjacents().forEach { point ->
                        val v1 = point.value()
                        if (v1 != 9 && v1 > v0) {
                            stack.push(point)
                            result.add(point)
                        }
                    }
                }
                marked.addAll(result)
                if (result.size > 1) {
                    this.add(result.size)
                }
            }
        }
    }.let {
        println(it.sorted().takeLast(3).reduce { a, b -> a * b })
    }
}


