package com.ziyun.mock.log

import com.ziyun.udf.SparkGrokUDF
import com.ziyun.utils.GrokUtils
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SaveMode, SparkSession}

import scala.util.Random

object SecurityLogMock {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()

    val path = "data/mock/raw/small"

    val df = spark.read.format("text").load(path)
    val grokPattern = "%{IPORHOST:src_ip} (?:%{NUMBER:in_flow}|-) (?:%{NUMBER:out_flow}|-) (?:%{NUMBER:duration}|-) \"\\[%{HTTPDATE:timestamp}\\]\""
    val sparkGrokUDF = new SparkGrokUDF(spark)
    sparkGrokUDF.registerGrokUDF2(grokPattern)

    val grokDF = df.selectExpr("grok(value) as grokCol")
    val fields = GrokUtils.getPatternFields(grokPattern)
    val rawDF = GrokUtils.unwrapColumns(grokDF, "grokCol", fields)

    //rawDF.show()
    val locIpDomainArr = spark.read.format("csv")
      .options(Map("header" -> "true"))
      .load("data/mock/loc_ip_domain/loc_ip_domain_full")
      .collect()
      .map(x => (x.getString(0), x.getString(1), x.getString(2)))

    val rowRDD = rawDF.rdd.map(row => {
      try {
        val srcIp = row.getString(0)
        val inFlow = row.getString(1)
        val outFlow = row.getString(2)
        val duration = row.getString(3)
        val eventTime = row.getString(4)

        val locIpDomain = locIpDomainArr(Random.nextInt(locIpDomainArr.length))
        val locID = locIpDomain._1
        val destIp = locIpDomain._2
        val domain = locIpDomain._3

        val appId = Random.nextInt(3167) + 1
        val srcPort = 80 + Random.nextInt(5000)
        val descPort = 80 + Random.nextInt(5000)

        Row(srcIp, srcPort.toString, destIp, descPort.toString, locID, domain, appId.toString, inFlow, outFlow, duration, eventTime)
      } catch {
        case e: Exception => {
          Row.empty
        }
      }
    })

    val struct = StructType(List(
      StructField("src_ip", StringType),
      StructField("src_port", StringType),
      StructField("dest_ip", StringType),
      StructField("dest_port", StringType),
      StructField("loc_id", StringType),

      StructField("domain", StringType),
      StructField("app_id", StringType),
      StructField("in_flow", StringType),
      StructField("out_flow", StringType),
      StructField("duration", StringType),

      StructField("event_time", StringType)

    ))

    val logDF = spark.createDataFrame(rowRDD.filter(_.length > 0), struct)

    logDF.write.format("csv")
      .options(Map("sep" -> "\t"))
      .mode(SaveMode.Append)
      .save("data/mock/securitylog")


    spark.close()
  }

}
