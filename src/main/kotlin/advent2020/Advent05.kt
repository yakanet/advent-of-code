@file:Exercise(2020, 5)
package advent2020

import common.*

// Link for the exercise: https://adventofcode.com/2020/day/5
fun main() {
    data class Seat(val row: Int, val col: Int) {
        val seatId: Int = row * 8 + col
        override fun toString() = "${seatId.toString().padStart(3, '0')} - " + Pair(row, col)
    }

    fun String.toSeat(): Seat {
        var rows = (0..127).toList()
        var cols = (0..7).toList()
        for (c in this) {
            when (c) {
                'F' -> rows = rows.dropLast((rows.last() - rows.first() + 1) / 2)
                'B' -> rows = rows.drop((rows.last() - rows.first() + 1) / 2)
                'R' -> cols = cols.drop((cols.last() - cols.first() + 1) / 2)
                'L' -> cols = cols.dropLast((cols.last() - cols.first() + 1) / 2)
            }
        }
        return Seat(rows.first(), cols.first())
    }

    val lines = "2020/05".getLines()
    val list = lines.map { it.toSeat() }.toMutableList()
    list.sortBy { it.seatId }
    println(list.last().seatId)

    var lastSeat = list.first()
    list.drop(1).forEach { seat ->
        if (lastSeat.seatId + 1!=seat.seatId) {
            println(lastSeat.seatId + 1)
        }
        lastSeat = seat
    }
}

