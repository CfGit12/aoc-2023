private val sequences = readFileAsLines("9.txt").map { line ->
    line.split(" ").map { it.toInt() }
}

fun main() {
    println(sequences)
    println(part1())
    println(part2())
}

private fun part1() =
    sequences.sumOf { findTerm(it) }

private fun part2() =
    sequences.map { it.reversed() }.sumOf { findTerm(it) }

private fun findTerm(sequence: List<Int>): Int {
    val allSequences = mutableListOf(sequence)
    while (!allSequences.last().allEqual()) {
        allSequences.add(allSequences.last().windowed(2).map { it[1] - it[0] })
    }
    return allSequences.sumOf { it.last() }
}