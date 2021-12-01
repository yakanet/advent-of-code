@file:Puzzle(2021, 1)

package advent2021

import common.Puzzle
import common.getLinesInt

// Link for the exercise: https://adventofcode.com/2021/day/1
fun main() {
    val input = "2021/01".getLinesInt()
    solution1(input) // 1624
    solution2(input) // 1653
}

fun solution1(input: List<Int>) {
    var increase = 0
    input.reduce { acc, it ->
        if (it > acc) {
            increase++
        }
        it
    }
    println(increase)
}


fun solution2(input: List<Int>) {
    val list = input.windowed(3).map { it.sum() }
    solution1(list)
}