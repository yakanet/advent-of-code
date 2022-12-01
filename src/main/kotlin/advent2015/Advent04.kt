@file:Puzzle(2015, 4)

package advent2015

import common.Puzzle
import common.getText
import java.math.BigInteger
import java.security.MessageDigest

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
        if (md5("$this$count").startsWith(startWith)) {
            return count
        }
        count++
    }
}

fun md5(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}
