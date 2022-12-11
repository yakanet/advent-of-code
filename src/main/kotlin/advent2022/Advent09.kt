@file:Puzzle(2022, 9)

package advent2022

import common.Puzzle
import common.getLines
import kotlin.math.abs
import kotlin.math.sign

// Link for the exercise: https://adventofcode.com/2022/day/9
fun main() {
    val lines = "2022/09".getLines().map {
        it.split(" ")
            .let { (direction, steps) -> Motion(steps.toInt(), Direction.fromString(direction)) }
    }
    part1(Rope(mutableListOf(Knot(0, 0, "H"), Knot(0, 0, "T"))), lines)
    part2(
        Rope((listOf(Knot(0, 0, "H")) + List(9) { Knot(0, 0, (it + 1).toString()) }).toMutableList()),
        lines
    )
}

private fun part1(rope: Rope, lines: List<Motion>) {
    rope.debug?.print(rope)
    for (motion in lines) {
        rope.apply(motion)
    }
    rope.debug?.printVisited(rope)
    println(rope.visited.size - 1)
}

private fun part2(rope: Rope, lines: List<Motion>) {
    //rope.debug = Debug(-11, 25 - 11, 5 - 20, 5)
    rope.debug?.print(rope)
    for (motion in lines) {
        rope.apply(motion)
    }
    rope.debug?.printVisited(rope)
    println(rope.visited.size - 1)
}

private class Debug(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int) {
    fun printMotion(motion: Motion) {
        println("== $motion ==")
    }

    fun print(rope: Rope) {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(rope.knots.firstOrNull { it.x == x && it.y == y }?.name ?: ".")
            }
            println()
        }
        println()
    }

    fun printVisited(rope: Rope) {
        for (y in minY..maxY) {
            for (x in minX..maxX) {
                print(if (rope.visited.firstOrNull { it.x == x && it.y == y } == null) "." else "#")
            }
            println()
        }
        println()
    }
}

private class Rope(var knots: MutableList<Knot>) {
    var debug: Debug? = null
    var visited = mutableSetOf(knots.first())

    fun apply(motion: Motion) {
        debug?.printMotion(motion)
        for (step in 0 until motion.steps) {
            knots[0] = knots[0] + motion.direction
            for (i in 1 until knots.size) {
                //if (!knots[i].isAdjacent(knots[i - 1])) {
                knots[i] = knots[i].moveTo(knots[i - 1])
                //}
            }
            debug?.print(this)
            visited += knots.last()
        }
    }
}

private data class Motion(val steps: Int, val direction: Direction)

private enum class Direction {
    RIGHT,
    UP,
    LEFT,
    DOWN;

    companion object {
        fun fromString(s: String) = when (s) {
            "R" -> RIGHT
            "U" -> UP
            "L" -> LEFT
            "D" -> DOWN
            else -> TODO("Unreachable")
        }
    }
}

private data class Knot(val x: Int, val y: Int, val name: String) {

    operator fun plus(direction: Direction): Knot = when (direction) {
        Direction.RIGHT -> Knot(x + 1, y, name)
        Direction.LEFT -> Knot(x - 1, y, name)
        Direction.UP -> Knot(x, y - 1, name)
        Direction.DOWN -> Knot(x, y + 1, name)
    }

    fun moveTo(target: Knot): Knot {
        val dx = target.x - x
        val dy = target.y - y
        if (abs(dx) > 1 || abs(dy) > 1) {
            return Knot(
                x + dx.sign,
                y + dy.sign,
                name
            )
        }
        return this
    }
}
