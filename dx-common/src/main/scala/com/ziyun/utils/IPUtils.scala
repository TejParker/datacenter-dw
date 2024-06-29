package com.ziyun.utils

import org.apache.spark.sql.{SparkSession, functions}

object IPUtils {
  /**
   * ipv4转成数字
   * 192.168.11.22
   * @param ip
   * @return
   */
  def ipv4ToNum(ip: String): Long = {
    val ipArr = ip.split("\\.")
    var num = 0L
    for (i <- 0 to 3) {
      val seg = ipArr(i).toLong
      num = num + (seg << (3 - i) * 8)
    }
    num
  }

  def num2Ipv4(ip: Long) = {
    ((ip >> 24) & 0xFF) + "." +
      ((ip >> 16) & 0xFF) + "." +
      ((ip >> 8) & 0xFF) + "." +
      ((ip >> 0) & 0xFF)
  }

  def convertIpv4ToFull(ipv4: String) = {
    ipv4.split("\\.").map(x => "%03d".format(x.toInt)).mkString(".")
  }

  /**
   * ipv4全写方式转换成简写
   * @param ipv4
   * @return
   */
  def convertIpv4ToShort(ipv4: String) = {
    ipv4.split("\\.")
      .map(x => x.toInt)
      .mkString(".")
  }

  def ipBinarySearch(ipInfo: Array[(String, String, String)],
                     ip: String): Int = {
    var low = 0
    var high = ipInfo.length - 1
    while (low <= high) {
      val mid = (low + high) / 2
      val midInfo = ipInfo(mid)
      val startIp = midInfo._1
      val endIp = midInfo._2
      if (ip >= startIp && ip <= endIp) {
        return mid
      } else if (ip < startIp) {
        high = mid - 1
      } else {
        low = mid + 1
      }
    }
    -1
  }
  def main(args: Array[String]): Unit = {
    /*val ip = "192.168.11.22"
    val ipNum = ipv4ToNum(ip)
    println(ipNum)

    val ipStr = num2Ipv4(ipNum)
    println(ipStr)

    val ipFull = convertIpv4ToFull(ipStr)
    println(ipFull)
    val ipShort = convertIpv4ToShort(ipFull)
    println(ipShort)*/

    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    spark.udf.register("ipv4_full", (ipv4: String) => {
      convertIpv4ToFull(ipv4)
    })
    val path = "data/ip_search_data/ip_lib_thin.json"
    val df = spark.read.format("json").load(path)
      .selectExpr("ipv4_full(start_ip) as start_ip",
        "ipv4_full(end_ip) as end_ip", "area_code")
      .orderBy(functions.col("start_ip"))

    var arr = df.collect()
      .map(x => (x.getString(0), x.getString(1), x.getString(2)))

    val ip = "117.35.128.1"
    val idx = ipBinarySearch(arr, convertIpv4ToFull(ip))
    println(idx)
    if (idx >= 0) {
      println(arr(idx)._3)
    } else {
      println("unkown")
    }
    //println(arr(idx)._3)

    spark.close()
  }

}
