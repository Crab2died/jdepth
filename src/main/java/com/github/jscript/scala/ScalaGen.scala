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


    // list
    var list1 = List("A", "B", "C")
    val list2 = "E":: list1
    println(list1 :: list2)
  }

}
