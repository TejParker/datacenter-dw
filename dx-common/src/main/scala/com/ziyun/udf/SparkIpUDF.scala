package com.ziyun.udf

import com.ziyun.utils.IPUtils
import org.apache.spark.sql.SparkSession

class SparkIpUDF(spark: SparkSession) {

  def registerCommon() = {
    spark.udf.register("ipv4_to_num", (ipv4: String) => {
      IPUtils.ipv4ToNum(ipv4)
    })

    spark.udf.register("num_to_ipv4", (num: Long) => {
      IPUtils.num2Ipv4(num)
    })

    spark.udf.register("ipv4_short", (ipv4: String) => {
      IPUtils.convertIpv4ToShort(ipv4)
    })

    spark.udf.register("ipv4_full", (ipv4: String) => {
      IPUtils.convertIpv4ToFull(ipv4)
    })
  }

  /**
   * IP地址的二分查找UDF定义
   * @param ipLibArr
   */
  def registerIpSearch(ipLibArr: Array[(String, String, String)]) = {
    spark.udf.register("ip_search", (ip: String) => {
      val index = IPUtils.ipBinarySearch(ipLibArr, IPUtils.convertIpv4ToFull(ip))
      if (index >= 0) {
        ipLibArr(index)._3
      } else {
        "unknown"
      }
    })
  }
}
