@file:Puzzle(2020, 17)

package advent2020

import common.Puzzle
import common.getLines

private const val INACTIVE = '.'
private const val ACTIVE = '#'

// Link for the exercise: https://adventofcode.com/2020/day/17
fun main() {
    val input = "2020/17".getLines(1)
    // Part 1
    println((1..6).fold(input.createSpace()) { space, _ ->
        space.cycle(Cube::neighbors3)
    }.activeCube.size)

    // Part 2
    println((1..6).fold(input.createSpace()) { space, _ ->
        space.cycle(Cube::neighbors4)
    }.activeCube.size)
}

private data class Space(val activeCube: Set<Cube>) {

    fun cycle(neighborFunction: (Cube) -> List<Cube>): Space {
        val neighborsMap = mutableMapOf<Cube, Int>()
        for (c in activeCube) {
            for (neighbor in neighborFunction(c)) {
                neighborsMap[neighbor] = neighborsMap.getOrDefault(neighbor, 0) + 1
            }
        }
        return Space(neighborsMap.entries.mapNotNull { (cube, neighborCount) ->
            when {
                neighborCount==3 -> cube
                neighborCount==2 && cube.isActive() -> cube
                else -> null
            }
        }.toSet())
    }

    fun Cube.isActive() = activeCube.contains(this)

    override fun toString(): String {
        val (minX, maxX) = activeCube.minOf { it.x } to activeCube.maxOf { it.x }
        val (minY, maxY) = activeCube.minOf { it.y } to activeCube.maxOf { it.y }
        val (minZ, maxZ) = activeCube.minOf { it.z } to activeCube.maxOf { it.z }
        val (minW, maxW) = activeCube.minOf { it.w } to activeCube.maxOf { it.w }
        val builder = StringBuilder()
        for (w in minW..maxW) {
            for (z in minZ..maxZ) {
                builder.appendLine("z=$z, w=$w")
                for (y in minY..maxY) {
                    builder.appendLine(
                        (minX..maxX).map { x ->
                            val cube = Cube(x, y, z, w)
                            if (cube.isActive()) ACTIVE else INACTIVE
                        }.joinToString("")
                    )
                }
                builder.appendLine()
            }
        }
        return builder.toString()
    }
}

private fun List<String>.createSpace(): Space {
    val result = mutableSetOf<Cube>()
    forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            if (c==ACTIVE) {
                result.add(Cube(x, y))
            }
        }
    }
    return Space(result)
}

private data class Cube(val x: Int, val y: Int, val z: Int = 0, val w: Int = 0)

private fun Cube.neighbors3(): List<Cube> {
    val result = mutableListOf<Cube>()
    for (dx in -1..1) {
        for (dy in -1..1) {
            for (dz in -1..1) {
                Cube(this.x + dx, this.y + dy, this.z + dz).let {
                    if (this!=it) {
                        result.add(it)
                    }
                }
            }
        }
    }
    return result
}


private fun Cube.neighbors4(): List<Cube> {
    val result = mutableListOf<Cube>()
    for (dx in -1..1) {
        for (dy in -1..1) {
            for (dz in -1..1) {
                for (dw in -1..1) {
                    Cube(this.x + dx, this.y + dy, this.z + dz, this.w + dw).let {
                        if (this!=it) {
                            result.add(it)
                        }
                    }
                }
            }
        }
    }
    return result
}

