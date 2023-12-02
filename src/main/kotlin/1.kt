private val input = readFileAsLines("1.txt")

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = sumOfDigits(false)
private fun part2(): Int = sumOfDigits(true)

private fun sumOfDigits(includeWords: Boolean) =
    input.sumOf {
        val digits = it.toDigits(includeWords)
        val first = digits.first()
        val last = digits.last()
        "$first$last".toInt()
    }

private fun String.toDigits(includeWords: Boolean): List<Int> {
    val digits = mutableListOf<Int>()

    for (i in indices) {
        if (this[i].isDigit()) digits.add(this[i].digitToInt())

        if (includeWords) {
            when {
                substring(i).startsWith("one") -> digits.add(1)
                substring(i).startsWith("two") -> digits.add(2)
                substring(i).startsWith("three") -> digits.add(3)
                substring(i).startsWith("four") -> digits.add(4)
                substring(i).startsWith("five") -> digits.add(5)
                substring(i).startsWith("six") -> digits.add(6)
                substring(i).startsWith("seven") -> digits.add(7)
                substring(i).startsWith("eight") -> digits.add(8)
                substring(i).startsWith("nine") -> digits.add(9)

            }
        }

    }
    return digits
}