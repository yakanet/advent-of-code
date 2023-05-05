@file:Puzzle(2022, 11)

package advent2022

import common.Puzzle
import common.getText
import common.lcm
import java.util.function.Predicate

// Link for the exercise: https://adventofcode.com/2022/day/11
fun main() {
    step1(parseMonkeys("2022/11".getText().lines()))
    step2(parseMonkeys("2022/11".getText().lines()))
}

fun step1(monkeys: List<Monkey>) {
    repeat(20) { round ->
        for (monkey in monkeys) {
            monkey.nextTurn1(monkeys)
        }
        println("\nAfter round ${round + 1}, the monkeys are holding items with these worry levels:")
        for (monkey in monkeys) {
            println("Monkey ${monkey.id}: ${monkey.items.joinToString(", ")}")
        }
    }

    val (first, second) = monkeys.map { it.inspectedItem }.sortedDescending().take(2)
    println("Answer: ${first * second}")
}

fun step2(monkeys: List<Monkey>) {
    val monkeysModulus = monkeys.map { it.condition.divisor }.lcm().toInt()

    repeat(10_000) { round ->
        for (monkey in monkeys) {
            monkey.nextTurn2(monkeys, monkeysModulus)
        }
        println("\n== After round ${round + 1} ==")
        for (monkey in monkeys) {
            println("Monkey ${monkey.id} inspected items ${monkey.inspectedItem}")
        }
    }
    val (first, second) = monkeys.map { it.inspectedItem }.sortedDescending().take(2)
    println("Answer: ${first * second}")
}

typealias MonkeyId = Int
typealias Item = Long

private fun parseMonkeys(input: List<String>): List<Monkey> = buildList {
    var monkeyId: MonkeyId? = null
    var items = listOf<Item>()
    var operation: Operation? = null
    var test: Long? = null
    var trueStep: MonkeyId? = null
    var falseStep: MonkeyId? = null

    fun reset() {
        monkeyId = null
        items = listOf()
        operation = null
        test = null
        trueStep = null
        falseStep = null
    }

    fun addMonkey() {
        if (monkeyId == null) {
            return
        }
        add(
            Monkey(
                monkeyId!!,
                items.toMutableList(),
                operation!!,
                Condition(test!!, IsDivisible(test!!), trueStep!!, falseStep!!)
            )
        )
        reset()
    }

    val monkeyIdRegex = "Monkey (\\d):".toRegex()
    val itemsRegex = "  Starting items: "
    val operationRegex = " {2}Operation: new = old (.) (.+)".toRegex()
    val testRegex = " {2}Test: divisible by (\\d+)".toRegex()
    val testTrueRegex = " {4}If true: throw to monkey (\\d+)".toRegex()
    val testFalseRegex = " {4}If false: throw to monkey (\\d+)".toRegex()
    for (line in input) {
        when {
            line.isEmpty() -> addMonkey()
            monkeyIdRegex.matches(line) -> monkeyId = monkeyIdRegex.matchEntire(line)!!.groups[1]!!.value.toInt()
            line.startsWith(itemsRegex) -> items = line.substring(itemsRegex.length - 1)
                .split(", ")
                .map { it.trim().toLong() }

            operationRegex.matches(line) -> operationRegex.matchEntire(line)!!.let { op ->
                operation = when {
                    op.groups[2]!!.value == "old" -> Squared
                    op.groups[1]!!.value == "*" -> Multiplication(op.groups[2]!!.value.toLong())
                    op.groups[1]!!.value == "+" -> Addition(op.groups[2]!!.value.toLong())
                    else -> TODO("Unknown operation $line")
                }
            }

            testRegex.matches(line) -> test = testRegex.matchEntire(line)!!.groups[1]!!.value.toLong()
            testTrueRegex.matches(line) -> trueStep = testTrueRegex.matchEntire(line)!!.groups[1]!!.value.toInt()
            testFalseRegex.matches(line) -> falseStep = testFalseRegex.matchEntire(line)!!.groups[1]!!.value.toInt()
        }
    }
    addMonkey()
}

data class Monkey(
    val id: Int,
    val items: MutableList<Item>,
    val operation: Operation,
    val condition: Condition
) {
    var inspectedItem: Long = 0L
}

fun Monkey.nextTurn1(pool: List<Monkey>) {
    println("Monkey $id:")
    for (item in items.toList()) {
        this.inspectedItem++
        println(" Monkey inspects an item with a worry level of $item.")
        items.removeFirst()
        var nextItem = operation.execute(item)
        println("   $operation to $nextItem")
        nextItem /= 3
        println("   Monkey gets bored with item. Worry level is divided by 3 to $nextItem.")
        val nextMonkeyId = condition.test(nextItem)
        println("   Item with worry level $nextItem is thrown to monkey $nextMonkeyId.")
        pool[nextMonkeyId].items.add(nextItem)
    }
}

fun Monkey.nextTurn2(pool: List<Monkey>, commonModulus: Int) {
    for (item in items.toList()) {
        this.inspectedItem++
        items.removeFirst()
        var nextItem = operation.execute(item)
        nextItem %= commonModulus
        val nextMonkeyId = condition.test(nextItem)
        pool[nextMonkeyId].items.add(nextItem)
    }
}

sealed interface Operation {
    fun execute(old: Item): Item
}

class Addition(val value: Item) : Operation {
    override fun execute(old: Item) = value + old
    override fun toString() = "Worry level increases by $value"
}

object Squared : Operation {
    override fun execute(old: Item) = old * old
    override fun toString() = "Worry level is multiplied by itself"
}

class Multiplication(val value: Item) : Operation {
    override fun execute(old: Item) = old * value
    override fun toString() = "Worry level is multiplied by $value"
}

class IsDivisible(val value: Item) : Predicate<Item> {
    override fun test(other: Item): Boolean = other % value == 0L
}

data class Condition(
    val divisor: Item,
    val predicate: Predicate<Item>,
    val trueValue: MonkeyId,
    val falseValue: MonkeyId
) {
    fun test(value: Item): Int {
        if (predicate.test(value)) {
            return trueValue
        }
        return falseValue
    }
}
