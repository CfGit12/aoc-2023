fun readFile(name: String) = object {}::class.java.classLoader.getResource(name)!!.readText().trimEnd()

fun readFileAsLines(name: String) = readFile(name).lines()

data class Point2D(val x: Int, val y: Int)

fun Point2D.surroundingPoints() = listOf(
    copy(x = x + 1), copy(x = x - 1), copy(y = y + 1), copy(y = y - 1)
)

operator fun Point2D.plus(direction: Direction) =
    when (direction) {
        Direction.NORTH -> copy(y = y - 1)
        Direction.EAST -> copy(x = x + 1)
        Direction.SOUTH -> copy(y = y + 1)
        Direction.WEST -> copy(x = x - 1)
    }

enum class Direction { NORTH, EAST, SOUTH, WEST }

fun Direction.perpendicularFrom() =
    when (this) {
        Direction.NORTH -> listOf(Direction.WEST, Direction.EAST)
        Direction.SOUTH -> listOf(Direction.EAST, Direction.WEST)
        Direction.EAST -> listOf(Direction.NORTH, Direction.SOUTH)
        Direction.WEST -> listOf(Direction.SOUTH, Direction.NORTH)
    }

fun Direction.opposite() =
    when (this) {
        Direction.NORTH -> Direction.SOUTH
        Direction.EAST -> Direction.WEST
        Direction.SOUTH -> Direction.NORTH
        Direction.WEST -> Direction.EAST
    }

class Grid<T>(private val points: Map<Point2D, T>) : Map<Point2D, T> by points {
    val highestX = points.keys.maxOf { it.x }
    val highestY = points.keys.maxOf { it.y }

    fun point2DInGrid(point2D: Point2D) =
        point2D.x in 0..highestX && point2D.y in 0..highestY

    fun getSurroundingPoints(point2D: Point2D) =
        point2D.surroundingPoints().filter { point2DInGrid(it) }

    fun toStringWithOnly(restrictedPoints: Set<Point2D>) = buildString {
        for (y in 0..highestY) {
            for (x in 0..highestX) {
                if (Point2D(x, y) in restrictedPoints) append(points[Point2D(x, y)]) else append(" ")
            }
            if (y < highestY) appendLine()
        }
    }

    override fun toString() = buildString {
        for (y in 0..highestY) {
            for (x in 0..highestX) {
                append(points[Point2D(x, y)])
            }
            if (y < highestY) appendLine()
        }
    }

    companion object {
        fun <T> fromFile(fileName: String, transformFn: (Char) -> T) =
            fromLines(readFileAsLines(fileName), transformFn)

        fun <T> fromLines(lines: List<String>, transformFn: (Char) -> T): Grid<T> =
            Grid(
                buildMap {
                    lines.mapIndexed { y, line ->
                        line.mapIndexed { x, c ->
                            put(Point2D(x, y), transformFn(c))
                        }
                    }
                }
            )
    }
}

infix fun IntRange.overlapsWith(other: IntRange) =
    this.intersect(other).isNotEmpty()

fun List<Int>.product() = reduce(Int::times)

fun lcm(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm < maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) return lcm
        lcm += larger
    }
    return maxLcm
}

fun <T> List<T>.allEqual() = all { it == this[0] }

infix fun Int.between(other: Int): IntProgression =
    if (this <= other) this..other else this downTo other

fun List<String>.transposed(): List<String> {
    val cols = this[0].length
    val rows = this.size
    return List(cols) { j ->
        List(rows) { i ->
            this[i][j]
        }.joinToString("")
    }
}

fun String.substringBetween(a: String, b: String) = substringAfter(a).substringBefore(b)