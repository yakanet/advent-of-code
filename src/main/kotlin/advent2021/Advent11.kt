@file:Puzzle(2021, 11)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/11
fun main() {
    solution1(load())
    solution2(load())
}

private fun load() = "2021/11".getLines().flatMap { line ->
    line.map { Octopus(it.digitToInt()) }
}.apply {
    forEachIndexed { i, octopus -> octopus.findAdjacent(i, this) }
}

private fun solution1(octopuses: List<Octopus>) {
    repeat(100) { turn ->
        octopuses.forEach { octopus -> octopus.tick(turn) }
    }
    println(octopuses.sumOf { it.flashed.size })
}

private fun solution2(octopuses: List<Octopus>) {
    var turn = 0
    while (octopuses.any { it.energy != 0 }) {
        octopuses.forEach { octopus -> octopus.tick(turn) }
        turn++
    }
    println(turn)
}


private class Octopus(var energy: Int) {
    private lateinit var adjacents: List<Octopus>
    val flashed = mutableSetOf<Int>()

    fun tick(turn: Int) {
        if (turn in flashed) {
            return
        }
        energy += 1
        if (energy == 10) {
            energy = 0
            flashed.add(turn)
            adjacents.forEach { it.tick(turn) }
        }
    }

    fun findAdjacent(position: Int, octopuses: List<Octopus>) {
        val (x0, y0) = position.toCartesian()
        adjacents = (x0 to y0).square()
            .filter { (x, y) -> (x in 0..9 && y in 0..9) }
            .map { (x, y) -> octopuses[Pair(x, y).toPosition()] }
    }
}

private fun Pair<Int, Int>.square() = let { (x, y) ->
    listOf(Pair(x - 1, y), Pair(x + 1, y)) +
            (-1..1).map { Pair(x + it, y - 1) } +
            (-1..1).map { Pair(x + it, y + 1) }
}

private fun Int.toCartesian() = this % 10 to this / 10
private fun Pair<Int, Int>.toPosition() = first + second * 10