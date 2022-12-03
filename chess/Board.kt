package chess

import kotlin.math.abs

class Square(s:State){
    enum class State{
        Empty,
        Black,
        White
    }
    var state: State = s
    var hasMoved = false
    var passant = false
    override fun toString(): String {
        return when (state) {
            State.Empty -> " "
            State.Black -> "B"
            State.White -> "W"
        }
    }
}

class Board(_main: Functions) {
    private val board: MutableList<MutableList<Square>>
    private val main = _main

    init {
        board = generateBoard()
    }

    private fun generateBoard(): MutableList<MutableList<Square>> {
        val board: MutableList<MutableList<Square>> = ArrayList()

        repeat(8){
            val row: MutableList<Square> = ArrayList()
            val r = it
            repeat(8){
                row.add(when(r){
                    1 -> Square(Square.State.White)
                    6 -> Square(Square.State.Black)
                    else -> Square(Square.State.Empty)
                })
            }
            board.add(0,row)
        }

        return board
    }

    fun print() {
        var i = 8
        board.forEach{row ->
            println("  +---+---+---+---+---+---+---+---+")
            println("$i | " + row.joinToString(" | ") + " |")
            i--
        }
        println("  +---+---+---+---+---+---+---+---+")
        println("    a   b   c   d   e   f   g   h")
    }

    fun getSquareAt(pos: String): Square{
        var n = pos.chunked(1)[1].toInt()
        n = abs(8-n) //invertimos porque sino lo lee mal

        val ln = charToNumber(pos.chunked(1)[0].toCharArray().first())

        return board[n][ln]
    }

    fun charToNumber(c: Char): Int{
        return when(c){
            'a' -> 0
            'b' -> 1
            'c' -> 2
            'd' -> 3
            'e' -> 4
            'f' -> 5
            'g' -> 6
            'h' -> 7
            else -> 100
        }
    }

     private fun numberToChar(n: Int): Char{
        return when(n){
            0 -> 'a'
            1 -> 'b'
            2 -> 'c'
            3 -> 'd'
            4 -> 'e'
            5 -> 'f'
            6 -> 'g'
            7 -> 'h'
            else -> 'z'
        }
    }

    fun move(from: String, to: String){
        getSquareAt(to).state = getSquareAt(from).state
        getSquareAt(from).state = Square.State.Empty
        if (!getSquareAt(to).hasMoved) getSquareAt(to).passant = true
        getSquareAt(to).hasMoved = true
    }

    fun tickPassants(){
        board.forEach{row ->
            row.forEach{
                square -> square.passant = false
            }
        }
    }

    fun checkWinners(): Square.State? {

        val occupiedSquares: MutableList<String> = ArrayList()

        var blackSquares = 0
        var whiteSquares = 0
        for (iRow in board.indices){
            ilop@
            for (iLine in board[iRow].indices){
                if (board[iRow][iLine].state == Square.State.Empty) continue@ilop

                occupiedSquares.add("${iLine}${iRow}")

                if (board[iRow][iLine].state == Square.State.White) whiteSquares++ else blackSquares++

                if (iRow == 0 || iRow == 7){ //Gana llegando al final
                    return if(board[iRow][iLine].state == Square.State.White) Square.State.White else Square.State.Black
                }
            }
        }
        if (blackSquares == 0) return Square.State.White
        if (whiteSquares == 0) return Square.State.Black

        occupiedSquares.forEach{oc ->
            val iRow = oc.chunked(1)[1].toInt()
            val n = abs(8-iRow)
            val lRow = oc.chunked(1)[0].toInt()
            val l = numberToChar(lRow)


            if (board[iRow][lRow].state == Square.State.White){
                if (
                    !main.validateMovement("$l${n}","$l${n+1}",true) &&
                    !main.validateMovement("$l${n}","${l.inc()}${n+1}", true) &&
                    !main.validateMovement("$l${n}","${l.dec()}${n+1}", true)
                ){ whiteSquares-- }
            }else if (board[iRow][lRow].state == Square.State.Black) {
                if (
                !main.validateMovement("$l${n}","$l${n-1}",false) &&
                !main.validateMovement("$l${n}","${l.inc()}${n-1}", false) &&
                !main.validateMovement("$l${n}","${l.dec()}${n-1}", false)
                ) { blackSquares-- }
            }else{
//                println("ERROR")
            }

            if (whiteSquares == 0 || blackSquares == 0 ) return Square.State.Empty

        }

        return null
    }
}

