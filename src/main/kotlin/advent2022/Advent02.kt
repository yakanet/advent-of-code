@file:Puzzle(2022, 2)

package advent2022

import common.Puzzle
import common.getLines

private enum class Move(val opponentLetter: String, val playerLetter: String, val point: Int) {
    ROCK("A", "X", 1),
    PAPER("B", "Y", 2),
    SCISSOR("C", "Z", 3);
}

private enum class Points(val point: Int) {
    LOSE(0),
    DRAW(3),
    WIN(6);
}

private val winAgainst = mapOf(
    Move.ROCK to Move.PAPER,
    Move.PAPER to Move.SCISSOR,
    Move.SCISSOR to Move.ROCK,
)

private val part2Rule = mapOf(
    Move.ROCK to Points.LOSE,
    Move.PAPER to Points.DRAW,
    Move.SCISSOR to Points.WIN,
)

private fun play(opponentMove: Move, playerMove: Move): Points {
    if (opponentMove == playerMove) {
        return Points.DRAW
    }
    if (winAgainst[opponentMove] == playerMove) {
        return Points.WIN
    }
    return Points.LOSE
}

// Link for the exercise: https://adventofcode.com/2022/day/2
fun main() {
    val lines = "2022/02".getLines()
    part1(lines)
    part2(lines)
}

private fun part1(lines: List<String>) {
    var points = 0
    for (move in lines) {
        val (opponent, bestMove) = move.split(" ").map { it.toMove() }
        points += bestMove.point + play(opponent, bestMove).point
    }
    println(points)
}

private fun part2(lines: List<String>) {
    var points = 0
    for (move in lines) {
        val (opponent, bestMove) = move.split(" ").map { it.toMove() }
        val turnShouldEnd = part2Rule[bestMove]
        for (playerMove in Move.values()) {
            if (play(opponent, playerMove) == turnShouldEnd) {
                points += playerMove.point + play(opponent, playerMove).point
            }
        }
    }
    println(points)
}

private fun String.toMove(): Move {
    for (move in Move.values()) {
        if (move.opponentLetter == this || move.playerLetter == this)
            return move
    }
    TODO("Unknown move $this")
}
