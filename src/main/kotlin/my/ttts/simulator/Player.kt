package my.ttts.simulator

import my.ttts.board.Board

interface Player {
    abstract fun play(board : Board, mark : Mark) : List<Board>
}

