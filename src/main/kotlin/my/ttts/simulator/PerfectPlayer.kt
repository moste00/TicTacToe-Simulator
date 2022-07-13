package my.ttts.simulator

import my.ttts.board.Board
import my.ttts.board.CellPosition
import my.ttts.board.CellState

class PerfectPlayer(m:Mark) : Player {
    private val me = when(m) {
        Mark.X -> CellState.X
        Mark.O -> CellState.O
    }
    private val otherPlayer = when(m) {
        Mark.X -> CellState.O
        Mark.O -> CellState.X
    }
    private var currTurn = 1
    private var hasOpeningMove = false
    private var opponentMoves = ArrayDeque<CellPosition>(3)

    //Optimal Play as described in https://upload.wikimedia.org/wikipedia/commons/d/de/Tictactoe-X.svg
    override fun play(board: Board): List<Board> {
        //Opening move, guaranteed win or draw
        if (board.isEmpty) {
            hasOpeningMove = true
        }
        return if (hasOpeningMove) playWithOpeningMove(board)
               else                playWithoutOpeningMove(board)
    }

    private fun playWithOpeningMove(board: Board): List<Board> {
        val playPos = optimalPositionWithOpeningMove(board)
        currTurn++
        return listOf(board.fill(playPos,me))
    }

    private fun optimalPositionWithOpeningMove(board: Board): CellPosition {
        return when(currTurn) {
            5 -> {
                reset()
                board.emptyCells[0]
            }

            1 -> OPTIMAL_OPENING_MOVE_STRATEGY[1] as CellPosition

            else -> {
                for (c in Board.Cells) {
                    if (board[c] == otherPlayer && !opponentMoves.contains(c)) {
                        opponentMoves.addLast(c)
                        break
                    }
                }
                when(currTurn) {
                    2 -> {
                        val turn2Table = OPTIMAL_OPENING_MOVE_STRATEGY[2] as Map<CellPosition, CellPosition>
                        turn2Table[opponentMoves[0]]!!
                    }
                    3 -> {
                        val turn3table = OPTIMAL_OPENING_MOVE_STRATEGY[3] as Map<CellPosition,Map<CellPosition?,CellPosition>>
                        val turn3secondTable = turn3table[opponentMoves[0]] as Map<CellPosition?, CellPosition>
                        if (opponentMoves[1] in turn3secondTable.keys) {
                            turn3secondTable[opponentMoves[1]]!!
                        }
                        else {
                            reset()
                            turn3secondTable[null]!!
                        }
                    }
                    4 -> {
                        val turn4table = OPTIMAL_OPENING_MOVE_STRATEGY[4] as Map<CellPosition,Map<CellPosition,Map<CellPosition?,CellPosition>>>
                        val turn4secondTable = turn4table[opponentMoves[0]] as Map<CellPosition,Map<CellPosition?,CellPosition>>
                        val turn4thirdTable = turn4secondTable[opponentMoves[1]] as Map<CellPosition?,CellPosition>
                        if (opponentMoves[2] in turn4thirdTable.keys) {
                            val result = turn4thirdTable[opponentMoves[2]]!!
                            if (opponentMoves[2] != CellPosition.MIDDLE_LEFT) reset()
                            result
                        }
                        else {
                            reset()
                            turn4thirdTable[null]!!
                        }
                    }
                    else -> error("Impossible Error : Invalid Game Turn $currTurn")
                }
            }
        }
    }
    private fun reset() {
        currTurn = 0
        hasOpeningMove = false
        opponentMoves.clear()
    }
    private fun playWithoutOpeningMove(board: Board): List<Board> {
        TODO()
    }

    private companion object {
        @JvmStatic
        val OPTIMAL_OPENING_MOVE_STRATEGY = mapOf(
            1 to CellPosition.UPPER_LEFT,
            2 to mapOf(
                //Opponent's mark             //Optimal Mark
                CellPosition.UPPER_MIDDLE  to CellPosition.MIDDLE_LEFT,
                CellPosition.UPPER_RIGHT   to CellPosition.MIDDLE_LEFT,
                CellPosition.MIDDLE_LEFT   to CellPosition.UPPER_MIDDLE,
                CellPosition.MIDDLE_MIDDLE to CellPosition.UPPER_MIDDLE,
                CellPosition.LOWER_LEFT    to CellPosition.UPPER_MIDDLE,
                CellPosition.MIDDLE_RIGHT  to CellPosition.MIDDLE_MIDDLE,
                CellPosition.LOWER_MIDDLE  to CellPosition.UPPER_RIGHT,
                CellPosition.LOWER_RIGHT   to CellPosition.UPPER_RIGHT
            ),
            3 to mapOf(
                //Opponent's mark in previous turn
                CellPosition.UPPER_MIDDLE  to mapOf(
                    //Opponent's mark in current Turn
                    null                     to CellPosition.LOWER_LEFT, //win
                    CellPosition.LOWER_LEFT  to CellPosition.MIDDLE_MIDDLE
                ),
                CellPosition.UPPER_RIGHT   to mapOf(
                    null                     to CellPosition.LOWER_LEFT, //win
                    CellPosition.LOWER_LEFT  to CellPosition.MIDDLE_MIDDLE
                ),
                CellPosition.MIDDLE_LEFT   to mapOf(
                    null                     to CellPosition.UPPER_RIGHT, //win
                    CellPosition.UPPER_RIGHT to CellPosition.MIDDLE_MIDDLE
                ),
                CellPosition.MIDDLE_MIDDLE to mapOf(
                    null                     to CellPosition.UPPER_RIGHT, //win
                    CellPosition.UPPER_RIGHT to CellPosition.LOWER_LEFT,
                ),
                CellPosition.LOWER_LEFT    to mapOf(
                    null                     to CellPosition.UPPER_RIGHT, //win
                    CellPosition.UPPER_RIGHT to CellPosition.MIDDLE_MIDDLE
                ),
                CellPosition.MIDDLE_RIGHT  to mapOf(
                    null                     to CellPosition.LOWER_RIGHT, //win
                    CellPosition.LOWER_RIGHT to CellPosition.UPPER_RIGHT
                ),
                CellPosition.LOWER_MIDDLE  to mapOf(
                    null                      to CellPosition.UPPER_MIDDLE, //win
                    CellPosition.UPPER_MIDDLE to CellPosition.MIDDLE_MIDDLE
                ),
                CellPosition.LOWER_RIGHT   to mapOf(
                    null                      to CellPosition.UPPER_MIDDLE, //win
                    CellPosition.UPPER_MIDDLE to CellPosition.LOWER_LEFT
                )
            ),
            4 to mapOf(
                //Opponent's mark in 2nd turn
                CellPosition.UPPER_MIDDLE  to mapOf(
                    //Opponent's mark in 3rd Turn
                    CellPosition.LOWER_LEFT  to mapOf(
                        //Opponent's mark in current turn
                        null                      to CellPosition.MIDDLE_RIGHT, //win
                        CellPosition.MIDDLE_RIGHT to CellPosition.LOWER_RIGHT   //win
                    )
                ),
                CellPosition.UPPER_RIGHT   to mapOf(
                    CellPosition.LOWER_LEFT  to mapOf(
                        null                      to CellPosition.MIDDLE_RIGHT, //win
                        CellPosition.MIDDLE_RIGHT to CellPosition.LOWER_RIGHT   //win
                    )
                ),
                CellPosition.MIDDLE_LEFT   to mapOf(
                    CellPosition.UPPER_RIGHT to mapOf(
                        null                      to CellPosition.LOWER_MIDDLE, //win
                        CellPosition.LOWER_MIDDLE to CellPosition.LOWER_RIGHT   //win
                    )
                ),
                CellPosition.MIDDLE_MIDDLE to mapOf(
                    CellPosition.UPPER_RIGHT to mapOf(
                        null                      to CellPosition.MIDDLE_LEFT, //win
                        CellPosition.MIDDLE_LEFT  to CellPosition.MIDDLE_RIGHT //Blocking the opponent,
                                                                               //This is the only game will result in a draw and will last to turn 5
                    )
                ),
                CellPosition.LOWER_LEFT    to mapOf(
                    CellPosition.UPPER_RIGHT to mapOf(
                        null                      to CellPosition.LOWER_MIDDLE, //win
                        CellPosition.LOWER_MIDDLE to CellPosition.LOWER_RIGHT   //win
                    )
                ),
                CellPosition.MIDDLE_RIGHT  to mapOf(
                    CellPosition.LOWER_RIGHT to mapOf(
                        null                      to CellPosition.UPPER_MIDDLE, //win
                        CellPosition.UPPER_MIDDLE to CellPosition.LOWER_LEFT    //win
                    )
                ),
                CellPosition.LOWER_MIDDLE  to mapOf(
                    CellPosition.UPPER_MIDDLE to mapOf(
                        null                      to CellPosition.LOWER_LEFT, //win
                        CellPosition.LOWER_LEFT   to CellPosition.LOWER_RIGHT //win
                    )
                ),
                CellPosition.LOWER_RIGHT   to mapOf(
                    CellPosition.UPPER_MIDDLE to mapOf(
                        null                      to CellPosition.MIDDLE_LEFT, //win
                        CellPosition.MIDDLE_LEFT  to CellPosition.MIDDLE_MIDDLE //win
                    )
                )
        ))
    }
}
