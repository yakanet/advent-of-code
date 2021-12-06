@file:Puzzle(2021, 6)

package advent2021

import common.Puzzle
import common.getText


private fun solution1(input: List<Lanternfish>) {
    fun Lanternfish.tick(): Pair<Lanternfish, Lanternfish?> {
        if (timer == 0) {
            return Lanternfish(6) to Lanternfish(8)
        }
        return Lanternfish(timer - 1) to null
    }

    var current = input
    repeat(80) {
        val newFish = mutableListOf<Lanternfish>()
        current = current.map {
            val (fish1, fish2) = it.tick()
            fish2?.let { newFish.add(fish2) }
            fish1
        } + newFish
    }
    println(current.count())
}


private fun solution2(input: List<Lanternfish>) {
    var current: Map<Int, Long> = List(9) { i ->
        i to input.count { it.timer == i }.toLong()
    }.toMap()
    repeat(256) {
        current = current.keys.associateWith {
            when (it) {
                6 -> current[7]!! + current[0]!!
                8 -> current[0]!!
                else -> current[it + 1]!!
            }
        }
    }
    println(current.values.sum())
}


private data class Lanternfish(val timer: Int) {
    override fun toString() = timer.toString()
}

// Link for the exercise: https://adventofcode.com/2021/day/6
fun main() {
    val input = "2021/06".getText().split(",").map { Lanternfish(it.toInt()) }
    solution1(input)
    solution2(input)
}


