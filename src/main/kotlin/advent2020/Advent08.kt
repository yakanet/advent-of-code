@file:Puzzle(2020, 8)
package advent2020

import common.*

// Link for the exercise: https://adventofcode.com/2020/day/8
fun main() {
    data class EndInstruction(val finished: Boolean, val counter: Int)
    data class CodeOp(val instruction: String, val delta: Int, val line: Int)

    fun Map<Int, CodeOp>.execute(): EndInstruction {
        var cursor = 0
        var counter = 0
        val visited = mutableSetOf<CodeOp>()
        while (true) {
            val inst = this[cursor] ?: return EndInstruction(true, counter)
            if (visited.contains(inst)) {
                return EndInstruction(false, counter)
            }
            var jmp = 1
            when (inst.instruction) {
                "acc" -> counter += inst.delta;
                "jmp" -> jmp = inst.delta
            }
            visited.add(inst)
            cursor += jmp
        }
    }

    val lines = "2020/08".getLines()
    val instructions = lines.withIndex()
        .map { (lineIndex, line) ->
            val (operation, delta) = line.split(" ")
            lineIndex to CodeOp(operation, delta.toInt(), lineIndex + 1)
        }.toMap()
    println(instructions.execute().counter)
    for (i in instructions.keys.indices) {
        val mutedInstruction = instructions.toMutableMap()
        val toChange = mutedInstruction[i]!!
        when (toChange.instruction) {
            "jmp" -> mutedInstruction[i] = toChange.copy(instruction = "nop")
            "nop" -> mutedInstruction[i] = toChange.copy(instruction = "jmp")
        }
        if (toChange!=mutedInstruction[i]) {
            val res = mutedInstruction.execute()
            if (res.finished) {
                println("Updated line $toChange")
                println(res.counter)
                return
            }
        }
    }
}
