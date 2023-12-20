import kotlin.math.max
import kotlin.math.min

private val input = readFile("19.txt").split("\n\n")

private val workflows = input[0].lines().map { Workflow.fromString(it) }
private val workflowsMap = workflows.associateBy { it.label }
private val parts = input[1].lines().map { line ->
    val (x, m, a, s) = line.substringBetween("{", "}").split(",").map { it.substringAfter("=").toInt() }
    Part(x, m, a, s)
}

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    parts.filter { executeWorkflow(it) }.sumOf { it.partSum() }

private fun part2(): Long {
    var total = 0L

    val initialPartRange = PartRange(1..4000, 1..4000, 1..4000, 1..4000)
    val queue = mutableListOf("in" to initialPartRange)

    while (queue.isNotEmpty()) {
        val (where, part) = queue.removeFirst()
        if (where == "A") {
            total += part.score()
        } else if (where == "R") {
            continue
        } else {
            val workflow = workflowsMap[where]!!
            workflow.actions.fold(part) { accPart, action ->
                when (action) {
                    is Comparison -> {
                        val satisfiedPart = accPart.updatedWith(action.symbol, action.comparator, action.amount)
                        val unsatisfiedPart =
                            accPart.updatedOppositeWith(action.symbol, action.comparator, action.amount)
                        queue.add(action.target to satisfiedPart)
                        unsatisfiedPart
                    }

                    is Redirect -> {
                        queue.add(action.target to accPart)
                        accPart // Will always be last element so this is ignored
                    }
                }
            }
        }
    }

    return total
}

private fun executeWorkflow(part: Part): Boolean {
    var target = "in"
    while (target !in listOf("A", "R")) {
        val workflow = workflowsMap[target]!!
        target = workflow.actions.firstNotNullOf { it.test(part) }
    }
    return target == "A"
}

private data class Part(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun partSum() = x + m + a + s
}

private data class PartRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
    fun updatedWith(symbol: Char, comparator: Char, value: Int) =
        if (comparator == '<') {
            when (symbol) {
                'x' -> copy(x = x.first..<min(x.last, value))
                'm' -> copy(m = m.first..<min(m.last, value))
                'a' -> copy(a = a.first..<min(a.last, value))
                else -> copy(s = s.first..<min(s.last, value))
            }
        } else {
            when (symbol) {
                'x' -> copy(x = max(x.first + 1, value + 1)..x.last)
                'm' -> copy(m = max(m.first + 1, value + 1)..m.last)
                'a' -> copy(a = max(a.first + 1, value + 1)..a.last)
                else -> copy(s = max(s.first + 1, value + 1)..s.last)
            }
        }

    fun updatedOppositeWith(symbol: Char, comparator: Char, value: Int) =
        if (comparator == '<') {
            when (symbol) {
                'x' -> copy(x = max(x.first, value)..x.last)
                'm' -> copy(m = max(m.first, value)..m.last)
                'a' -> copy(a = max(a.first, value)..a.last)
                else -> copy(s = max(s.first, value)..s.last)
            }
        } else {
            when (symbol) {
                'x' -> copy(x = x.first..min(x.last, value))
                'm' -> copy(m = m.first..min(m.last, value))
                'a' -> copy(a = a.first..min(a.last, value))
                else -> copy(s = s.first..min(s.last, value))
            }
        }

    fun score() = x.count().toLong() * m.count() * a.count() * s.count()
}

private data class Workflow(val label: String, val actions: List<Action>) {

    companion object {
        fun fromString(str: String) = Workflow(
            label = str.substringBefore("{"),
            actions = str.substringBetween("{", "}").split(",").map { Action.fromString(it) }
        )
    }
}

private sealed class Action {
    abstract fun test(part: Part): String?

    companion object {
        fun fromString(str: String): Action =
            if (str.contains(":")) Comparison.fromString(str) else Redirect(str)
    }
}

private data class Redirect(val target: String) : Action() {
    override fun test(part: Part) = target
}

private data class Comparison(
    val symbol: Char,
    val comparator: Char,
    val amount: Int,
    val target: String
) : Action() {

    override fun test(part: Part): String? {
        val comparison = when (symbol) {
            'x' -> part.x; 'm' -> part.m; 'a' -> part.a; 's' -> part.s;
            else -> error("unknown part")
        }.compareTo(amount)
        if (comparator == '>' && comparison > 0) return target
        if (comparator == '<' && comparison < 0) return target
        return null
    }

    companion object {
        fun fromString(str: String): Comparison {
            val symbol = str.first()
            val comparator = str.dropWhile { it.isLetter() }.first()
            val amount = str.substringBefore(":").substringAfter("<").substringAfter(">").toInt()
            val target = str.substringAfter(":")
            return Comparison(symbol, comparator, amount, target)
        }
    }
}
