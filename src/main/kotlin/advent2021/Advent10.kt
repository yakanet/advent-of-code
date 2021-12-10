@file:Puzzle(2021, 10)

package advent2021

import common.Puzzle
import common.getLines
import java.util.*

private sealed interface ParseError
private class Corrupted(val expected: Char, val found: Char, val position: Int, val lineNumber: Int) : ParseError
private class Incomplete(val missings: List<Char>, val lineNumber: Int) : ParseError

private class Parser(private val lineNumber: Int) {
    fun parse(line: String): ParseError {
        val stack = LinkedList<Char>()
        line.forEachIndexed { i, c ->
            if (c in OPENING) {
                stack.addLast(OPENING[c]!!)
            } else {
                val expected = stack.removeLast()
                if (expected != c) {
                    return Corrupted(expected, c, i, lineNumber)
                }
            }
        }
        return Incomplete(stack.map { it }, lineNumber)
    }

    companion object {
        private val OPENING = mapOf(
            '(' to ')',
            '[' to ']',
            '{' to '}',
            '<' to '>',
        )
    }
}

// Link for the exercise: https://adventofcode.com/2021/day/10
fun main() {
    val input = "2021/10".getLines()
    solution1(input)
    solution2(input)
}

private fun solution1(input: List<String>) {
    fun points(c: Char) = when (c) {
        ')' -> 3
        ']' -> 57
        '}' -> 1197
        '>' -> 25137
        else -> throw RuntimeException("Invalid char $c")
    }
    input.mapIndexed { lineNumber, line -> Parser(lineNumber).parse(line) }
        .filterIsInstance(Corrupted::class.java)
        .sumOf { points(it.found) }
        .let { println(it) }
}


private fun solution2(input: List<String>) {
    fun points(c: Char) = when (c) {
        ')' -> 1
        ']' -> 2
        '}' -> 3
        '>' -> 4
        else -> throw RuntimeException("Invalid char $c")
    }
    input.mapIndexed { lineNumber, line -> Parser(lineNumber).parse(line) }
        .filterIsInstance(Incomplete::class.java)
        .map { it.missings.fold(0L) { acc, c -> (5 * acc) + points(c) } }
        .sorted()
        .let { println(it[it.size / 2]) }
}
