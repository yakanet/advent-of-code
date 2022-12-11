@file:Puzzle(2022, 10)

package advent2022

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2022/day/10
fun main() {

    fun process(line: String, consumeCycle: () -> Unit): Int {
        val args = line.split(" ")
        return when (args[0]) {
            "noop" -> {
                consumeCycle()
                0
            }

            "addx" -> {
                consumeCycle()
                consumeCycle()
                args[1].toInt()
            }

            else -> TODO("Unknown instruction ${args[0]}")
        }
    }

    fun part1(lines: List<String>) {
        var register = 1
        var cycle = 1
        var result = 0

        lines.forEach { line ->
            register += process(line) {
                if (cycle in setOf(20, 60, 100, 140, 180, 220)) {
                    result += cycle * register
                }
                cycle++
            }
        }
        println(result)
    }

    fun part2(lines: List<String>) {
        var register = 1
        var cycle = 1
        val screen = List(6) { CharArray(40) { '.' } }

        lines.forEach { line ->
            register += process(line) {
                val x = (cycle - 1) % 40
                val y = (cycle - 1) / 40
                if (x in register - 1..register + 1) {
                    screen[y][x] = '#'
                }
                cycle++
            }
        }
        println(screen.joinToString("\n") { it.joinToString("") })
    }


    val lines = "2022/10".getLines()
    part1(lines)
    part2(lines)
}
