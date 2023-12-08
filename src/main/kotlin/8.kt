private val input = readFileAsLines("8.txt")
private val instructions = input.first()
private val nodes =
    input.drop(2)
        .associate { line ->
            line.substring(0, 3) to (line.substring(7, 10) to line.substring(12, 15))
        }

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = countSteps("AAA")

private fun part2() =
    nodes.keys.filter { it.endsWith('A') }
        .map { countSteps(it).toLong() }
        .reduce { acc, i -> lcm(acc, i) }

private fun countSteps(startingNode: String): Int {
    var steps = 0
    var currentNode = startingNode
    
    while (!currentNode.endsWith("Z")) {
        val instruction = instructions[steps % instructions.length]
        steps++
        currentNode = if (instruction == 'L') nodes[currentNode]!!.first else nodes[currentNode]!!.second
    }
    return steps
}
