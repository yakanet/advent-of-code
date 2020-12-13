@file:Puzzle(2020, 4)
package advent2020

import common.*

// Link for the exercise: https://adventofcode.com/2020/day/4
fun main() {
    data class Passport(
        val byr: String,
        val iyr: String,
        val eyr: String,
        val hgt: String,
        val hcl: String,
        val ecl: String,
        val pid: String,
        val cid: String?
    ) {
        fun isValid(): Boolean {
            return isBirthdayValid() && isIssueValid() && isExpireValid() && isHeightValid() && isHairValid() && isEyeValid() && isPasswordValid();
        }

        fun isBirthdayValid() = byr.toInt() in 1920..2020
        fun isIssueValid() = iyr.toInt() in 2010..2020
        fun isExpireValid() = eyr.toInt() in 2020..2030
        fun isHeightValid(): Boolean {
            val res = Regex("([0-9]+)(cm|in)").matchEntire(hgt)
            return when (res?.groups?.get(2)?.value) {
                "cm" -> res.groups.get(1)?.value?.toInt() in 150..193
                "in" -> res.groups.get(1)?.value?.toInt() in 59..76
                else -> false
            }
        }

        fun isHairValid() = Regex("#[0-9a-f]{6}").matches(hcl)
        fun isEyeValid() = Regex("amb|blu|brn|gry|grn|hzl|oth").matches(ecl)
        fun isPasswordValid() = Regex("[0-9]{9}").matches(pid)

    }

    fun Map<String, String>.toPassport(): Passport {
        return Passport(
            this["byr"]!!,
            this["iyr"]!!,
            this["eyr"]!!,
            this["hgt"]!!,
            this["hcl"]!!,
            this["ecl"]!!,
            this["pid"]!!,
            this["cid"]
        )
    }

    fun StringBuilder.toMap(): Map<String, String> {
        val parts = this.trim().split(" ")
        return parts.map {
            val (key, value) = it.split(":")
            key to value
        }.toMap()
    }


    val lines = "2020/04".getLines()
    val builder = StringBuilder()
    val list = mutableListOf<Map<String, String>>()
    lines.forEach { line ->
        if (line.isEmpty()) {
            list.add(builder.toMap())
            builder.clear()
        } else {
            builder.append(" $line")
        }
    }
    if (builder.isNotEmpty()) {
        list.add(builder.toMap())
    }
    val res = list.filter {
        listOf("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid").all { i -> it.containsKey(i) }
    }.map { it.toPassport() }
    println(res.size)

    val res2 = res.filter { it.isValid() }
    println(res2.size)
}

