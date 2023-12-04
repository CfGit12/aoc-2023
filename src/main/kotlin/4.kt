import kotlin.math.pow

private val scratchCards = readFileAsLines("4.txt").map { line ->
    val (gameStr, numbersStr) = line.split(": ")
    val gameId = gameStr.substringAfterLast(" ").toInt()
    val (winningNumbers, yourNumbers) = numbersStr
        .split(" | ").map { numbers ->
            numbers.trim().split("\\s+".toRegex()).map { number -> number.toInt() }
        }
    ScratchCard(gameId, winningNumbers.toSet(), yourNumbers.toSet())
}

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    scratchCards.sumOf { it.score }

private fun part2(): Int {
    val cardCounts = scratchCards.associateBy({ it.cardNumber }, { 1 }).toMutableMap()
    for (scratchCard in scratchCards) {
        val noOfCards = cardCounts[scratchCard.cardNumber]!!
        val noOfMatches = scratchCard.noOfWinningNumbers
        (scratchCard.cardNumber + 1..scratchCard.cardNumber + noOfMatches).forEach { cardNumber ->
            cardCounts[cardNumber] = cardCounts[cardNumber]!! + noOfCards
        }
    }
    return cardCounts.values.sum()
}

private data class ScratchCard(val cardNumber: Int, val winningNumbers: Set<Int>, val yourNumbers: Set<Int>) {

    val noOfWinningNumbers = yourNumbers.intersect(winningNumbers).size
    val score = if (noOfWinningNumbers == 0) 0 else 2.0.pow(noOfWinningNumbers - 1).toInt()

}