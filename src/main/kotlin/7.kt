private fun parseHands(jacksWild: Boolean) =
    readFileAsLines("7.txt").map {
        val (hand, bid) = it.split(" ")
        HandAndBid(hand, bid.toInt(), jacksWild)
    }

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = doIt(jacksWild = false)
private fun part2() = doIt(jacksWild = true)

private fun doIt(jacksWild: Boolean) =
    parseHands(jacksWild)
        .sorted()
        .mapIndexed { index, handAndBid -> handAndBid.bid.toLong() * (index + 1) }
        .sum()

class HandAndBid(
    private val cards: String,
    val bid: Int,
    private val jacksWild: Boolean
) : Comparable<HandAndBid> {

    override fun toString() = "$cards - $bid - $cardCounts"

    private val cardCounts =
        if (jacksWild) {
            listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
                .map { originalHand ->
                    val newHand = cards.replace('J', originalHand)
                    newHand.cardCounts()
                }
                .sortedWith(compareBy({ it[0] }, { it.getOrNull(1) }))
                .last()
        } else {
            cards.cardCounts()
        }

    private fun String.cardCounts() = groupingBy { it }.eachCount().values.sortedDescending()

    private fun Char.toRank() = when {
        this == 'T' -> 10
        this == 'J' && !jacksWild -> 11
        this == 'J' && jacksWild -> 1
        this == 'Q' -> 12
        this == 'K' -> 13
        this == 'A' -> 14
        else -> digitToInt()
    }

    override fun compareTo(other: HandAndBid) = compareValuesBy(
        this,
        other,
        { it.cardCounts[0] },
        { it.cardCounts.getOrNull(1) },
        { it.cards[0].toRank() },
        { it.cards[1].toRank() },
        { it.cards[2].toRank() },
        { it.cards[3].toRank() },
        { it.cards[4].toRank() }
    )
}