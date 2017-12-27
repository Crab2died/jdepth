package com.github.jscript.kotlin

fun main(ags: Array<String>) {
    println("Hello World")

    basicDataType()
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

