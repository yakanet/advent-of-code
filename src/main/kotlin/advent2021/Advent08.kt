@file:Puzzle(2021, 8)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/8
fun main() {
    val input = "2021/08".getLines()
        .map { it.split(" | ") }
        .map { (signal, output) -> (signal.split(" ") to output.split(" ")) }

    solution1(input)
    solution2(input)
}

private fun solution1(input: List<Pair<List<String>, List<String>>>) {
    println(input.sumOf { (_, output) -> output.count { isOne(it) || isFour(it) || isSeven(it) || isEight(it) } })
}

private fun solution2(input: List<Pair<List<String>, List<String>>>) {
    input.sumOf { (signals, output) ->
        val digitMap: MutableMap<Int, String> = mutableMapOf()
        val positionMap: MutableMap<DigitPosition, Char> = mutableMapOf()
        val mapIndex = signals.groupBy { it.length }
        digitMap[1] = mapIndex[2]!!.first()
        digitMap[4] = mapIndex[4]!!.first()
        digitMap[8] = mapIndex[7]!!.first()
        digitMap[7] = mapIndex[3]!!.first()
        val zeroSixNine = mapIndex[6]!!
        val twoThreeFive = mapIndex[5]!!

        val centerTopLeftBottomLeft = zeroSixNine
            .flatMap { it.toCharArray().toList() }
            .groupBy { it }
            .filter { it.value.size == 2 }
            .keys
        positionMap[DigitPosition.TOP_RIGHT] = centerTopLeftBottomLeft.first { it in digitMap[1]!! }
        positionMap[DigitPosition.BOTTOM_RIGHT] = digitMap[1]!!.first { it != positionMap[DigitPosition.TOP_RIGHT] }
        digitMap[6] = zeroSixNine.first { !it.contains(positionMap[DigitPosition.TOP_RIGHT]!!) }
        positionMap[DigitPosition.TOP] = digitMap[7]!!.first { it !in digitMap[1]!! }
        digitMap[5] = twoThreeFive.first { it.count { c -> c in digitMap[6]!! } == 5 }
        positionMap[DigitPosition.BOTTOM_LEFT] = digitMap[6]!!.first { it !in digitMap[5]!! }
        digitMap[2] = twoThreeFive.first { it != digitMap[5]!! && positionMap[DigitPosition.BOTTOM_LEFT]!! in it }
        digitMap[3] = twoThreeFive.first { it != digitMap[5]!! && it != digitMap[2] }
        digitMap[0] = zeroSixNine.first { it != digitMap[6] && positionMap[DigitPosition.BOTTOM_LEFT]!! in it }
        digitMap[9] = zeroSixNine.first { it != digitMap[6] && it != digitMap[0]!! }

        val codex = digitMap.map { it.value.toSortedSet().toString() to it.key.toString() }.toMap()
        output.joinToString("") {
            val key = it.toSortedSet().toString()
            if(key !in codex){
                println(it)
            }
            codex[key]!!
        }.toInt()
    }.let { println(it) }
}

private enum class DigitPosition {
    TOP, TOP_LEFT, TOP_RIGHT, CENTER, BOTTOM_LEFT, BOTTOM_RIGHT, BOTTOM
}

private fun isOne(out: String) = out.length == 2
private fun isFour(out: String) = out.length == 4
private fun isSeven(out: String) = out.length == 3
private fun isEight(out: String) = out.length == 7
