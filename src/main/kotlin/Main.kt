import java.io.File

data class Point(val x: Int, val y: Int)

data class Map(val width: Int, val height: Int) {

    private var map: Array<IntArray> = Array(this.height) { IntArray(width) }

    fun markLine(p1: Point, p2: Point) {
        // Since in this particular case, the line is perfectly diagonal 45degree, we can safely assume
        // that the diff of xs and ys are the same.
        val stepX = if (p1.x == p2.x) 0 else if (p1.x > p2.x) -1 else 1
        val stepY = if (p1.y == p2.y) 0 else if (p1.y > p2.y) -1 else 1
        var currX = p1.x
        var currY = p1.y
        while (currX != p2.x || currY != p2.y) {
            map[currY][currX] += 1
            currX += stepX
            currY += stepY
        }

        // Do for the last point
        map[currY][currX] += 1
    }

    fun countDangerous(): Int {
        return this.map.flatMap { it.toList() }.count { it >= 2 }
    }
}

fun solvePart1(rows: Int, cols: Int, points: List<Pair<Point, Point>>) {
    val map = Map(width = cols, height = rows)
    points.asSequence().filter { it.first.x == it.second.x || it.first.y == it.second.y }
        .forEach { map.markLine(it.first, it.second) }
    val dangerousPoints = map.countDangerous()

    println("PART 1 ANSWER")
    println(dangerousPoints)
}

fun solvePart2(rows: Int, cols: Int, points: List<Pair<Point, Point>>) {
    val map = Map(width = cols, height = rows)
    points.forEach { map.markLine(it.first, it.second) }
    val dangerousPoints = map.countDangerous()

    println("PART 2 ANSWER")
    println(dangerousPoints)
}

fun main(args: Array<String>) {
    val coordinates = File("input.txt").readLines()

    // Parse each line of input to get the 2 points.
    // At the same time, we need to get the maximum number of rows and cols.
    val points = mutableListOf<Pair<Point, Point>>()
    var maxRows = -1
    var maxCols = -1
    coordinates.forEach { coordinate ->
        // Split by the arrow ( -> )
        val input = coordinate.split(" -> ")
        val (x1, y1) = input[0].split(",").map { it.toInt() }
        val (x2, y2) = input[1].split(",").map { it.toInt() }

        // Determine the maximum rows and cols.
        maxRows = listOf(y1, y2, maxRows).maxOrNull()!! + 1
        maxCols = listOf(x1, x2, maxCols).maxOrNull()!! + 1

        points.add(element = Pair(first = Point(x = x1, y = y1), second = Point(x = x2, y = y2)))
    }

    solvePart1(rows = maxRows, cols = maxCols, points = points)
    solvePart2(rows = maxRows, cols = maxCols, points = points)
}