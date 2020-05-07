package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImplementation(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImplementation(width)

open class SquareBoardImplementation(final override val width: Int) : SquareBoard {
    private val cells: List<Cell> = (1..width).flatMap { i -> (1..width).map { Cell(i, it) } }

    override fun getCellOrNull(i: Int, j: Int): Cell? = cells.find { cell -> cell == Cell(i, j) }

    override fun getCell(i: Int, j: Int): Cell = getCellOrNull(i, j) ?: throw IllegalArgumentException()

    override fun getAllCells(): Collection<Cell> = cells

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> = when {
        (jRange.last > width) -> (jRange.first..width).map { j -> getCell(i, j) }
        (jRange.first > width) -> (width..jRange.last).map { j -> getCell(i, j) }
        else -> jRange.map { j -> getCell(i, j) }
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> = when {
        (iRange.last > width) -> (iRange.first..width).map { i -> getCell(i, j) }
        (iRange.first > width) -> (width..iRange.last).map { i -> getCell(i, j) }
        else -> iRange.map { i -> getCell(i, j) }
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when (direction) {
        UP -> getCellOrNull(i - 1, j)
        DOWN -> getCellOrNull(i + 1, j)
        LEFT -> getCellOrNull(i, j - 1)
        RIGHT -> getCellOrNull(i, j + 1)
    }
}

class GameBoardImplementation<T>(width: Int) : SquareBoardImplementation(width), GameBoard<T> {
    private val cellMap: MutableMap<Cell, T?> = getAllCells().map { Pair(it, null) }.toMap<Cell, T?>().toMutableMap()

    override fun get(cell: Cell): T? = cellMap[cell]

    override fun set(cell: Cell, value: T?) {
        cellMap[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = cellMap.filter { predicate(it.value) }.keys

    override fun find(predicate: (T?) -> Boolean): Cell? = cellMap.entries.find { predicate(it.value) }?.key

    override fun any(predicate: (T?) -> Boolean): Boolean = cellMap.any { predicate(it.value) }

    override fun all(predicate: (T?) -> Boolean): Boolean = cellMap.all { predicate(it.value) }
}

