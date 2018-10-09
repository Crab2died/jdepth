package com.github.jscript.scala

object IpIntUtil {

    private val INADDRSZ = 4

    def ip2Int(ip: String): Int = {

        val ipPart = ip.split("\\.")
        if (ipPart.length != INADDRSZ) {
            throw new IllegalArgumentException("[" + ip + "] is invalid IP.")
        }
        var ipInt: Int = 0
        for (i: Int <- 0 until ipPart.length) {
            ipInt |= (Integer.parseInt(ipPart(i)) & 0xFF) << (8 * (3 - i))
        }
        ipInt
    }

    def int2Ip(ipInt: Int): String = {
        val Ⅰ = (ipInt & 0xFF000000) >>> 24
        val Ⅱ = (ipInt & 0xFF0000) >>> 16
        val Ⅲ = (ipInt & 0XFF00) >>> 8
        val Ⅳ = ipInt & 0xFF
        Ⅰ + "." + Ⅱ + "." + Ⅲ + "." + Ⅳ
    }

}
