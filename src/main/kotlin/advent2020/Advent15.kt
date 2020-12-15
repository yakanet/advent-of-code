@file:Puzzle(2020, 15)

package advent2020

import common.Puzzle
import common.getText

// Link for the exercise: https://adventofcode.com/2020/day/15
fun main() {
    val input = "2020/15".getText().trim().split(",").map { it.toInt() }
    // Part 1
    println(input.findSpokenNumber(2020))

    // Part 2
    println(input.findSpokenNumber(30_000_000))
}

private fun List<Int>.findSpokenNumber(max: Int): Int {
    val turns = mapIndexed { index, value -> value to mutableListOf(index) }.toMap().toMutableMap()
    var last = last()
    (size until max).forEach { turn ->
        last = if (!turns.containsKey(last) || turns[last]!!.size <= 1) {
            0
        } else {
            val (previousLastIndex, lastIndex) = turns[last]!!.takeLast(2)
            lastIndex - previousLastIndex
        }.also {
            turns[it] = turns.computeIfAbsent(it) { mutableListOf() }.apply { add(turn) }
        }
    }
    return last
}
