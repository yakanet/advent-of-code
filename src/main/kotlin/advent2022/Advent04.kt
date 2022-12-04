@file:Puzzle(2022, 4)

package advent2022

import common.Puzzle
import common.getLines
import kotlin.math.max
import kotlin.math.min

// Link for the exercise: https://adventofcode.com/2022/day/4
fun main() {
    val lines = "2022/04".getLines()
    println(part1(lines))
    println(part2(lines))
}

private fun part1(lines: List<String>) = lines.count { line ->
    val (range1, range2) = line.toRanges()
    range1 in range2 || range2 in range1
}

private fun part2(lines: List<String>) = lines.count { line ->
    val (range1, range2) = line.toRanges()
    max(range1.first, range2.first) <= min(range1.last, range2.last)
}

private fun String.toRanges() = split(",").map {
    val (min, max) = it.split("-")
    IntRange(min.toInt(), max.toInt())
}

private operator fun IntRange.contains(range: IntRange) = first >= range.first && last <= range.last
