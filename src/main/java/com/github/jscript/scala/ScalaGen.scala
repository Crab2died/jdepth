package com.github.jscript.scala

object ScalaGen {

    def main(args: Array[String]): Unit = {

        println("hello world")

        for (i <- 1 to 5 by 1 if i % 2 == 0) {
            println(i)
        }

        // 推导式
        val v = for (i <- 1 to 5 by 2) yield {
            println(i); i
        }
        println(v)

        // 闭包
        val p = (i: Int, b: Int) => i + b
        println(p(12, 12))

        // list
        val list1 = List("A", "B", "C")
        val list2 = "E" :: list1
        println(list1 :: list2)

        val calc = new Area()
        println(calc.calcArea(12, 12))

    }

}

class Area {

    def calcArea(x: Double, y: Double): Double = {
        x * y
    }

}

