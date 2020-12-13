@file:Puzzle(2020, 12)

package advent2020

import advent2020.Directions.*
import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2020/day/12
fun main() {
    val input = "2020/12".getLines()
    // Part 1
    Ship().let { ship ->
        input.forEach { ship.next(it[0], it.drop(1).toInt()) }
        println(Math.abs(ship.x) + Math.abs(ship.y))
    }

    // Part 2
    val waypoint = Waypoint()
    Ship().let { ship ->
        input.forEach { ship.next(it[0], it.drop(1).toInt(), waypoint) }
        println(Math.abs(ship.x) + Math.abs(ship.y))
    }

}

private class Ship {
    var x = 0
    var y = 0
    var facing = EAST

    fun next(action: Char, strength: Int) {
        when (action) {
            'F' -> forward(facing, strength)
            'N' -> forward(NORTH, strength)
            'S' -> forward(SOUTH, strength)
            'E' -> forward(EAST, strength)
            'W' -> forward(WEST, strength)
            'R' -> facing = turn(facing, strength)
            'L' -> facing = turn(facing, 360 - strength)
        }
    }

    fun next(action: Char, strength: Int, waypoint: Waypoint) {
        waypoint.next(action, strength)
        when (action) {
            'F' -> {
                x += waypoint.x * strength
                y += waypoint.y * strength
            }
        }
    }

    private fun turn(facing: Directions, strength: Int): Directions {
        val degree = (facing.degree + strength) % 360
        return Directions.values().first { it.degree == degree }
    }

    private fun forward(facing: Directions, strength: Int) {
        when (facing) {
            NORTH -> y += strength
            SOUTH -> y -= strength
            EAST -> x += strength
            WEST -> x -= strength
        }
    }
}

private class Waypoint {
    var x = 10
    var y = 1

    fun next(action: Char, strength: Int) {
        when (action) {
            'N' -> y += strength
            'S' -> y -= strength
            'E' -> x += strength
            'W' -> x -= strength
            'L' -> rotate(strength)
            'R' -> rotate(360-strength)
        }
    }

    fun rotate(degree: Int) {
        repeat(degree / 90) {
            val tmp = x
            x = -y
            y = tmp
        }
    }
}

private enum class Directions(val degree: Int) { NORTH(0), EAST(90), WEST(180 + 90), SOUTH(180) }
