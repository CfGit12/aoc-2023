private val grid: Map<Point2D, String> = buildMap {
    val lines = readFileAsLines("3.txt")
    for (y in lines.indices) {
        val sb = StringBuilder()
        for (x in lines[0].indices) {
            val c = lines[y][x]
            if (!c.isDigit() && sb.isNotEmpty()) {
                put(Point2D(x - sb.length, y), sb.toString())
                sb.clear()
            }
            if (c.isDigit()) {
                sb.append(c)
            } else if (c != '.') {
                put(Point2D(x, y), c.toString())
            }
            if (x == lines.indices.last && sb.isNotEmpty()) {
                put(Point2D(x - sb.length + 1, y), sb.toString())
                sb.clear()
            }
        }
    }
}

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = grid
    .filterValues { it.isNumeric() }
    .filterKeys { key -> grid.getSurroundingCoordinates(key).values.any { !it.isNumeric() } }
    .values
    .sumOf { it.toInt() }

private fun String.isNumeric() = toIntOrNull() != null

private fun part2() = grid
    .filterValues { it == "*" }
    .keys.sumOf { point ->
        val surroundingParts = grid.getSurroundingCoordinates(point).filterValues { surr -> surr.isNumeric() }
        if (surroundingParts.size == 2) {
            surroundingParts.values.map { it.toInt() }.product()
        } else {
            0
        }
    }

private fun Map<Point2D, String>.getSurroundingCoordinates(point2D: Point2D): Map<Point2D, String> {
    val xRange = (point2D.x - 1)..(point2D.x + this[point2D]!!.length)
    val yRange = (point2D.y - 1)..(point2D.y + 1)
    return filter { it.getXRange() overlapsWith xRange && it.key.y in yRange }
}

private fun Map.Entry<Point2D, String>.getXRange() =
    if (!value.isNumeric()) key.x..key.x
    else key.x..<key.x + value.length