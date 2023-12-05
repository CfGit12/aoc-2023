val nutrientsMap = readFile("5.txt").let { file ->
    val seeds = file.substringAfter("seeds: ").substringBefore("\n").split(" ").map { it.toLong() }

    fun getNutrientsMap(name: String) =
        file.substringAfter("$name\n").lines().takeWhile { it.length > 1 }.map { line ->
            val (destination, source, range) = line.split(" ").map { it.toLong() }
            NutrientsMap(destination, source, range)
        }

    Nutrients(
        seeds = seeds,
        seedToSoilMaps = getNutrientsMap("seed-to-soil map:"),
        soilToFertilizerMaps = getNutrientsMap("soil-to-fertilizer map:"),
        fertilizerToWaterMaps = getNutrientsMap("fertilizer-to-water map:"),
        waterToLightMaps = getNutrientsMap("water-to-light map:"),
        lightToTemperatureMaps = getNutrientsMap("light-to-temperature map:"),
        temperatureToHumidityMaps = getNutrientsMap("temperature-to-humidity map:"),
        humidityToLocationMaps = getNutrientsMap("humidity-to-location map:"),
    )
}

fun main() {
    println(part1())
    println(part2())
}

private fun part1() =
    nutrientsMap.getMinLocationPart1()

private fun part2() =
    nutrientsMap.getMinLocationPart2()

data class Nutrients(
    val seeds: List<Long>,
    val seedToSoilMaps: List<NutrientsMap>,
    val soilToFertilizerMaps: List<NutrientsMap>,
    val fertilizerToWaterMaps: List<NutrientsMap>,
    val waterToLightMaps: List<NutrientsMap>,
    val lightToTemperatureMaps: List<NutrientsMap>,
    val temperatureToHumidityMaps: List<NutrientsMap>,
    val humidityToLocationMaps: List<NutrientsMap>
) {
    fun getMinLocationPart1() =
        seeds.minOf { getSeedLocation(it) }

    fun getMinLocationPart2() =
        seeds
            .chunked(2)
            .map { it[0]..<it[0] + it[1] }
            .minOf { range -> range.asSequence().map { getSeedLocation(it) }.min() }

    private fun getSeedLocation(seed: Long): Long {
        val maps = listOf(
            seedToSoilMaps, soilToFertilizerMaps, fertilizerToWaterMaps, waterToLightMaps,
            lightToTemperatureMaps, temperatureToHumidityMaps, humidityToLocationMaps
        )

        val locationNumber = maps.fold(seed) { acc, nutrientsMaps ->
            nutrientsMaps.firstNotNullOfOrNull { it.getDestinationFromSource(acc) } ?: acc
        }
        return locationNumber
    }
}

class NutrientsMap(destinationStart: Long, sourceStart: Long, range: Long) {
    private val destinationRange = destinationStart..<destinationStart + range
    private val sourceRange = sourceStart..<sourceStart + range

    fun getDestinationFromSource(source: Long): Long? =
        if (source in sourceRange) destinationRange.first + source - sourceRange.first else null

    override fun toString() = "($destinationRange $sourceRange)"
}