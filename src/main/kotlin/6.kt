private val sampleInput = listOf(7 to 9, 15 to 40, 30 to 200)
private val sampleInput2 = 71530L to 940200L
private val input = listOf(55 to 401, 99 to 1485, 97 to 2274, 93 to 1405)
private val input2 = 55999793L to 401148522741405

fun main() {
    println(part1())
    println(part2())
}

private fun part1() = input
    .map {
        (1..<it.first).count { holdTime -> holdTime * (it.first - holdTime) > it.second }
    }.product()

private fun part2() = input2
    .let {
        (1..<it.first).count { holdTime -> holdTime * (it.first - holdTime) > it.second }
    }