package my.ttts.simulator

import my.ttts.board.Board
import my.ttts.board.CellPosition
import my.ttts.board.CellState

enum class Mark {
    X { override fun not() = O },
    O { override fun not() = X };

    abstract operator fun not() : Mark
}

class Simulator(private val playerX : Player,
                private val playerO : Player) {
    val gameTree = GameTree()

    fun simulate(m : Mark) {
        val startingBoard = Board.Empty
        gameTree.beginState(startingBoard)
        continueSimulation(startingBoard,m)
        gameTree.endState()
    }

    private fun continueSimulation(board : Board, m : Mark) {
        if (win(board,Mark.X)) {
            gameTree.markCurrentStateAsWin(Mark.X)
            return
        }
        if (win(board,Mark.O)) {
            gameTree.markCurrentStateAsWin(Mark.O)
            return
        }
        if (board.isFull) {
            gameTree.markCurrentStateAsDraw()
            return
        }

        val currentPlayer = when(m) {
            Mark.X -> playerX
            Mark.O -> playerO
        }

        val newStates = currentPlayer.play(board)

        for (s in newStates) {
            gameTree.beginState(s)
            continueSimulation(s,!m)
            gameTree.endState()
        }
    }

    private companion object {
        @JvmStatic
        fun win(b : Board, m : Mark) : Boolean {
            val c = when(m) {
                Mark.X -> CellState.X
                Mark.O -> CellState.O
            }
            return when {
                //row win
                b[CellPosition.UPPER_LEFT] == c  && b[CellPosition.UPPER_MIDDLE] == c  && b[CellPosition.UPPER_RIGHT] == c
                    -> true
                b[CellPosition.MIDDLE_LEFT] == c && b[CellPosition.MIDDLE_MIDDLE] == c && b[CellPosition.MIDDLE_RIGHT] == c
                    -> true
                b[CellPosition.LOWER_LEFT] == c  && b[CellPosition.LOWER_MIDDLE] == c  && b[CellPosition.LOWER_RIGHT] == c
                    -> true

                //column win
                b[CellPosition.UPPER_LEFT] == c   && b[CellPosition.MIDDLE_LEFT] == c   && b[CellPosition.LOWER_LEFT] == c
                    -> true
                b[CellPosition.UPPER_MIDDLE] == c && b[CellPosition.MIDDLE_MIDDLE] == c && b[CellPosition.LOWER_MIDDLE] == c
                    -> true
                b[CellPosition.UPPER_RIGHT] == c  && b[CellPosition.MIDDLE_RIGHT] == c  && b[CellPosition.LOWER_RIGHT] == c
                    -> true

                //diagonal win
                b[CellPosition.UPPER_LEFT] == c  && b[CellPosition.MIDDLE_MIDDLE] == c && b[CellPosition.LOWER_RIGHT] == c
                    -> true
                b[CellPosition.UPPER_RIGHT] == c && b[CellPosition.MIDDLE_MIDDLE] == c && b[CellPosition.LOWER_LEFT] == c
                    -> true

                //no win
                else -> false
            }
        }
    }
}