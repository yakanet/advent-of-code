@file:Exercise(2020, 2)

package advent2020

import common.Exercise
import common.getLines

// Link for the exercise: https://adventofcode.com/2020/day/2
fun main() {
    val lines = "2020/02".getLines().map { it.parsePolicyAndPassword() }

    // Part 1
    println(lines.filter { (policy, password) -> policy.isValid1(password) }.size)

    // Part 2
    println(lines.filter { (policy, password) -> policy.isValid2(password) }.size)
}

private fun PasswordPolicy.isValid1(password: String) =
    password.count { it==letter } in first..second

private fun PasswordPolicy.isValid2(password: String) =
    (password[first - 1]==letter).xor(password[second - 1]==letter)

private fun String.parsePolicyAndPassword(): Pair<PasswordPolicy, String> {
    val (range, letter, password) = split(" ")
    val (first, second) = range.split("-").map { it.toInt() }
    return Pair(
        PasswordPolicy(first, second, letter[0]),
        password.trim()
    )
}

private class PasswordPolicy(val first: Int, val second: Int, val letter: Char)
