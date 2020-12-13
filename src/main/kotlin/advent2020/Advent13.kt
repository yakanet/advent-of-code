@file:Puzzle(2020, 13)

package advent2020

import com.google.common.math.LongMath.gcd
import common.Puzzle
import common.getLines
import kotlin.math.abs

// Link for the exercise: https://adventofcode.com/2020/day/13
fun main() {
    val (time, buses) = "2020/13".getLines().let { (rawTime, rawBus) ->
        Pair(rawTime.toLong(), rawBus.split(',').mapIndexedNotNull { i, bus ->
            if (bus != "x") Bus(i, bus.toLong()) else null
        })
    }
    // Part 1
    buses.findFirstDepartureAt(time).let { (bus, departure) ->
        val waitingTime = departure - time
        println(waitingTime * bus.id)
    }

    // Part 2
    println(buses.findSequentialDeparture())
}

private fun List<Bus>.findSequentialDeparture(): Long {
    var step = this[0].id
    var departure = 0L
    do {
        departure += step
        val busCanLeave = filter { bus -> bus.canLeave(departure + bus.index) }
        if (busCanLeave.isNotEmpty()) {
            step = busCanLeave.map { it.id }.reduce { a, b -> lcm(a, b) }
        }
    } while (busCanLeave != this)
    return departure
}

// https://en.wikipedia.org/wiki/Least_common_multiple
fun lcm(a: Long, b: Long): Long = abs(a * b) / gcd(a, b)

private fun List<Bus>.findFirstDepartureAt(time: Long): Pair<Bus, Long> {
    var departure = time
    while (true) {
        forEach { bus ->
            if (bus.canLeave(departure)) {
                return Pair(bus, departure)
            }
        }
        departure++
    }
}

private class Bus(val index: Int, val id: Long) {
    fun canLeave(time: Long): Boolean {
        return time % id == 0L
    }
}
