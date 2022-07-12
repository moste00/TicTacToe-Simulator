package my.ttts.simulator

import my.ttts.board.Board

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
        val currentPlayer = when(m) {
            Mark.X -> playerX
            Mark.O -> playerO
        }
        val newStates = currentPlayer.play(board,m)
        for (s in newStates) {
            gameTree.beginState(s)
            continueSimulation(s,!m)
            gameTree.endState()
        }
    }
}