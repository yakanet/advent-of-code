@file:Puzzle(2021, 4)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/4


private fun solution1(draught: List<Int>, cards: List<Card>): Int {
    draught.forEach { draw ->
        cards.forEach { card ->
            card.draw(draw)
            if (card.isWinner()) {
                return card.result(draw)
            }
        }
    }
    throw RuntimeException("No winner possible")
}

private fun solution2(draught: List<Int>, cards: List<Card>): Int {
    val cardsLeft = cards.toMutableList()
    draught.forEach { draw ->
        val it = cardsLeft.iterator()
        while (it.hasNext()) {
            val card = it.next()
            card.draw(draw)
            if (card.isWinner()) {
                it.remove()
                if (cardsLeft.isEmpty()) {
                    return card.result(draw)
                }
            }
        }
    }
    throw RuntimeException("No winner possible")
}


private fun String.splitNumbers(separator: String = ",") = split(separator)
    .filter { it.isNotBlank() }
    .map { it.toInt() }

private class Card(number: List<Int>) {
    val rows = number.windowed(5, 5)
    val cols = List(5) { col ->
        List(5) { row -> number[row * 5 + col] }
    }

    val numbers = number.associateWith { false }.toMutableMap()

    fun draw(number: Int) {
        if (numbers.containsKey(number)) {
            numbers[number] = true
        }
    }

    fun isWinner(): Boolean {
        if (rows.any { row -> row.all { numbers[it]!! } }) return true
        if (cols.any { col -> col.all { numbers[it]!! } }) return true
        return false
    }

    fun result(draw: Int): Int {
        val notMarked = numbers.filter { (_, marked) -> !marked }.keys.sum()
        return notMarked * draw
    }
}

private fun List<String>.toCards(): List<Card> = buildList {
    var currentCard = mutableListOf<Int>()
    this@toCards.forEach { line ->
        if (line.isEmpty()) {
            add(Card(currentCard))
            currentCard = mutableListOf()
        } else {
            currentCard += line.splitNumbers(" ")
        }
    }
    add(Card(currentCard))
}

fun main() {
    val input = "2021/04".getLines()
    println(solution1(input.first().splitNumbers(","), input.drop(2).toCards()))
    println(solution2(input.first().splitNumbers(","), input.drop(2).toCards()))

}
