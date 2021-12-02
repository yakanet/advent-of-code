@file:Puzzle(2021, 2)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/2
fun main() {
    val input = "2021/02".getLines().toInstruction()
    solution1(input)
    solution2(input);
}

private fun solution1(input: List<Instruction>) {
    val position = input.fold(Position(0, 0)) { position, instruction -> position.apply(instruction) }
    println(position.depth * position.horizontal)
}

private fun solution2(input: List<Instruction>) {
    val position = input.fold(Position2(0, 0, 0)) { position, instruction -> position.apply(instruction) }
    println(position.depth * position.horizontal)
}

private enum class Operation { UP, DOWN, FORWARD }
private data class Instruction(val operation: Operation, val unit: Int)
private data class Position(val horizontal: Int, val depth: Int) {
    fun apply(instruction: Instruction): Position = when (instruction.operation) {
        Operation.DOWN -> copy(depth = depth + instruction.unit)
        Operation.UP -> copy(depth = depth - instruction.unit)
        Operation.FORWARD -> copy(horizontal = horizontal + instruction.unit)
    }
}

private data class Position2(val horizontal: Int, val depth: Int, val aim: Int) {
    fun apply(instruction: Instruction): Position2 = when (instruction.operation) {
        Operation.DOWN -> copy(aim = aim + instruction.unit)
        Operation.UP -> copy(aim = aim - instruction.unit)
        Operation.FORWARD -> copy(horizontal = horizontal + instruction.unit, depth = depth + aim * instruction.unit)
    }
}

private fun List<String>.toInstruction(): List<Instruction> {
    return this.map { it.split(" ") }
        .map { (op, unit) -> Instruction(Operation.valueOf(op.uppercase()), unit.toInt()) }
}