package com.github.crab2died.utils

fun main(args: Array<String>) {

    // "Aa" & "BB" hash the same hashcode
    val map: HashMap<String, String> = HashMap()

    map["Aa"] = "Aa"
    map["BB"] = "BB"

    println(map)

    val set: HashSet<String> = HashSet()
    set.add("Aa")
    set.add("BB")

    println(set)

}