package com.ziyun.mock.locipdomain

import org.apache.spark.sql.{SaveMode, SparkSession}

object LocIpDomainMock {
  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()

    val path = "data/mock/loc_ip_domain/loc_ip_domain"

    val df = spark.read.format("csv")
      .options(Map("header" -> "true"))
      .load(path)

    // ip + domain
    val ipDomainDF = df.select("ip", "domain")
    // loc_id, ip + status
    val locIpDF = df.select("loc_id", "ip", "status")
    // illegal entity
    val illegalIpDF = df
      .selectExpr("'0' as illegal_type", "ip as illegal_entity", "now() as create_time")
      .sample(0.01)

    val illegalDomainDF = df
      .selectExpr("'1' as illegal_type", "domain as illegal_entity", "now() as create_time")
      .sample(0.01)

    val illegalDF = illegalIpDF.unionAll(illegalDomainDF)

    val commonOptions = Map(
      "url" ->"jdbc:mysql://bigdata:3306/dx?useUnicode=true&characterEncoding=UTF-8",
      "user" -> "hive",
      "password" -> "hive@123"
    )
    // ip + domain 写入
    ipDomainDF.write.format("jdbc")
      .options(commonOptions ++ Map("dbtable" -> "dx.ip_domain"))
      .mode(SaveMode.Overwrite)
      .save()

    locIpDF.write.format("jdbc")
      .options(commonOptions ++ Map("dbtable" -> "dx.loc_ip"))
      .mode(SaveMode.Overwrite)
      .save()

    illegalDF.write.format("jdbc")
      .options(commonOptions ++ Map("dbtable" -> "dx.illegal_entity"))
      .mode(SaveMode.Overwrite)
      .save()

    spark.close()
  }
}
