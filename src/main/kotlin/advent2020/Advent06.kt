@file:Exercise(2020, 6)
package advent2020

import common.*

// Link for the exercise: https://adventofcode.com/2020/day/6
fun main() {
    val lines = "2020/06".getLines()
    var group = mutableListOf<Set<Char>>()
    val list = mutableListOf<List<Set<Char>>>()
    lines.forEach { line ->
        if (line.isEmpty()) {
            list.add(group)
            group = mutableListOf()
        } else {
            group.add(line.toCharArray().toSet())
        }
    }
    list.add(group)
    val y = list.map { g ->
        val x = mutableMapOf<Char, Int>()
        g.forEach { lc ->
            lc.forEach { c ->
                x.putIfAbsent(c, 0)
                x[c] = x[c]!! + 1
            }
        }
        x.count { it.value == g.size }
    }
    println(y.sum())
}
