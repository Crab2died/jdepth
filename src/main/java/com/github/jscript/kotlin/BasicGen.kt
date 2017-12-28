package com.github.jscript.kotlin

fun main(ags: Array<String>) {
    println("Hello World")

    basicDataType()

    condition(2)
}

fun basicDataType() {

    val a: Char = 'a'
    val b: Byte = 127
    val c: Short = 12
    val d: Int = 7811
    val e: Double = 123.1
    val f: Long = 121212L
    val g: Boolean = true

    println(a.toDouble() + b + c + d + e + f)
    println(g)

}

fun condition(what: Int) {

    var a = what

    // 条件
    if (a in 1..5)
        println("$a in [1,5]")
    else
        println("$a not in [1,5]")

    // 类三目
    a = if (a > 0) 8 else 0

    println(a)

    // 类switch
    when (a){
        1 -> println("$a")
        in 1..8 -> print("$a in [1,8]")
        else -> {
            println("I do not known")
        }
    }
}