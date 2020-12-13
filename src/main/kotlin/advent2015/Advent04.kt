@file:Puzzle(2015, 4)

package advent2015

import common.Puzzle
import common.getText
import org.apache.commons.codec.digest.DigestUtils

// Link for the exercise: https://adventofcode.com/2015/day/4
fun main() {
    val input = "2015/04".getText()

    // Part 1
    println(input.findMd5StartWith("00000"))

    // Part 2
    println(input.findMd5StartWith("000000"))
}

private fun String.findMd5StartWith(startWith: String): Long {
    var count = 0L
    while (true) {
        if (DigestUtils.md5Hex("$this$count").startsWith(startWith)) {
            return count
        }
        count++
    }
}
