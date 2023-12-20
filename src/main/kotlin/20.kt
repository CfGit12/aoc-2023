private val input = readFileAsLines("20-sample-2.txt")

private fun parseInput() = input.map { line ->
    val (left, right) = line.split(" -> ")
    val destinations = right.split(", ")
    when {
        left.startsWith("%") -> FlipFlop(left.substring(1), destinations)
        left.startsWith("&") -> Conjunction(left.substring(1), destinations)
        else -> Broadcaster("broadcaster", destinations)
    }
}

fun main() {
    println(part1())
    println(part2())
}

private fun part1(): Long {
    val modules = parseInput()
    val modulesMap = modules.associateBy { it.label }

    val queue = mutableListOf<Triple<String, Pulse, String>>()

    var lowPulses = 0
    var highPulses = 0

    fun pushButton() = queue.add(Triple("pushButton", Pulse.LOW, "broadcaster"))
    var buttonPushes = 0
    do {
        pushButton()
        buttonPushes++
        while (queue.isNotEmpty()) {
            val (source, pulse, destination) = queue.removeFirst()
            if (pulse == Pulse.LOW) lowPulses++ else highPulses++

            modulesMap[destination]?.let { module ->


                val receivedPulses = module.receivePulse(pulse, source)
                receivedPulses?.let { (newPulse, newDestinations) ->
                    queue.addAll(newDestinations.map { newDestination ->
                        Triple(
                            destination,
                            newPulse,
                            newDestination
                        )
                    })
                }
            }

        }
    } while (buttonPushes < 1000)

    return lowPulses.toLong() * highPulses
}

private fun part2() = 0


private sealed class Module {
    abstract val label: String
    abstract val destinations: List<String>

    abstract fun receivePulse(pulse: Pulse, whereFrom: String): Pair<Pulse, List<String>>?
}

private data class Broadcaster(override val label: String, override val destinations: List<String>) : Module() {
    override fun receivePulse(pulse: Pulse, whereFrom: String) =
        pulse to destinations
}

private data class FlipFlop(override val label: String, override val destinations: List<String>) : Module() {
    var isOn = false

    override fun receivePulse(pulse: Pulse, whereFrom: String) =
        if (pulse == Pulse.HIGH) null
        else {
            val pulseToEmit = if (isOn) Pulse.LOW else Pulse.HIGH
            isOn = !isOn
            pulseToEmit to destinations
        }
}

private data class Conjunction(override val label: String, override val destinations: List<String>) : Module() {
    //val memory = destinations.associateBy({ it }, { Pulse.LOW }).toMutableMap()
    val memory = mutableMapOf<String, Pulse>()


    override fun receivePulse(pulse: Pulse, whereFrom: String): Pair<Pulse, List<String>>? {
        memory[whereFrom] = pulse
        val pulseToEmit = if (memory.values.all { it == Pulse.HIGH }) Pulse.LOW else Pulse.HIGH
        return pulseToEmit to destinations
    }
}

private enum class Pulse { HIGH, LOW }