fun readFile(name: String) = object {}::class.java.classLoader.getResource(name)!!.readText().trimEnd()

fun readFileAsLines(name: String) = readFile(name).lines()

data class Point2D(val x: Int, val y: Int)

class Grid<T>(private val points: Map<Point2D, T>) : Map<Point2D, T> by points {
    private val highestX = points.keys.maxOf { it.x }
    private val highestY = points.keys.maxOf { it.y }

    override fun toString() = buildString {
        for (y in 0..highestY) {
            for (x in 0..highestX) {
                append(points[Point2D(x, y)])
            }
            if (y < highestY) appendLine()
        }
    }
}

infix fun IntRange.overlapsWith(other: IntRange) =
    this.intersect(other).isNotEmpty()

fun List<Int>.product() = reduce(Int::times)