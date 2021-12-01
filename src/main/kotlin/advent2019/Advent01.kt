@file:Puzzle(2019, 1)

package advent2019

import common.Puzzle
import common.getLinesInt

// Link for the exercise: https://adventofcode.com/2019/day/1
fun main() {
    val input = "2019/01".getLinesInt()

    fun fuelCount(fuel: Int) = (fuel / 3) - 2
    println(input.sumOf(::fuelCount))
    println(input.sumOf {
        var fuel = fuelCount(it)
        var sum = 0
        while (fuel >= 0) {
            sum += fuel
            fuel = fuelCount(fuel)
        }
        sum
    })
}
