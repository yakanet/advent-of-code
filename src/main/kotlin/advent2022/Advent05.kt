@file:Puzzle(2022, 5)

package advent2022

import common.Puzzle
import common.getText

// Link for the exercise: https://adventofcode.com/2022/day/5
fun main() {
    val input = "2022/05".getText()
    val (rawCargo, rawInstructions) = input.split("\n\n")
    println(part1(rawCargo.toCargo(), rawInstructions.toInstructions()))
    println(part2(rawCargo.toCargo(), rawInstructions.toInstructions()))
}

private fun part1(cargo: Cargo, instructions: List<Instruction>): String {
    for (instruction in instructions) {
        for (q in 1..instruction.quantity) {
            val crate = cargo.lines[instruction.from - 1].removeLast()
            cargo.lines[instruction.to - 1].add(crate)
        }
    }
    return cargo.getTops().joinToString("")
}

private fun part2(cargo: Cargo, instructions: List<Instruction>): String {
    for (instruction in instructions) {
        val crates = (1..instruction.quantity).map { cargo.lines[instruction.from - 1].removeLast() }.reversed()
        cargo.lines[instruction.to - 1].addAll(crates)
    }
    return cargo.getTops().joinToString("")
}


private class Cargo {
    lateinit var lines: List<MutableList<Crate>>
    var initialized = false
    fun initialize(indexes: List<String>) {
        lines = List(indexes.size) { mutableListOf() }
        initialized = true
    }

    fun appendCratesOnLine(crates: List<Crate>) {
        crates.forEachIndexed { index, crate ->
            if (crate.data.isNotEmpty())
                lines[index].add(crate)
        }
    }

    fun getTops() = lines.map { it.last().data }
}

private data class Crate(val data: String) {
    override fun toString() = "[$data]"
}

private data class Instruction(val quantity: Int, val from: Int, val to: Int)

private fun String.toCargo(): Cargo {
    val cargo = Cargo()
    lines().reversed().map { line ->
        val crates = line.chunked(4)
        if (!cargo.initialized) {
            cargo.initialize(crates)
        } else {
            cargo.appendCratesOnLine(crates.map { it.toCrate() })
        }
    }
    return cargo
}

private fun String.toCrate() = Crate(trimStart('[', ' ').trimEnd(']', ' '))

private fun String.toInstructions() = lines().filter(String::isNotEmpty).map {
    val match = Regex("move (\\d+) from (\\d+) to (\\d+)").matchEntire(it)!!
    Instruction(match.groupValues[1].toInt(), match.groupValues[2].toInt(), match.groupValues[3].toInt())
}
