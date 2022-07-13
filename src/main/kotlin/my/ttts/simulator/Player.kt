package my.ttts.simulator

import my.ttts.board.Board

interface Player {
    fun play(board : Board) : List<Board>
    fun copy() : Player
}

