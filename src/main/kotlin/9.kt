private val sequences = readFileAsLines("9.txt").map { line ->
    line.split(" ").map { it.toInt() }
}

fun main() {
    println(sequences)
    println(part1())
    println(part2())
}

private fun part1() =
    sequences.sumOf { findTerm(NextOrPreceding.NEXT, it) }

private fun part2() =
    sequences.sumOf { findTerm(NextOrPreceding.PRECEDING, it) }

private fun findTerm(nextOrPreceding: NextOrPreceding, sequence: List<Int>): Int {
    val allSequences = mutableListOf(sequence)
    while (!allSequences.last().allEqual()) {
        allSequences.add(allSequences.last().windowed(2).map { it[1] - it[0] })
    }
    return if (nextOrPreceding == NextOrPreceding.NEXT) {
        allSequences.sumOf { it.last() }
    } else {
        allSequences.reversed().fold(0) { acc, s -> s.first() - acc }
    }
}

enum class NextOrPreceding { NEXT, PRECEDING }