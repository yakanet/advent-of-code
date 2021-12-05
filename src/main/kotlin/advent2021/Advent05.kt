@file:Puzzle(2021, 5)

package advent2021

import common.Puzzle
import common.getLines
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min


private data class Point(val x: Int, val y: Int)
private data class Range(val from: Point, val to: Point)

private fun solution1(input: List<Range>) {
    val map = input.flatMap { (from, to) -> from.rangeTo1D(to) }
        .groupingBy { it }
        .eachCount()
    //map.print()
    println(map.count { it.value > 1 })
}

private fun solution2(input: List<Range>) {
    val map = input.flatMap { (from, to) -> from.rangeTo1D(to) + from.rangeTo2D(to) }
        .groupingBy { it }
        .eachCount()
    //map.print()
    println(map.count { it.value > 1 })
}

private fun Point.rangeTo1D(p: Point): List<Point> {
    return if (x == p.x) {
        (min(y, p.y)..max(y, p.y)).map { Point(x, it) }
    } else if (y == p.y) {
        (min(x, p.x)..max(x, p.x)).map { Point(it, y) }
    } else {
        emptyList()
    }
}

private fun Point.rangeTo2D(p: Point): List<Point> {
    return if (abs(x - p.x) == abs(y - p.y)) {
        val (start, end) = if (x < p.x) this to p else p to this
        val step = if (end.y > start.y) 1 else -1
        buildList {
            (start.x..end.x).forEachIndexed { i, x ->
                this.add(Point(x, start.y + (step * i)))
            }
        }
    } else {
        emptyList()
    }
}

private fun Map<Point, Int>.print() {
    val maxX = keys.maxOf { it.x }
    val maxY = keys.maxOf { it.y }
    (0..maxY).forEach { y ->
        println((0..maxX).joinToString("") { x ->
            getOrDefault(Point(x, y), ".").toString()
        })
    }
}

private fun String.toRange() = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)")
    .matchEntire(this)!!
    .groupValues
    .let {
        Range(
            Point(it[1].toInt(), it[2].toInt()),
            Point(it[3].toInt(), it[4].toInt())
        )
    }

// Link for the exercise: https://adventofcode.com/2021/day/5
fun main() {
    val input = "2021/05".getLines(1).map { it.toRange() }
    solution1(input)
    solution2(input)
}