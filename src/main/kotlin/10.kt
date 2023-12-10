import Direction.*
import kotlin.math.absoluteValue

private val pipeGrid = Grid.fromLines(readFileAsLines("10.txt")) { it.toGridLocation() }

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = getLoopDirections().size / 2

private fun part2(): Int {
    val mainLoop = getLoopDirections()
    return getLoopDirections().fold(0 to 0) { (sum, d), move ->
        when (move) {
            NORTH -> sum to d + 1
            SOUTH -> sum to d - 1
            WEST -> sum - d to d
            EAST -> sum + d to d
        }
    }.first.absoluteValue - (mainLoop.size / 2) + 1
}


private fun getLoopDirections(): List<Direction> {
    val directions = mutableListOf<Direction>()
    var currentPosition = pipeGrid.entries.first { it.value == Start }.key
    var currentDirection = NORTH // EAST for sample NORTH for real
    var steps = 0
    do {
        directions.add(currentDirection)
        currentPosition += currentDirection
        val gridLocation = pipeGrid[currentPosition]
        if (gridLocation is Pipe) {
            currentDirection = (pipeGrid[currentPosition] as Pipe).exitsFrom(currentDirection)
        }
        steps++
    } while (pipeGrid[currentPosition] != Start)

    return directions
}

private fun Char.toGridLocation() =
    when (this) {
        'S' -> Start
        '.' -> Ground
        '|' -> VerticalPipe
        '-' -> HorizontalPipe
        'L' -> LPipe
        'J' -> JPipe
        '7' -> SevenPipe
        'F' -> FPipe
        else -> error("unknown grid symbol")
    }

private sealed interface GridLocation
private object Ground : GridLocation {
    override fun toString() = "."
}

private object Start : GridLocation {
    override fun toString() = "S"
}

private sealed class Pipe(val directions: Pair<Direction, Direction>) : GridLocation {
    fun exitsFrom(direction: Direction) =
        directions.toList().first { it != direction.opposite() }
}

private object VerticalPipe : Pipe(NORTH to SOUTH) {
    override fun toString() = "|"
}

private object HorizontalPipe : Pipe(EAST to WEST) {
    override fun toString() = "-"
}

private object LPipe : Pipe(NORTH to EAST) {
    override fun toString() = "L"
}

private object JPipe : Pipe(NORTH to WEST) {
    override fun toString() = "J"
}

private object SevenPipe : Pipe(SOUTH to WEST) {
    override fun toString() = "7"
}

private object FPipe : Pipe(SOUTH to EAST) {
    override fun toString() = "F"
}


