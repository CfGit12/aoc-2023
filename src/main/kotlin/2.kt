private val games = readFileAsLines("2.txt")
    .map { line ->
        val (gameStr, roundsStr) = line.split(":")
        val gameId = """Game (\d+)""".toRegex().find(gameStr)!!.groupValues[1].toInt()
        val rounds = buildList {
            roundsStr.split(";").forEach { roundStr ->
                val reds = findForColour("red", roundStr)
                val greens = findForColour("green", roundStr)
                val blues = findForColour("blue", roundStr)
                add(Round(reds, greens, blues))
            }
        }
        Game(gameId, rounds)
    }

private fun findForColour(colour: String, str: String) =
    """(\d+) $colour""".toRegex().find(str)?.groupValues?.getOrNull(1)?.toInt() ?: 0

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    games
        .filter { game -> game.rounds.none { it.red > 12 || it.green > 13 || it.blue > 14 } }
        .sumOf { it.gameId }

private fun part2() =
    games
        .sumOf { game ->
            val minRed = game.rounds.maxOf { it.red }
            val minGreen = game.rounds.maxOf { it.green }
            val minBlue = game.rounds.maxOf { it.blue }
            minRed * minGreen * minBlue
        }

private data class Game(val gameId: Int, val rounds: List<Round>)
private data class Round(val red: Int, val green: Int, val blue: Int)