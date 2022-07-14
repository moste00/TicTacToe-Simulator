package my.ttts.entry


import my.ttts.board.Board
import my.ttts.board.CellPosition
import my.ttts.board.CellState
import my.ttts.simulator.*


fun main() {
    val sim = Simulator(PerfectPlayer(Mark.X),
                        ExhaustivePlayer(Mark.O))

    sim.simulate(Mark.X)
}