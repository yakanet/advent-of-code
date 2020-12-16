@file:Puzzle(2020, 16)

package advent2020

import common.Puzzle
import common.getText
import java.util.regex.Pattern.compile

private const val PARSING_REGEX = "([a-z ]+): (\\d+)-(\\d+) or (\\d+)-(\\d+)|([\\d,]+)"
private const val REG_RULE_NAME = 1
private const val REG_RULE_RANGE1_MIN = 2
private const val REG_RULE_RANGE1_MAX = 3
private const val REG_RULE_RANGE2_MIN = 4
private const val REG_RULE_RANGE2_MAX = 5
private const val REG_TICKET_LINE = 6


// Link for the exercise: https://adventofcode.com/2020/day/16
fun main() {
    val (rules, tickets) = "2020/16".getText().parseRulesAndTickets()

    // Part 1
    rules.validNumbers()
        .let { valids -> tickets.other.flatten().filter { !valids.contains(it) } }
        .let { invalids -> println(invalids.sum()) }

    // Part 2
    val validGroup = tickets.discardInvalidTickets(rules)
    validGroup.findFieldPositions(rules).let {
        it.filter { (_, value) -> value.name.startsWith("departure") }
            .map { (index, _) -> validGroup.mine[index].toLong() }
            .reduce { acc, i -> acc * i }
    }.let { println(it) }
}

private fun String.parseRulesAndTickets(): Pair<List<Rule>, TicketGroup> {
    val rules = mutableListOf<Rule>()
    val tickets = mutableListOf<Ticket>()
    val regex = compile(PARSING_REGEX).matcher(this)
    generateSequence { if (regex.find()) regex else null }.forEach { m ->
        when {
            m.group(REG_RULE_NAME)!=null -> rules += Rule(
                m.group(REG_RULE_NAME),
                listOf(
                    IntRange(m.group(REG_RULE_RANGE1_MIN).toInt(), m.group(REG_RULE_RANGE1_MAX).toInt()),
                    IntRange(m.group(REG_RULE_RANGE2_MIN).toInt(), m.group(REG_RULE_RANGE2_MAX).toInt()),
                )
            )
            m.group(REG_TICKET_LINE)!=null -> tickets += m.group(REG_TICKET_LINE)
                .split(",")
                .map { it.toInt() }
        }
    }
    return Pair(rules.toList(), TicketGroup(tickets.first(), tickets.drop(1)))
}

private typealias Ticket = List<Int>

private data class Rule(val name: String, val rules: List<IntRange>)
private data class TicketGroup(val mine: Ticket, val other: List<Ticket>) {
    fun discardInvalidTickets(rules: List<Rule>): TicketGroup {
        val valids = rules.validNumbers()
        val newOther = other.filter { tickets -> !tickets.any { !valids.contains(it) } }
        return TicketGroup(mine, newOther)
    }

    fun findFieldPositions(rules: List<Rule>): Map<Int, Rule> {
        val indexRule = Array(rules.size) { rules.toList() }
        other.forEach { ticket ->
            ticket.forEachIndexed { i, value ->
                indexRule[i] = indexRule[i].removeNotMatching(value)
            }
        }
        return indexRule.decideIndexPosition(rules)
    }

    private fun Array<List<Rule>>.decideIndexPosition(rules: List<Rule>): Map<Int, Rule> {
        val result = mutableMapOf<Int, Rule>()
        var rulePossibleIndex = rules.map { rule ->
            rule to mapIndexed { index, list -> IndexedValue(index, list) }
                .filter { (_, list) -> list.contains(rule) }
                .map { it.index }
        }.sortedBy { it.second.size }.toMap()
        for (rule in rulePossibleIndex.keys) {
            if (rulePossibleIndex[rule]!!.size!=1) {
                throw Exception("Couldn't decide the field position")
            }
            val index = rulePossibleIndex[rule]!!.first()
            result[index] = rule
            rulePossibleIndex = rulePossibleIndex.map { entry -> entry.key to entry.value.filter { it!=index } }.toMap()
        }
        return result.toMap()
    }
}

private fun List<Rule>.removeNotMatching(value: Int) = filter { it.rules.any { range -> value in range } }

private fun List<Rule>.validNumbers() = flatMap { rule -> rule.rules.flatten() }.toSet()
