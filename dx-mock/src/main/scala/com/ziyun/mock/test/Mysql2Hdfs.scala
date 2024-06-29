package com.ziyun.mock.test

import com.typesafe.config.ConfigFactory
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.slf4j.LoggerFactory

import java.io.File

object Mysql2Hdfs {
  def main(args: Array[String]): Unit = {
    // Get logger
    val logger = LoggerFactory.getLogger(getClass)
    val spark = SparkSession.builder().getOrCreate()
    // Log some messages
    logger.info("Starting SparkLogbackExample application")

    val list = List((1, "simon"), (2, "aina"))
    println(list)
    val rdd = spark.sparkContext.makeRDD(list)
    import spark.implicits._

    val df = rdd.toDF("id", "name")
    logger.info("DataFrame created")
    df.show()

    val config = ConfigFactory.parseFile(new File("conf/data2mysql.conf"))
    val mysqlConfig = config.getConfig("mysql")
    val user = mysqlConfig.getString("user")
    val password = mysqlConfig.getString("password")
    val url = mysqlConfig.getString("url")
    val driver = mysqlConfig.getString("driver")
    val dbtable = mysqlConfig.getString("dbtable")

    /*df.write.format("jdbc").options(
      Map(
        "user" -> "hive",
        "password" -> "hive@123",
        "url" -> "jdbc:mysql://192.168.0.161:3306/?useUnicode=true&characterEncoding=UTF-8",
        "dbtable" -> "test_db.t_user"
      )
    ).mode(SaveMode.Overwrite).save()*/

    df.write.format("jdbc")
      .options(Map(
        "user" -> user,
        "password" -> password,
        "url" -> url,
        "driver" -> driver,
        "dbtable" -> dbtable
      )).mode(SaveMode.Overwrite).save()

    logger.info("Application finished")


    spark.close()
  }

}
