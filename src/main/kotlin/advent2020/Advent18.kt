@file:Puzzle(2020, 18)

package advent2020

import common.Puzzle
import common.getLines
import java.util.*

// Link for the exercise: https://adventofcode.com/2020/day/18
fun main() {
    val input = "2020/18".getLines()
    // Part 1
    println(input.sumOf { line ->
        line.tokenize().evaluate()
    })

    // Part 2
    println(input.sumOf { line ->
        line.tokenize().addAdditionPrecedence().evaluate()
    })
}

private fun List<Token>.evaluate(): Long {
    var left = 0L
    var operator: Operator? = null
    for (t in this) {
        when (t) {
            is Value -> if (operator != null) {
                left = operator.execute(left, t.value)
                operator = null
            } else {
                left = t.value
            }

            is Operation -> operator = t.operator
            is Parenthesis -> if (operator != null) {
                left = operator.execute(left, t.expressions.evaluate())
                operator = null
            } else {
                left = t.expressions.evaluate()
            }
        }
    }
    return left
}

private fun List<Token>.addAdditionPrecedence(): List<Token> {
    val stack = mutableListOf<Token>()
    for (t in this) {
        when (t) {
            is Value -> {
                val last = stack.lastOrNull()
                if (last is Operation && last.operator == Operator.ADDITION) {
                    val o = stack.removeLast()
                    stack.add(Parenthesis(listOf(stack.removeLast(), o, t)))
                } else {
                    stack.add(t)
                }
            }

            is Operation -> stack.add(t)
            is Parenthesis -> {
                val last = stack.lastOrNull()
                if (last is Operation && last.operator == Operator.ADDITION) {
                    val o = stack.removeLast()
                    stack.add(
                        Parenthesis(
                            listOf(
                                stack.removeLast(),
                                o,
                                Parenthesis(t.expressions.addAdditionPrecedence())
                            )
                        )
                    )
                } else {
                    stack.add(Parenthesis(t.expressions.addAdditionPrecedence()))
                }
            }
        }
    }
    return stack
}

sealed class Token
data class Value(val value: Long) : Token() {
    override fun toString() = value.toString()
}

data class Operation(val operator: Operator) : Token() {
    override fun toString() = when (operator) {
        Operator.ADDITION -> "+"
        Operator.MULTIPLICATION -> "*"
    }
}

data class Parenthesis(val expressions: List<Token>) : Token() {
    override fun toString() = expressions.joinToString(" ", "(", ")")
}

fun String.tokenize(): List<Token> {
    val regex = Regex("[0-9]+|[*+]|[()]")
    val stack = LinkedList<MutableList<Token>>()
    stack.push(mutableListOf())
    regex.findAll(this).map { it.value }.forEach {
        when {
            isDigits(it) -> stack.peek().add(Value(it.toLong()))
            it == "+" -> stack.peek().add(Operation(Operator.ADDITION))
            it == "*" -> stack.peek().add(Operation(Operator.MULTIPLICATION))
            it == "(" -> stack.push(mutableListOf())
            it == ")" -> stack.pop().let { s -> stack.peek().add(Parenthesis(s)) }
        }
    }
    return stack.pop()
}

private fun isDigits(value: String): Boolean {
    val (_, notDigits) = value.partition { it.isDigit() }
    return notDigits.isEmpty()
}

enum class Operator(val execute: (left: Long, right: Long) -> Long) {
    ADDITION({ left, right -> left + right }),
    MULTIPLICATION({ left, right -> left * right }),
}
