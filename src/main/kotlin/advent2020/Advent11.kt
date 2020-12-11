@file:Exercise(2020, 11)
package advent2020

import common.*


private const val OCCUPIED = '#'
private const val EMPTY = 'L'
private const val FLOOR = '.'

private val ADJACENT_POSITION = listOf(
    Position(-1, 0),
    Position(1, 0),
    Position(0, -1),
    Position(0, 1),
    Position(-1, -1),
    Position(1, -1),
    Position(-1, 1),
    Position(1, 1)
)

// Link for the exercise: https://adventofcode.com/2020/day/12
fun main() {
    val lines = "2020/11".getLines()

    // Part 1
    println(Plane(lines).mutateUntilStable(4) { p, x, y -> p.getImmediateAdjacents(x, y) }.countOccupied())

    // Part 2
    println(Plane(lines).mutateUntilStable(5) { p, x, y -> p.getAdjacents(x, y) }.countOccupied())
}

private fun Plane.countOccupied(): Int =
    (0 until height).map { y ->
        (0 until width).filter { x -> this.getAt(x, y)==OCCUPIED }.size
    }.sum()

private data class Plane(val lines: List<String>) {
    val width = lines[0].length
    val height = lines.size
    fun getAt(x: Int, y: Int): Char? {
        if (x in 0 until width && y in 0 until height) {
            return lines[y][x]
        }
        return null
    }

    fun getImmediateAdjacents(x: Int, y: Int): List<Char> {
        return ADJACENT_POSITION
            .mapNotNull { seat -> getAt(x + seat.x, y + seat.y) }
    }

    fun getAdjacents(x: Int, y: Int): List<Char> {
        return ADJACENT_POSITION
            .mapNotNull { delta ->
                var position: Position
                var i = 0
                do {
                    position = delta * ++i
                } while (getAt(x + position.x, y + position.y)==FLOOR)
                getAt(x + position.x, y + position.y)
            }
    }

    override fun toString() = lines.joinToString("\n")
}

data class Position(val x: Int, val y: Int) {
    operator fun times(time: Int) = Position(x * time, y * time)
}

private fun Plane.mutateUntilStable(
    maxOccupied: Int,
    adjacentRule: (plane: Plane, x: Int, y: Int) -> List<Char>
): Plane {
    val planes = mutableListOf(this)
    do {
        planes.add(planes.last().mutate(maxOccupied, adjacentRule))
    } while (planes.dropLast(1).last()!=planes.last())
    return planes.last()
}

private fun Plane.mutate(maxOccupied: Int, adjacentRule: (plane: Plane, x: Int, y: Int) -> List<Char>) = Plane(
    (0 until height).map { y ->
        (0 until width).map { x ->
            val adjacents = adjacentRule(this, x, y)
            when (getAt(x, y)) {
                FLOOR -> FLOOR
                EMPTY -> if (adjacents.all { it!=OCCUPIED }) OCCUPIED else EMPTY
                else -> if (adjacents.filter { it==OCCUPIED }.size >= maxOccupied) EMPTY else OCCUPIED
            }
        }.joinToString("")
    }
)


