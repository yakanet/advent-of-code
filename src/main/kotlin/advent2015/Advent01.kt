@file:Puzzle(2015, 1)
package advent2015

import common.Puzzle
import common.getText

fun main() {
    var counter = 0
    val steps = "2015/01".getText().map { c ->
        when (c) {
            '(' -> ++counter
            else -> --counter
        }
    }
    println(steps.last())
    println(steps.indexOfFirst { it==-1 } + 1)
}
