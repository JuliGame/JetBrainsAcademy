package chess

import java.util.*

fun main() {
    println("Pawns-Only Chess")
    println("First Player's name:")
    val whiteName = readln()
    println("Second Player's name:")
    val blackName = readln()

    val main = Functions()
    main.board.print()

    while(true){
        val response = main.nextTurn(whiteName, blackName)
        if ("[a-hA-H][1-8][a-hA-H][1-8]".toRegex().matches(response)){
            val from = response.chunked(2)[0]
            val to = response.chunked(2)[1]

            val valid = main.validateMovement(from, to, main.n, true)
            if (!valid) continue

            main.board.tickPassants()
            main.board.move(from, to)
            main.n = !main.n
            main.board.print()

            //check wins
            when(main.board.checkWinners()){
                null -> continue
                Square.State.Empty -> {
                    println("Stalemate!")
                    println("Bye!")
                    break
                }
                Square.State.Black -> {
                    println("Black Wins!")
                    println("Bye!")
                    break
                }
                Square.State.White -> {
                    println("White Wins!")
                    println("Bye!")
                    break
                }
            }

        }else{
            if (response == "exit"){
                println("Bye!")
                break
            }
            else println("Invalid Input")
        }
    }
}


class Functions{
    var board = Board(this)

    var n = true
    fun nextTurn(p1:String, p2:String): String{
        println("${if (n) p1 else p2}'s turn:")
        return readln()
    }

    fun validateMovement(from: String, to: String, isWhite: Boolean, pri: Boolean = false): Boolean{
//        println("from $from to $to")
        val n1 = from.chunked(1)[1].toInt()
        val n2 = to.chunked(1)[1].toInt()

        if (!"[a-hA-H][1-8]".toRegex().matches(to)){
            return false
        }

        val l1 = from.chunked(1)[0].lowercase(Locale.getDefault()).toCharArray().first()
        val l2 = to.chunked(1)[0].lowercase(Locale.getDefault()).toCharArray().first()

        val square = board.getSquareAt(from)

        val vMov = kotlin.math.abs(board.charToNumber(l1) - board.charToNumber(l2))
        if (vMov > 1){
            if (pri) println("Invalid Input") //out of range
            return false
        }
        if (vMov == 0 && board.getSquareAt(to).state != Square.State.Empty){
            if (pri) println("Invalid Input") //something blocking the movement
            return false
        }


        if (isWhite){ //if white
            if (square.state != Square.State.White) {
                if (pri) println("No white pawn at $from")
                return false
            }
            if (n2 - n1 != 1 && !(n2 - n1 == 2 && !square.hasMoved)) {
                if (pri) println("Invalid Input") //out of range
                return false
            }
            if (vMov == 1 && board.getSquareAt(to).state != Square.State.Black){

                // En passant CAPTURE
                val passant = board.getSquareAt("$l2${n2-1}")
                if (passant.state == Square.State.Black && passant.passant){
                    passant.state = Square.State.Empty
                    return true
                }

                if (pri) println("Invalid Input") //no enemy to CAPTURE
                return false
            }

            return true
        }else{
            if (square.state != Square.State.Black) {
                if (pri) println("No black pawn at $from")
                return false
            }
            if (n2 - n1 != -1 && !(n2 - n1 == -2 && !square.hasMoved)) {
                if (pri) println("Invalid Input") //out of range
                return false
            }
            if (vMov == 1 && board.getSquareAt(to).state != Square.State.White){

                // En passant CAPTURE
                val passant = board.getSquareAt("$l2${n2+1}")
                if (passant.state == Square.State.White && passant.passant){
                    passant.state = Square.State.Empty
                    return true
                }

                if (pri) println("Invalid Input") //no enemy to CAPTURE
                return false
            }

            return true
        }
    }
}