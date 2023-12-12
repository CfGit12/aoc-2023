private val input = readFileAsLines("12.txt").map { line ->
    val (springString, numbers) = line.split(" ")
    springString to numbers.split(",").map { it.toInt() }
}

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    input.sumOf { (wildcardString, counts) ->
        wildcardString.substituteWildcards().count { it.satisfiesSpringCounts(counts) }
    }

private fun part2() =
    input.map { it.unfold() }.sumOf { (wildcardString, counts) ->
        arrangements(wildcardString, counts)
    }

private fun String.toContiguousSprings() = trim('.').split("\\.+".toRegex())

private fun String.satisfiesSpringCounts(counts: List<Int>) = toContiguousSprings().map { it.length } == counts

private fun String.substituteWildcards(): List<String> {
    val strings: MutableList<MutableList<Char>> = mutableListOf(mutableListOf())
    for (c in this) {
        if (c == '.' || c == '#') strings.forEach { it.add(c) }
        else {
            strings.addAll(strings.map { it.toMutableList() })
            strings.forEachIndexed { index, chars ->
                if (index < strings.size / 2) chars.add('.') else chars.add('#')
            }
        }
    }
    return strings.map { it.joinToString("") }
}

private fun Pair<String, List<Int>>.unfold(): Pair<String, List<Int>> =
    "$first?".repeat(5).dropLast(1) to List(5) { second }.flatten()

// Nicked from Kotlin slack - Kingsley Adio
private fun arrangements(spread: String, compact: List<Int>, cache: MutableMap<String, Long> = hashMapOf()): Long {
    var count = 0L
    for (i in spread.indices) {
        if (i > 0 && spread[i - 1] == '#') return count
        val char = spread[i]
        if (char == '.') continue
        if (compact.isEmpty() && char == '#') return 0
        if (compact.isEmpty()) continue
        val matchSize = compact.first()
        if (i + matchSize > spread.length) continue
        val spreadEquivalent = spread.substring(i, i + matchSize)
        val isMatch = spreadEquivalent.all { it != '.' }
        val hasDelimiter = i + matchSize == spread.length || spread[i + matchSize] != '#'
        if (isMatch) count += when {
            i + matchSize > spread.lastIndex -> arrangements("", compact.drop(1), cache)
            hasDelimiter -> {
                val next = spread.substring(i + matchSize + 1)
                val nextCompact = compact.drop(1)
                val key = "$next-${nextCompact.hashCode()}"
                cache.getOrPut(key) { arrangements(next, nextCompact, cache) }
            }

            else -> 0
        }
    }
    return if (compact.isEmpty()) 1 else count
}

/**
 * ??? = ###,##., #.#,
 *
 *
 * ???.### [1, 1, 3]
 * .??.### = [1 or 2, 3] - Can't continue (??. = (1,2)) - Count would be wrong
 * #??.### = [1, 3] - Can continue
 * ##?.###
 *
 *
 * ??? = 3 or 2 or 1,1 or 1
 *
 * ??..??...?## = 0 or 1 or 2 AND 0 or 1 or 2 AND 2 or 3 - Target [1, 1, 3]
 *
 *
 *
 */