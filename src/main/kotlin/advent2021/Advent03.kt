@file:Puzzle(2021, 3)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/3
fun main() {
    val input = "2021/03".getLines()
    solution1(input) // 3901196
    solution2(input)
}

private fun solution1(input: List<String>) {
    val epsilon = (0 until input[0].length).map { at ->
        val (zeros, ones) = input.partition { it[at] == '0' }
        if (zeros.size > ones.size) '1' else '0'
    }.joinToString("")
    val gamma = epsilon.map { '1' - it.digitToInt() }.joinToString("")
    println(epsilon.toInt(2) * gamma.toInt(2))
}

private fun solution2(input: List<String>) {
    val oxygen = findByPredicate(input) { zeros, ones -> zeros.size > ones.size }
    val co2 = findByPredicate(input) { zeros, ones -> zeros.size <= ones.size }
    println(oxygen.toInt(2) * co2.toInt(2))
}

private fun findByPredicate(
    input: List<String>,
    predicate: (zeros: List<String>, ones: List<String>) -> Boolean
): String {
    var current = input
    var at = 0
    while (current.size != 1) {
        val (zeros, ones) = current.partition { it[at] == '0' }
        current = if (predicate(zeros, ones)) zeros else ones
        at++
    }
    return current.first()
}
