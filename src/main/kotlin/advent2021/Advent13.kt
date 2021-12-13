@file:Puzzle(2021, 13)

package advent2021

import common.Puzzle
import common.getLines

// Link for the exercise: https://adventofcode.com/2021/day/13
fun main() {
    val (dots, folds) = "2021/13".getLines().getInstruction()
    solution1(dots, folds)
    solution2(dots, folds)
}

private fun solution1(dots: List<Dot>, folds: List<Fold>) {
    println(dots.fold(folds.take(1)).size)
}

private fun solution2(dots: List<Dot>, folds: List<Fold>) {
    dots.fold(folds).print()
}

private fun List<Dot>.fold(folds: List<Fold>): Set<Dot> {
    var dotsList = toSet()
    folds.forEach { (foldType, position) ->
        dotsList = when (foldType) {
            FoldType.VERTICAL -> {
                val (left, right) = dotsList.partition { it.x < position }
                val rightFolded = right.filter { it.x != position }
                    .map { it.copy(x = position - (it.x - position)) }
                (left + rightFolded).toSet()
            }
            FoldType.HORIZONTAL -> {
                val (top, bottom) = dotsList.partition { it.y < position }
                val bottomFolded = bottom.filter { it.y != position }
                    .map { it.copy(y = position - (it.y - position)) }
                (top + bottomFolded).toSet()
            }
        }
    }
    return dotsList
}

private fun Set<Dot>.print() {
    val width: Int = maxOf { it.x }
    val height: Int = maxOf { it.y }
    (0..height).forEach { y ->
        println((0..width).map { x -> if (contains(Dot(x, y))) '#' else '.' }.joinToString(""))
    }
}

private data class Dot(val x: Int, val y: Int)
private data class Fold(val foldType: FoldType, val position: Int)
private enum class FoldType { HORIZONTAL, VERTICAL }

private fun List<String>.getInstruction(): Pair<List<Dot>, List<Fold>> {
    val dotLine = Regex("(\\d+),(\\d+)")
    val foldLine = Regex("fold along ([x|y])=(\\d+)")
    val dots = mutableListOf<Dot>()
    val folds = mutableListOf<Fold>()
    forEach { line ->
        when {
            line.matches(dotLine) -> dotLine.find(line)
                ?.let { result -> dots.add(Dot(result.groups[1]!!.value.toInt(), result.groups[2]!!.value.toInt())) }
            line.matches(foldLine) -> foldLine.find(line)?.let { result ->
                folds.add(
                    Fold(
                        if (result.groups[1]!!.value == "x") FoldType.VERTICAL else FoldType.HORIZONTAL,
                        result.groups[2]!!.value.toInt()
                    )
                )
            }
        }
    }
    return dots.toList() to folds.toList()
}