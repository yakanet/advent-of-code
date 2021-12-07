@file:Puzzle(2021, 7)

package advent2021

import common.Puzzle
import common.getText
import kotlin.math.abs

// Link for the exercise: https://adventofcode.com/2021/day/7
fun main() {
    val input = "2021/07".getText().split(",").map { it.toInt() }.sorted()
    solution1(input) //339321
    solution2(input) //95476244
}

private fun solution1(input: List<Int>) {
    (input.first()..input.last()).minOfOrNull { target ->
        input.sumOf { distance(it, target) }
    }.let { println(it) }
}

private fun solution2(input: List<Int>) {
    (input.first()..input.last()).minOfOrNull { target ->
        input.sumOf { cost(distance(it, target)) }
    }.let { println(it) }
}

private fun distance(a: Int, b: Int) = abs(a - b)
private fun cost(to: Int) = (1..to).sum()