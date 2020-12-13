@file:Puzzle(2015, 3)

package advent2015

import common.Puzzle
import common.getText

// Link for the exercise: https://adventofcode.com/2015/day/3
fun main() {
    val input = "2015/03".getText()

    // Part 1
    println(input.toCharArray().deliver(1).size)

    // Part 2
    println(input.toCharArray().deliver(2).size)
}

private fun CharArray.deliver(playerCount: Int): Map<Position, Int> {
    val houses = mutableMapOf(Position(0, 0) to playerCount)
    val players = Array(playerCount) { Position(0, 0) }
    forEachIndexed { i, c ->
        players[i % playerCount] += moves[c]!!
        houses.deliverAt(players[i % playerCount])
    }
    return houses
}

private val moves = mapOf(
    '^' to Position(0, -1),
    'v' to Position(0, 1),
    '<' to Position(-1, 0),
    '>' to Position(1, 0)
)

private data class Position(val x: Int, val y: Int)

private operator fun Position.plus(p: Position) = Position(x + p.x, y + p.y)

private fun MutableMap<Position, Int>.deliverAt(position: Position) {
    this[position] = this.getOrDefault(position, 0) + 1
}
