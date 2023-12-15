private val input = readFileAsLines("14.txt")

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    input.tiltNorth().loadScore()

private fun part2(): Int {
    var board = input
    var loadScore = 0

    val cache = mutableMapOf<Int, List<String>>()
    val scoreCache = mutableMapOf<Int, Int>()

    fun tiltAndRotate360() {
        val existing = cache[board.hashCode()]
        if (existing != null) {
            board = existing
        } else {
            val hashcode = board.hashCode()
            repeat(4) {
                board = board.tiltNorth()
                board = board.rotate()
            }
            cache[hashcode] = board
        }
    }

    repeat(1_000_000_000) {
        tiltAndRotate360()
        loadScore =
            scoreCache.getOrPut(board.hashCode()) { board.loadScore() }

    }

    return loadScore
}

private fun List<String>.tiltNorth(): List<String> {
    val updatedGrid = this.map { it.toMutableList() }

    fun furthestClearFrom(y: Int, x: Int): Int? =
        (y - 1 downTo 0).takeWhile { updatedGrid[it][x] == '.' }.lastOrNull()

    for (y in 1..<this.size) for (x in 0..<this[1].length) {
        if (updatedGrid[y][x] == 'O') {
            furthestClearFrom(y, x)?.let { newY ->
                updatedGrid[newY][x] = 'O'
                updatedGrid[y][x] = '.'
            }
        }
    }


    return updatedGrid.map { it.joinToString("") }
}

private fun List<String>.loadScore(): Int {
    var count = 0
    for (y in this.indices) {
        for (x in 0..<this[1].length) {
            if (this[y][x] == 'O') {
                count += this.size - y
            }
        }
    }
    return count
}

private fun List<String>.print() = forEach { println(it) }

private fun List<String>.rotate() =
    List(this.size) { i ->
        List(this[0].length) { j ->
            this[this.size - 1 - j][i]
        }.joinToString("")
    }