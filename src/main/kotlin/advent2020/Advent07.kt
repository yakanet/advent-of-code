@file:Exercise(2020, 7)
package advent2020

import common.*
import java.util.LinkedList

// Link for the exercise: https://adventofcode.com/2020/day/7
fun main() {
    data class Bag(val name: String, val quantity: Int)

    val lines = "2020/07".getLines()
    val reversedIndex = mutableMapOf<String, MutableSet<String>>()
    val index = mutableMapOf<String, MutableList<Bag>>()
    lines.forEach { line ->
        val (source, destinations) = line.split(" contain ")
        val bagSource = source.substring(0, source.length - 5)
        val q = if (destinations=="no other bags.") emptyList()
        else destinations
            .split(",")
            .map { Regex("([0-9]+) ([a-z ]+) bags?").find(it)!! }
            .map { Bag(it.groups[2]!!.value, it.groups[1]!!.value.toInt()) }
        q.forEach {
            index.putIfAbsent(bagSource, mutableListOf())
            index[bagSource]!!.add(it)
        }
        q.forEach {
            reversedIndex.putIfAbsent(it.name, mutableSetOf())
            reversedIndex[it.name]!!.add(bagSource)
        }
    }
    // Part 1
    val result = mutableSetOf<String>()
    val queue = LinkedList<String>();
    queue.add("shiny gold")
    while (queue.isNotEmpty()) {
        val current = queue.pop()
        reversedIndex[current]?.forEach {
            if (result.add(it)) {
                queue.push(it)
            }
        }
    }
    println(result.size)

    // Part 2
    fun countBags(name: String): Int {
        if (!index.containsKey(name)) return 1
        return 1 + index[name]!!.map { countBags(it.name) * it.quantity }.sum()
    }
    println(countBags("shiny gold") - 1)
}
