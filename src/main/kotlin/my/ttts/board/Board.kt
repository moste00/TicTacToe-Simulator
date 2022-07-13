package my.ttts.board

data class Row(val C1 : CellState,
               val C2 : CellState,
               val C3 : CellState) {

    fun fill(s : CellState, i : Int) : Row = when {
        i == 1 && C1 == CellState.Empty -> Row(s ,C2,C3)
        i == 2 && C2 == CellState.Empty -> Row(C1,s ,C3)
        i == 3 && C3 == CellState.Empty -> Row(C1,C2,s )
        else -> this
    }

    operator fun get(i : Int) : CellState = when(i) {
        1 -> C1
        2 -> C2
        3 -> C3
        else -> CellState.Empty
    }
}

class Board(private val R1 : Row, private val R2 : Row, private val R3 : Row) {

    companion object {
        @JvmStatic
        val Cells = listOf(CellPosition.UPPER_LEFT ,CellPosition.UPPER_MIDDLE ,CellPosition.UPPER_RIGHT ,
                           CellPosition.MIDDLE_LEFT,CellPosition.MIDDLE_MIDDLE,CellPosition.MIDDLE_RIGHT,
                           CellPosition.LOWER_LEFT ,CellPosition.LOWER_MIDDLE ,CellPosition.LOWER_RIGHT)
        @JvmStatic
        val Empty = Board(Row(CellState.Empty,CellState.Empty,CellState.Empty),
                          Row(CellState.Empty,CellState.Empty,CellState.Empty),
                          Row(CellState.Empty,CellState.Empty,CellState.Empty))

    }

    fun fill(pos : CellPosition, newState : CellState) : Board {
        val i = pos.toInt()
        return when {
            i <= 3 -> Board(R1.fill(newState,i),
                            R2,
                            R3)
            i <= 6 -> Board(R1,
                            R2.fill(newState,i - 3),
                            R3)
            i <= 9 -> Board(R1,
                            R2,
                            R3.fill(newState,i - 6))
            //impossible by definition of toInt
            //The compiler is just not smart enough to prove it
            else -> error("Impossible Error : Invalid Cell Position $i")
        }
    }

    operator fun get(pos : CellPosition) : CellState = when(pos) {
        CellPosition.UPPER_LEFT    -> R1[1]
        CellPosition.UPPER_MIDDLE  -> R1[2]
        CellPosition.UPPER_RIGHT   -> R1[3]
        CellPosition.MIDDLE_LEFT   -> R2[1]
        CellPosition.MIDDLE_MIDDLE -> R2[2]
        CellPosition.MIDDLE_RIGHT  -> R2[3]
        CellPosition.LOWER_LEFT    -> R3[1]
        CellPosition.LOWER_MIDDLE  -> R3[2]
        CellPosition.LOWER_RIGHT   -> R3[3]
    }

    val emptyCells = Cells.filter { this[it] == CellState.Empty }
    val isEmpty = emptyCells.size == 9
    val isFull  = emptyCells.isEmpty()
}

