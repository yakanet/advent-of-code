@file:Puzzle(2020, 10)
package advent2020

import common.*

// Link for the exercise: https://adventofcode.com/2020/day/12
fun main() {
    val lines = "2020/10".getLinesInt(2)

    // Part 1
    lines.chainAdaptersByDiff().let { count ->
        println(count[1]!! * count[3]!!)
        println(count)
    }

    // Debug
    lines.showGraph()

    // Part 2
    val paths = lines.mapPossiblePaths()
    print(paths[lines.max()!!]!!)
}

private fun List<Int>.chainAdaptersByDiff(): Map<Int, Int> {
    val count = mutableMapOf(1 to 1, 2 to 0, 3 to 1)
    var current = min()!!
    repeat(size) {
        (1..3).forEach {
            if (contains(current + it)) {
                current += it
                count[it] = count[it]!! + 1
                return@repeat
            }
        }
    }
    return count.toMap()
}

private fun List<Int>.mapPossiblePaths(): Map<Int, Long> {
    val memoize = mutableMapOf(0 to 1L) // normal path
    sorted().forEach { line ->
        memoize[line] = (1..3).fold(0L) { acc, value ->
            acc + memoize.getOrDefault(line - value, 0)
        }
    }
    println(memoize)
    return memoize
}

private fun List<Int>.showGraph() {
    val graph = Graph<Int>()
    forEach { current ->
        listOf(1, 2, 3).forEach {
            if (contains(current + it)) {
                graph.addEdge(current, current + it)
            }
        }
    }
    graph.toGraphviz { it.toString() }
}

