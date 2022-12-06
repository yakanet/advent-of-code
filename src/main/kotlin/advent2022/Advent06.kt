@file:Puzzle(2022, 6)

package advent2022

import common.Puzzle
import common.getText

// Link for the exercise: https://adventofcode.com/2022/day/6
fun main() {
    val input = "2022/06".getText()
    println(input.startOfMarker(4))
    println(input.startOfMarker(14))
}

private fun String.startOfMarker(markerSize: Int) =
    windowed(markerSize).takeWhile { it.countDifferentChars() != markerSize }.size + markerSize

private fun String.countDifferentChars() = toCharArray().toSet().size
