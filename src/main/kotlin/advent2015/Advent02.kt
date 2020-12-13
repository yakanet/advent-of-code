@file:Puzzle(2015, 2)

package advent2015

import common.Puzzle
import common.getLines


// Link for the exercise: https://adventofcode.com/2015/day/2
fun main() {
    val input = "2015/02".getLines().map { it.toPresent() }
    // Part 1
    println(input.fold(0) { acc, present -> acc + present.requiredPaper() })

    // Part 2
    println(input.fold(0) { acc, present -> acc + present.requiredRibbon() })
}

private data class Present(val l: Int, val w: Int, val h: Int)

private fun String.toPresent() =
    split("x").let { (l, w, h) -> Present(l.toInt(), w.toInt(), h.toInt()) }

private fun Present.requiredPaper() =
    2 * l * w + 2 * w * h + 2 * h * l + listOf(l * w, w * h, h * l).minOrNull()!!

private fun Present.requiredRibbon() = listOf(l, w, h).sorted().take(2).let { (min1, min2) ->
    2 * min1 + 2 * min2 + l * w * h
}
