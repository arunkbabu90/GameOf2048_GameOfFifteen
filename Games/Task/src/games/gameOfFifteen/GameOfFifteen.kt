package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.createGameBoard
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game = GameOfFifteen(initializer)


class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private val board = createGameBoard<Int?>(4)

    override fun initialize() {
        board.addNewValue(initializer)
    }

    override fun canMove(): Boolean = board.any { it == null }

    override fun hasWon(): Boolean {
        var isWon = true
        val bounds = 1..board.width

        var k = 1
        for (i in bounds) {
            for (j in bounds) {
                val cell = board.getCell(i, j)
                if (k <= 15 && board[cell] != k) {
                    isWon = false
                    break
                }
                k++
            }
        }

        return isWon && this[board.width, board.width] == null
    }

    override fun processMove(direction: Direction) {
        board.moveValues(direction)
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    private fun GameBoard<Int?>.addNewValue(initializer: GameOfFifteenInitializer) {
        val values = initializer.initialPermutation
        val bounds = 1..width
        var k = 0

        // add all values to the board
        for (i in bounds) {
            for (j in bounds) {
                val value = if (k < values.size) values[k] else null
                val cell = board.getCell(i, j)
                board[cell] = value
                k++
            }
        }
    }

    private fun GameBoard<Int?>.moveValues(direction: Direction) {
        val cell = filter { it == null }.first()

        when (direction) {
            Direction.UP -> {
                val next = getCell(cell.i + 1, cell.j)
                this[cell] = this[next]
                this[next] = null
            }
            Direction.DOWN -> {
                val next = getCell(cell.i - 1, cell.j)
                this[cell] = this[next]
                this[next] = null
            }
            Direction.RIGHT -> {
                val next = getCell(cell.i, cell.j - 1)
                this[cell] = this[next]
                this[next] = null
            }
            Direction.LEFT -> {
                val next = getCell(cell.i, cell.j + 1)
                this[cell] = this[next]
                this[next] = null
            }
        }
    }
}