package com.ziyun.tools.hdfs

import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.sql.{SaveMode, SparkSession}

import java.io.File
import scala.collection.mutable

object RDBMS2HDFS {

  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      System.err.println("Usage: RDBMS2HDFS <configFile>")
      System.exit(1);
    }

    val configFile = args(0)
    // 这里通过配置文件的路径进行解析的，这种方式适合yarn-client, local模式，TODO:如果是yarn-cluster模式是不能同各国这种方式进行解析的
    val config = ConfigFactory.parseFile(new File(configFile))

    val spark = SparkSession.builder().getOrCreate()

    val rdbmsConfig = config.getConfig("rdbms")
    val rdbmsProperties = getProperties(rdbmsConfig)
    val df = spark.read.format("jdbc")
      .options(rdbmsProperties)
      .load()

    val hdfsConfig = config.getConfig("hdfs")
    val format = hdfsConfig.getString("format")
    val saveMode = hdfsConfig.getString("save_mode")
    val path = hdfsConfig.getString("path")
    val hdfsProperties = getProperties(hdfsConfig)

    df.write.format(format)
      .options(hdfsProperties)
      .mode(saveMode)
      .save(path)


    spark.close()
  }

  def getProperties(config: Config): Map[String, String] = {
    import scala.collection.JavaConverters._
    val properties = new mutable.HashMap[String, String]()
    config.getConfig("properties").entrySet().asScala
      .foreach(entry => {
        properties.put(entry.getKey, entry.getValue.unwrapped().toString)
      })
    properties.toMap
  }
}
