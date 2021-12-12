@file:Puzzle(2021, 12)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/12
fun main() {
    val cave = Cave().apply {
        "2021/12".getLines()
            .map { it.split('-') }
            .forEach { (a, b) -> addPath(a, b) }
    }
    solution1(cave)
    solution2(cave)
}

private fun solution1(cave: Cave) {
    var pathCount = 0
    fun Cave.countPaths(
        a: Cave.Node,
        b: Cave.Node,
        currentVisited: Set<Cave.Node> = setOf(cave.startNode)
    ): Int {
        val visited = if (a.isSmall()) currentVisited + a else currentVisited
        if (a == b) {
            pathCount++
        } else {
            for (n in neighbour(a)) {
                if (n !in visited) {
                    countPaths(n, b, visited)
                }
            }
        }
        return pathCount
    }
    println(cave.countPaths(cave.startNode, cave.endNode))
}

private fun solution2(cave: Cave) {
    var pathCount = 0
    fun Cave.countPaths(
        a: Cave.Node,
        b: Cave.Node,
        currentVisited: Set<Cave.Node> = setOf(),
        canVisitTwice: Boolean = true
    ): Int {
        val visited = if (a.isSmall()) currentVisited + a else currentVisited
        if (a == b) {
            pathCount++
        } else {
            for (n in neighbour(a)) {
                if (n.isStart()) continue
                val alreadyVisited = n in visited
                if (n.isBig() || !alreadyVisited || canVisitTwice) {
                    countPaths(n, b, visited, canVisitTwice && !alreadyVisited)
                }
            }
        }
        return pathCount
    }

    println(cave.countPaths(cave.startNode, cave.endNode))

}


private class Cave {
    val startNode = Node("start")
    val endNode = Node("end")
    val paths = mutableMapOf<Node, MutableSet<Node>>()

    fun addPath(a: String, b: String) {
        addPath(Node(a), Node(b))
    }

    fun addPath(a: Node, b: Node) {
        paths.computeIfAbsent(a) { mutableSetOf() }.add(b)
        paths.computeIfAbsent(b) { mutableSetOf() }.add(a)
    }

    data class Node(val name: String) {
        fun isBig() = name == name.uppercase()
        fun isSmall() = !isBig() && !isStart()
        fun isStart() = name == "start"
    }

    fun neighbour(node: Node) = paths[node]!!
}

