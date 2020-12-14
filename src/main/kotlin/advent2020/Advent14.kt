@file:Puzzle(2020, 14)

package advent2020

import common.Puzzle
import common.getLines

private const val BINARY_SIZE = 36
private const val MASK_PATTERN = "mask = "

// Link for the exercise: https://adventofcode.com/2020/day/14
fun main() {
    val instructions = "2020/14".getLines().map { line ->
        when {
            line.startsWith(MASK_PATTERN) -> Mask(Floating(line.replace(MASK_PATTERN, "")))
            else -> Regex("mem\\[(\\d+)] = (\\d+)").matchEntire(line).let {
                Memory(
                    Binary(it!!.groups[1]!!.value.toInt()),
                    Binary(it.groups[2]!!.value.toLong())
                )
            }
        }
    }
    // Part 1
    println(instructions.execute().values.map { it.toLong() }.sum())

    // Part 2
    println(instructions.executeFloating().values.map { it.toLong() }.sum())
}

private fun List<Instruction>.execute(): Map<Binary, Binary> {
    val heap = mutableMapOf<Binary, Binary>()
    var currentMask: Mask? = null
    forEach { instruction ->
        when (instruction) {
            is Mask -> currentMask = instruction
            is Memory -> heap[instruction.address] = instruction.value + currentMask!!.mask
        }
    }
    return heap.toMap()
}

private fun List<Instruction>.executeFloating(): Map<Binary, Binary> {
    val heap = mutableMapOf<Binary, Binary>()
    var currentMask: Mask? = null
    forEach { instruction ->
        when (instruction) {
            is Mask -> currentMask = instruction
            is Memory -> instruction.floatingMask(currentMask!!).forEach {
                heap[it.address] = it.value
            }
        }
    }
    return heap.toMap()
}

private fun Memory.floatingMask(mask: Mask): List<Memory> {
    val floatingAddress = address.toFloating() + mask.mask
    return floatingAddress.toBinaries().map { Memory(it, value) }
}

private interface Instruction
private data class Mask(val mask: Floating) : Instruction
private data class Memory(val address: Binary, val value: Binary) : Instruction

private data class Binary(private val rawBinary: String) {
    constructor(value: Int) : this(value.toLong())
    constructor(value: Long) : this(value.toString(2).padStart(BINARY_SIZE, '0'))

    fun toLong() = rawBinary.reversed().foldIndexed(0L) { i, acc, c ->
        acc + c.toString().toLong().shl(i)
    }

    fun toInt() = rawBinary.reversed().foldIndexed(0) { i, acc, c ->
        acc + c.toString().toInt().shl(i)
    }

    fun toFloating() = Floating(rawBinary)

    override fun toString(): String {
        return toLong().toString()
    }

    fun size() = rawBinary.length

    operator fun plus(mask: Floating) = Binary(
        mask.rawBinary.zip(rawBinary) { m, v ->
            when (m) {
                '0' -> '0'
                '1' -> '1'
                else -> v
            }
        }.joinToString("")
    )
}

private data class Floating(val rawBinary: String) {
    operator fun plus(other: Floating) = Floating(
        other.rawBinary.zip(rawBinary) { m, v ->
            when (m) {
                '0' -> v
                '1' -> '1'
                else -> 'X'
            }
        }.joinToString("")
    )

    fun toBinaries(): List<Binary> {
        val xCount = Binary("1".repeat(rawBinary.count { it=='X' }))
        return (0..xCount.toInt()).map {
            val mask = it.toString(2).padStart(xCount.size(), '0')
            var counter = 0
            Binary(rawBinary.map { c ->
                if (c=='X') mask[counter++] else c
            }.joinToString(""))
        }
    }
}
