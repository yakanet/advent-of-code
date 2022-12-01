@file:Puzzle(2022, 1)

package advent2022

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2022/day/1
fun main() {
    val lines = "2022/01".getLines()
    val weights = mutableListOf<Int>()
    var current = 0
    for (line in lines) {
        if (line.isEmpty()) {
            weights.add(current)
            current = 0
        } else {
            current += line.toInt()
        }
    }
    if (current > 0) {
        weights.add(current)
    }
    solution1(weights)
    solution2(weights)
}

private fun solution1(weights: MutableList<Int>) {
    println(weights.maxOf { it })
}

private fun solution2(weights: MutableList<Int>) {
   println(weights.sorted().takeLast(3).sum())
}
