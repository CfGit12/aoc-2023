import kotlin.math.min

private val input = readFile("13.txt").split("\n\n").map { s ->
    val matrix = s.lines()
    val transposed = matrix.transposed()
    matrix to transposed
}

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = calculateSummary(0)

private fun part2() = calculateSummary(1)

private fun calculateSummary(differences: Int) =
    input.sumOf { matrices ->
        matrices.first.getReflectionIndex(differences)?.let {
            it * 100
        } ?: matrices.second.getReflectionIndex(differences)!!
    }

private fun List<String>.getReflectionIndex(differences: Int) =
    (1..<this.size).firstOrNull { i ->
        val numBefore = min(i, this.size - i)
        val numAfter = min(numBefore, this.size - 1)
        val before = this.subList(i - numBefore, i).reversed()
        val after = this.subList(i, i + numAfter)
        before.zip(after).sumOf { noOfDifferencesBetween(it.first, it.second) } == differences
    }

private fun noOfDifferencesBetween(s1: String, s2: String) =
    s1.zip(s2).count { it.first != it.second }


private fun List<String>.transposed(): List<String> {
    val cols = this[0].length
    val rows = this.size
    return List(cols) { j ->
        List(rows) { i ->
            this[i][j]
        }.joinToString("")
    }
}