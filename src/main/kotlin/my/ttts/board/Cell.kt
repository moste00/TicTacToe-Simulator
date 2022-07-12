package my.ttts.board

enum class CellState {
    X,
    O,
    Empty
}

enum class CellPosition {
    UPPER_LEFT   { override fun toInt() = 1 },
    UPPER_MIDDLE { override fun toInt() = 2 },
    UPPER_RIGHT  { override fun toInt() = 3 },
    MIDDLE_LEFT  { override fun toInt() = 4 },
    MIDDLE_MIDDLE{ override fun toInt() = 5 },
    MIDDLE_RIGHT { override fun toInt() = 6 },
    LOWER_LEFT   { override fun toInt() = 7 },
    LOWER_MIDDLE { override fun toInt() = 8 },
    LOWER_RIGHT  { override fun toInt() = 9 };

    abstract fun toInt() : Int
    companion object {
        @JvmStatic
        fun fromInt(i : Int) : CellPosition? = when(i) {
            1 -> UPPER_LEFT
            2 -> UPPER_MIDDLE
            3 -> UPPER_RIGHT
            4 -> MIDDLE_LEFT
            5 -> MIDDLE_MIDDLE
            6 -> MIDDLE_RIGHT
            7 -> LOWER_LEFT
            8 -> LOWER_MIDDLE
            9 -> LOWER_RIGHT
            else -> null
        }
    }
}