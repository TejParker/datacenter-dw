package com.ziyun.mock.locipdomain

import com.ziyun.udf.SparkIpUDF
import com.ziyun.utils.IPUtils
import org.apache.spark.sql.{DataFrame, SaveMode, SparkSession}

import scala.collection.mutable
import scala.util.Random

object LocIpDomainMidMock {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()

    val ipLibDF = getIpLibData(spark)
    val domainBodyArr = spark.sparkContext.textFile("data/mock/domain_body/domain_body.txt").collect()
    val domainPrefixList = List("blog", "article", "news", "soft")
    val domainSuffixList = List("cn", "net", "com", "io")
    val locIDList = List("101", "102", "103", "104")
    //ipLibDF.show()
    val sparkIpUDF = new SparkIpUDF(spark)
    sparkIpUDF.registerCommon()

    import spark.implicits._
    val midDF = ipLibDF.selectExpr("ipv4_to_num(start_ip)", "ipv4_to_num(end_ip)")
      .flatMap(row => {
        val startIpNum = row.getAs[Long](0)
        val endIpNum = row.getAs[Long](1)
        var index = 0
        val set = new mutable.HashSet[(String, String, String, Int)]()
        while (startIpNum + index < endIpNum & index < 100) {
          val ip = IPUtils.num2Ipv4(startIpNum + index)
          val domain = domainPrefixList(Random.nextInt(domainPrefixList.length)) + "." +
            domainBodyArr(Random.nextInt(domainBodyArr.length)) + "." +
            domainSuffixList(Random.nextInt(domainSuffixList.length))
          val locID = locIDList(Random.nextInt(locIDList.length))

          val status = if (Random.nextInt(100) < 99) 1 else 0
          set.+=((locID, ip, domain, status))
          index += 1

        }
        set
      }).toDF("loc_id", "ip", "domain", "status")

    //midDF.show()
    // 全量的中间数据1
    midDF.write.format("csv")
      .options(Map("header" -> "true"))
      .mode(SaveMode.Overwrite)
      .save("data/mock/loc_ip_domain/loc_ip_domain_full")


    spark.read.format("csv")
      .options(Map("header" -> "true"))
      .load("data/mock/loc_ip_domain/loc_ip_domain_full")
      .sample(0.99)
      .write.format("csv")
      .options(Map("header" -> "true"))
      .save("data/mock/loc_ip_domain/loc_ip_domain")

    spark.close()


  }

  /**
   * 获取上海的ip地址段
   * @param spark
   * @return
   */
  def getIpLibData(spark: SparkSession): DataFrame = {
    val level2Path = "data/ip/ip_area/area_level02"
    val level3Path = "data/ip/ip_area/area_level03"
    val ipLibPath = "data/ip/ip_lib"

    spark.read.format("json").load(level2Path)
      .filter("area_name='广东'").createOrReplaceTempView("level02")
    spark.read.format("json").load(level3Path)
      .createOrReplaceTempView("level03")
    spark.read.format("json").load(ipLibPath)
      .createOrReplaceTempView("lib")

    val sql =
    """
        |select
        | c.start_ip, c.end_ip
        |from level02 a, level03 b, lib c
        |where b.area_code = c.area_code and
        |       a.area_code = b.parent_area
        |
        |""".stripMargin

    spark.sql(sql)

  }

}
