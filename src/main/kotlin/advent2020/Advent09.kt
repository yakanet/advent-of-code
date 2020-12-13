@file:Puzzle(2020, 9)
package advent2020

import common.*

// Link for the exercise: https://adventofcode.com/2020/day/9
fun main() {
    val lines = "2020/09".getLinesLong()

    // Part 1
    val invalidNumber = lines.findInvalidSumWindowed(25)
    println(invalidNumber)

    // Part 2
    val range = lines.findSumRange(invalidNumber)
    lines.subList(range.first, range.second).sorted().let {
        println(it.first() + it.last())
    }
}

private fun List<Long>.findSumRange(target: Long): Pair<Int, Int> {
    repeat(size) { i ->
        repeat(size) { j ->
            if (subList(kotlin.math.min(i, j), kotlin.math.max(i, j)).sum()==target) {
                return i to j
            }
        }
    }
    return -1 to -1
}

private fun List<Long>.findInvalidSumWindowed(windowSize: Int): Long {
    drop(windowSize).fold(take(windowSize)) { sources, checksum ->
        val sums = sources.computeSum()
        if (!sums.containsKey(checksum)) {
            return checksum
        }
        sources.drop(1) + checksum
    }
    return -1L
}

private fun List<Long>.computeSum(): MutableMap<Long, Pair<Long, Long>> {
    val sums = mutableMapOf<Long, Pair<Long, Long>>()
    forEach { x ->
        forEach { y ->
            sums[x + y] = Pair(x, y)
        }
    }
    return sums
}
