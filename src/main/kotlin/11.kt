import kotlin.math.abs

private val grid = Grid.fromFile("11.txt") { it }
private val emptyX = (0..grid.maxOf { it.key.x }).filter { i -> grid.entries.none { it.key.x == i && it.value == '#' } }
private val emptyY = (0..grid.maxOf { it.key.y }).filter { i -> grid.entries.none { it.key.y == i && it.value == '#' } }

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = calculateTotalDistanceBetweenGalaxies(1)

private fun part2() = calculateTotalDistanceBetweenGalaxies(999_999)

private fun calculateTotalDistanceBetweenGalaxies(scaleFactor: Int) =
    grid.filterValues { it == '#' }.keys.toList()
        .allPermutations2()
        .sumOf { manhattanDistanceBetween(it.first, it.second, scaleFactor) }

private fun manhattanDistanceBetween(first: Point2D, other: Point2D, sf: Int): Long {
    val xSf = emptyX.count { it in (first.x between other.x) } * sf
    val ySf = emptyY.count { it in (first.y between other.y) } * sf
    return abs(first.x - other.x).toLong() + abs(first.y - other.y) + xSf + ySf
}

private fun List<Point2D>.allPermutations2(): List<Pair<Point2D, Point2D>> =
    (0..this.size - 2).flatMap { i -> (i + 1..<this.size).map { j -> this[i] to this[j] } }
