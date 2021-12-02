@file:Puzzle(2021, 2)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/2
fun main() {
    val input = "2021/02".getLines().toInstruction()
    solution1(input)
    solution2(input)
}

private fun solution1(input: List<Instruction>) {
    input.fold(Position()) { position, (operation, unit) ->
        when (operation) {
            Operation.DOWN -> position.copy(depth = position.depth + unit)
            Operation.UP -> position.copy(depth = position.depth - unit)
            Operation.FORWARD -> position.copy(horizontal = position.horizontal + unit)
        }
    }.apply {
        println(depth * horizontal)
    }
}

private fun solution2(input: List<Instruction>) {
    input.fold(Position()) { position, (operation, unit) ->
        when (operation) {
            Operation.DOWN -> position.copy(aim = position.aim + unit)
            Operation.UP -> position.copy(aim = position.aim - unit)
            Operation.FORWARD -> position.copy(
                horizontal = position.horizontal + unit,
                depth = position.depth + position.aim * unit
            )
        }
    }.apply {
        println(depth * horizontal)
    }
}

private enum class Operation { UP, DOWN, FORWARD }
private data class Instruction(val operation: Operation, val unit: Int)
private data class Position(val horizontal: Int = 0, val depth: Int = 0, val aim: Int = 0)

private fun List<String>.toInstruction() = map { it.split(" ") }
    .map { (operation, unit) -> Instruction(Operation.valueOf(operation.uppercase()), unit.toInt()) }
