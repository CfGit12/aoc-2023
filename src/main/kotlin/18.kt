import kotlin.math.absoluteValue

private val input = readFileAsLines("18.txt")

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = calculateArea(parseInput(simple = true))

private fun part2() = calculateArea(parseInput(simple = false))

private fun parseInput(simple: Boolean): List<Instruction> =
    if (simple) {
        input.map { line ->
            val (dir, num, _) = line.split(" ")
            Instruction(dir.toDirection(), num.toInt())
        }
    } else {
        input.map { line ->
            val col = line.substringAfter("(#").substringBefore(")")
            val dir = col.last().toString().toDirection()
            val amount = col.take(5).toInt(radix = 16)
            Instruction(dir, amount)
        }
    }

private fun calculateArea(instructions: List<Instruction>): Long {
    val instructionsSize = instructions.sumOf { it.amount.toLong() }
    return instructions.fold(0L to 0L) { (sum, d), instruction ->
        when (instruction.direction) {
            Direction.NORTH -> sum to d + instruction.amount
            Direction.SOUTH -> sum to d - instruction.amount
            Direction.WEST -> sum - (d * instruction.amount) to d
            Direction.EAST -> sum + (d * instruction.amount) to d
        }
    }.first.absoluteValue - (instructionsSize / 2) + 1 + instructionsSize
}

private data class Instruction(val direction: Direction, val amount: Int)

private fun String.toDirection() =
    when (this) {
        "U", "3" -> Direction.NORTH;
        "D", "1" -> Direction.SOUTH;
        "L", "2" -> Direction.WEST;
        "R", "0" -> Direction.EAST;
        else -> error("unknown char")
    }