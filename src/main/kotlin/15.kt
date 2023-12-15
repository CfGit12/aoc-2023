private val input = readFile("15.txt").split(",")

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    input.sumOf { it.hash() }

private fun part2(): Int {
    val boxes = List(256) { mutableMapOf<String, Int>() }

    for (operation in input) {
        val hash = operation.substringBefore("=").substringBefore("-").hash()
        if (operation.contains("=")) {
            val (lens, value) = operation.split("=")
            boxes[hash][lens] = value.toInt()
        } else {
            boxes[hash].remove(operation.substringBefore("-"))
        }
    }

    return boxes.mapIndexed { boxNo, box ->
        (boxNo + 1) * box.entries.mapIndexed { i, (_, focalLength) -> (i + 1) * focalLength }.sum()
    }.sum()
}

private fun String.hash() = fold(0) { value, c ->
    ((value + c.code) * 17) % 256
}
