private val grid = Grid.fromFile("16.txt") { it.toTile() }

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    fireLightBeam(Point2D(0, 0) to Direction.EAST)

private fun part2(): Int {
    var highest = 0

    fun fire(placeToFireFrom: Pair<Point2D, Direction>) {
        val result = fireLightBeam(placeToFireFrom)
        if (result > highest) highest = result
    }

    for (i in 0..grid.highestX) {
        fire(Point2D(0, i) to Direction.EAST)
        fire(Point2D(grid.highestX, i) to Direction.WEST)
        fire(Point2D(i, 0) to Direction.SOUTH)
        fire(Point2D(i, grid.highestY) to Direction.NORTH)
    }

    return highest
}

private fun fireLightBeam(placeToFireFrom: Pair<Point2D, Direction>): Int {
    val placesFired = mutableSetOf<Pair<Point2D, Direction>>()

    fun inner(placeToFire: Pair<Point2D, Direction>) {
        if (placeToFire in placesFired) return

        placesFired.add(placeToFire)

        grid[placeToFire.first]!!.reflectLight(placeToFire.second).forEach { newDir ->
            val newPoint2D = placeToFire.first + newDir
            if (grid.point2DInGrid(newPoint2D)) {
                inner(newPoint2D to newDir)
            }
        }
    }

    inner(placeToFireFrom)

    return placesFired.map { it.first }.toSet().size
}

private sealed class Tile {
    abstract fun reflectLight(from: Direction): List<Direction>
}

private object EmptySpace : Tile() {
    override fun reflectLight(from: Direction) = listOf(from)
    override fun toString() = "."
}

private object HorizontalSplitter : Tile() {
    override fun reflectLight(from: Direction) =
        when (from) {
            Direction.WEST, Direction.EAST -> listOf(from)
            Direction.NORTH, Direction.SOUTH -> listOf(Direction.WEST, Direction.EAST)
        }

    override fun toString() = "-"
}

private object VerticalSplitter : Tile() {
    override fun reflectLight(from: Direction) =
        when (from) {
            Direction.WEST, Direction.EAST -> listOf(Direction.NORTH, Direction.SOUTH)
            Direction.NORTH, Direction.SOUTH -> listOf(from)
        }

    override fun toString() = "|"
}

private object BackSlashMirror : Tile() {
    override fun reflectLight(from: Direction) =
        when (from) {
            Direction.NORTH -> listOf(Direction.WEST)
            Direction.EAST -> listOf(Direction.SOUTH)
            Direction.SOUTH -> listOf(Direction.EAST)
            Direction.WEST -> listOf(Direction.NORTH)
        }

    override fun toString() = "\\"
}

private object ForwardSlashMirror : Tile() {
    override fun reflectLight(from: Direction) =
        when (from) {
            Direction.NORTH -> listOf(Direction.EAST)
            Direction.EAST -> listOf(Direction.NORTH)
            Direction.SOUTH -> listOf(Direction.WEST)
            Direction.WEST -> listOf(Direction.SOUTH)
        }

    override fun toString() = "/"
}

private fun Char.toTile(): Tile =
    when (this) {
        '.' -> EmptySpace
        '-' -> HorizontalSplitter
        '|' -> VerticalSplitter
        '\\' -> BackSlashMirror
        '/' -> ForwardSlashMirror
        else -> error("")
    }