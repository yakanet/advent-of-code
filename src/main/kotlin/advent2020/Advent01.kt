@file:Exercise(2020, 1)

package advent2020

import common.Exercise
import common.getLinesInt

// Link for the exercise: https://adventofcode.com/2020/day/1
fun main() {
    val input = "2020/01".getLinesInt()
    input.find2SumEqual(2020).let { (a, b) ->
        println(a * b)
    }
    input.find3SumEqual(2020).let { (a, b, c) ->
        println(a * b * c)
    }
}

private fun List<Int>.find2SumEqual(target: Int): Pair<Int, Int> {
    repeat(size) { a ->
        repeat(size) { b ->
            if (this[a] + this[b]==target) {
                return this[a] to this[b]
            }
        }
    }
    throw Exception("No result")
}


private fun List<Int>.find3SumEqual(target: Int): Triple<Int, Int, Int> {
    repeat(size) { a ->
        repeat(size) { b ->
            repeat(size) { c ->
                if (this[a] + this[b] + this[c]==target) {
                    return Triple(this[a], this[b], this[c])
                }
            }
        }
    }
    throw Exception("No result")
}
