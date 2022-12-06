@file:Puzzle(2022, 6)

package advent2022

import common.Puzzle
import common.getText

// Link for the exercise: https://adventofcode.com/2022/day/6
fun main() {
    val input = "2022/06".getText()
    println(input.indexOfMarker(4))
    println(input.indexOfMarker(14))
}

private fun String.indexOfMarker(markerSize: Int) =
    windowed(markerSize).takeWhile { it.toSet().size != markerSize }.size + markerSize
