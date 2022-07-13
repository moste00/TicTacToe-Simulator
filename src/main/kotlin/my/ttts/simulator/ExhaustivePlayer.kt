package my.ttts.simulator

import my.ttts.board.Board
import my.ttts.board.CellState

class ExhaustivePlayer(m:Mark) : Player {
    private val me = when(m) {
        Mark.X -> CellState.X
        Mark.O -> CellState.O
    }

    override fun copy() = this
    //Simply performs every possible move at each turn
    override fun play(board: Board): List<Board> {
        val result = mutableListOf<Board>()
        for (c in board.emptyCells) {
            result.add(board.fill(c,me))
        }
        return result
    }
}