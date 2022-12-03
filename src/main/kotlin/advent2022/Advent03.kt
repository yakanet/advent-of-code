@file:Puzzle(2022, 3)

package advent2022

import common.Puzzle
import common.getLines


// Link for the exercise: https://adventofcode.com/2022/day/3
fun main() {
    val lines = "2022/03".getLines()
    println(part1(lines))
    println(part2(lines))
}


private fun part1(lines: List<String>): Int {
    return lines.sumOf { line ->
        val (item1, item2) = line.itemsFromCompartment()
        priority(item1.find { it in item2 }!!)
    }
}

private fun part2(lines: List<String>): Int {
    return lines.chunked(3).sumOf { group ->
        val c = group[0].find { it in group[1] && it in group[2] }!!
        priority(c)
    }
}

fun priority(c: Char) = if (c in CharRange('a', 'z')) {
    (c.code - 'a'.code) + 1
} else {
    (c.code - 'A'.code) + 27
}

private fun String.itemsFromCompartment() = listOf(
    this.substring(0, this.length / 2),
    this.substring(this.length / 2)
)
