package common

import kotlin.math.abs

fun gcd(a: Int, b: Int): Int = if (b == 0) {
    abs(a)
} else {
    gcd(b, a % b)
}

fun gcd(a: Long, b: Long): Long = if (b == 0L) {
    abs(a)
} else {
    gcd(b, a % b)
}


fun lcm(a: Long, b: Long) = (a * b) / gcd(a, b)
fun Iterable<Long>.lcm(): Long = reduce(::lcm)


