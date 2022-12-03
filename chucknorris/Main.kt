package chucknorris

import java.lang.Exception


fun main() {
    var running = true
    while (running){
        println("Please input operation (encode/decode/exit):")
        when (val input = readln()) {
            "encode" -> encode()
            "decode" -> {
                try { decode() }
                catch (e: Exception) {println("not valid")}
            }
            "exit" -> running = false
            else -> println("There is no '$input' operation")
        }
    }
    println("Bye!")
}

fun decode(){
    println("Input encoded string:")

    val input = readln()

    // checkeo (1) de espacios y 0, sino tira una exception
    input.replace(" ", "").trim().toInt()


    val slices = input.trim().split(" ")
    val chunks = mutableListOf<String>()


    // checkeo (3), si no es par, tira exception
    if (slices.size % 2 != 0) throw Exception()

    //info create chunks
    for (i in slices.indices step 2){
        // checkeo (2) si no es ni 0 ni 00 tira una exception
        if (slices[i] != "0" && slices[i] != "00") throw Exception()
        chunks.add(slices[i] +" "+ slices[i + 1])
    }

    //info to binary
    var binary = ""
    chunks.forEach { block ->
        val a = block.split(" ")[0]
        val b = block.split(" ")[1]
        binary += if (a == "0") "1".repeat(b.length) else "0".repeat(b.length)
    }

    //info to chars
    val chars = mutableListOf<Char>()

    // checkeo (4), si no es multiplo de 7, tira exception
    if (binary.length %7 != 0) throw Exception()
    binary.chunked(7).iterator().forEach{b -> chars.add(Char(Integer.valueOf(b,2)))}

    var out = ""
    chars.forEach{char -> out += char}

    println("Decoded string:")
    println(out)
}

fun encode() {
    println("input string:")

    val input: String = readln()
    val chars = input.asSequence()

    var sequence = ""
    for (i in chars){
        var ascii: String = Integer.toBinaryString(i.code)
        ascii = "0".repeat(7-ascii.length).plus(ascii)
        sequence += ascii
    }

    var block = ""
    var last: Int? = null

    for (n in sequence){
        val nint = Integer.parseInt(n+"")
        if (nint == last){
            block += "0"
        }else{
            if (last != null) block += " "
            block += if (nint == 0) "00 0" else "0 0"
        }
        last = nint
    }
    println("Encoded string:")
    println(block)
}