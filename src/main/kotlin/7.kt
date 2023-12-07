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

fun doIt(jacksWild: Boolean) =
    parseHands(jacksWild)
        .sorted()
        .mapIndexed { index, handAndBid -> handAndBid.bid.toLong() * (index + 1) }
        .sum()

class HandAndBid(
    private val cards: String,
    val bid: Int,
    private val jacksWild: Boolean
) : Comparable<HandAndBid> {

    override fun toString() = "$cards - $bid - $type"

    private val type = getType()

    private fun getType(): HandType =
        if (jacksWild && cards.contains('J')) {
            val withoutJacks = cards.filter { it != 'J' }
            val jacksCount = cards.length - withoutJacks.length
            when (jacksCount) {
                5 -> HandType.FIVE_OF_A_KIND
                4 -> HandType.FIVE_OF_A_KIND
                3 -> if (withoutJacks.isPair()) HandType.FIVE_OF_A_KIND else HandType.FOUR_OF_A_KIND
                2 -> {
                    when {
                        withoutJacks.isThreeOfAKind() -> HandType.FIVE_OF_A_KIND
                        withoutJacks.isPair() -> HandType.FOUR_OF_A_KIND
                        else -> HandType.THREE_OF_A_KIND
                    }
                }

                1 -> {
                    when {
                        withoutJacks.isFourOfAKind() -> HandType.FIVE_OF_A_KIND
                        withoutJacks.isThreeOfAKind() -> HandType.FOUR_OF_A_KIND
                        withoutJacks.isTwoPair() -> HandType.FULL_HOUSE
                        withoutJacks.isPair() -> HandType.THREE_OF_A_KIND
                        else -> HandType.PAIR
                    }
                }

                else -> error("not possible")
            }
        } else {
            when {
                cards.isFiveOfAKind() -> HandType.FIVE_OF_A_KIND
                cards.isFourOfAKind() -> HandType.FOUR_OF_A_KIND
                cards.isFullHouse() -> HandType.FULL_HOUSE
                cards.isThreeOfAKind() -> HandType.THREE_OF_A_KIND
                cards.isTwoPair() -> HandType.TWO_PAIR
                cards.isPair() -> HandType.PAIR
                else -> HandType.HIGH_CARD
            }
        }

    private fun String.isFiveOfAKind() =
        any { cardCount(it) == 5 }

    private fun String.isFourOfAKind() =
        any { cardCount(it) == 4 }

    private fun String.isFullHouse() =
        any { cardCount(it) == 3 } && any { cardCount(it) == 2 }

    private fun String.isThreeOfAKind() =
        any { cardCount(it) == 3 }

    private fun String.isTwoPair() =
        count { cardCount(it) == 2 } == 4

    private fun String.isPair() =
        any { cardCount(it) == 2 }

    private fun String.cardCount(card: Char) = count { it == card }

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
        { it.type },
        { it.cards[0].toRank() },
        { it.cards[1].toRank() },
        { it.cards[2].toRank() },
        { it.cards[3].toRank() },
        { it.cards[4].toRank() }
    )

}

private enum class HandType {
    HIGH_CARD, PAIR, TWO_PAIR, THREE_OF_A_KIND, FULL_HOUSE, FOUR_OF_A_KIND, FIVE_OF_A_KIND
}