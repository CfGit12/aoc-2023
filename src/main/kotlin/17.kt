private val grid = Grid.fromFile("17.txt") { it.digitToInt() }

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = shortestPath(minMoves = 1, maxMoves = 3)

private fun part2() = shortestPath(minMoves = 4, maxMoves = 10)

private data class State(
    val point2D: Point2D,
    val direction: Direction,
    val moves: Int,
    val heatLoss: Int
)

private fun shortestPath(minMoves: Int, maxMoves: Int): Int {
    val startPoint = Point2D(0, 0)
    val visited = mutableSetOf(
        Triple(startPoint, Direction.EAST, 1),
        Triple(startPoint, Direction.SOUTH, 1)
    )
    val queue = mutableListOf(
        State(startPoint, Direction.EAST, 1, 0),
        State(startPoint, Direction.SOUTH, 1, 0)
    )

    fun add(state: State) {
        if (visited.add(Triple(state.point2D, state.direction, state.moves))) {
            queue.add(State(state.point2D, state.direction, state.moves, state.heatLoss))
        }
    }

    while (queue.isNotEmpty()) {
        val state = queue.minBy { it.heatLoss }
        val nextPoint = state.point2D + state.direction

        if (grid.point2DInGrid(nextPoint)) {
            val nextHeatLoss = state.heatLoss + grid[nextPoint]!!

            if (nextPoint == Point2D(grid.highestX, grid.highestY)) return nextHeatLoss

            if (state.moves < maxMoves) {
                add(State(nextPoint, state.direction, state.moves + 1, nextHeatLoss))
            }
            if (state.moves >= minMoves) {
                state.direction.perpendicularFrom().forEach { nextDirection ->
                    add(State(nextPoint, nextDirection, 1, nextHeatLoss))
                }
            }
        }
        queue.remove(state)
    }
    error("didn't find heat loss")
}
