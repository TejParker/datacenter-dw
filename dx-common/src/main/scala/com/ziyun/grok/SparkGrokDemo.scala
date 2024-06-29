package com.ziyun.grok

import com.ziyun.udf.SparkGrokUDF
import com.ziyun.utils.GrokUtils
import org.apache.spark.sql.SparkSession

object SparkGrokDemo {

  def main(args: Array[String]): Unit = {
    val spark = SparkSession.builder().master("local[*]").getOrCreate()
    val path = "data/grok/grokData.txt"

    val df = spark.read.format("text").load(path)
    df.show(false)

    val sparkGrokUDF = new SparkGrokUDF(spark)
    val patten = "%{IPORHOST:addre} %{USER:ident} %{USER:auth} \\[%{HTTPDATE:timestamp}\\] \\\"%{WORD:http_method} %{NOTSPACE:request} HTTP/%{NUMBER:httpversion}\\\" %{NUMBER:status} (?:%{NUMBER:bytes}|-) \\\"(?:%{URI:http_referer}|-)\\\" \\\"%{GREEDYDATA:user_agent}\\\""
    sparkGrokUDF.registerGrokUDF(patten)
    var grokDF = df.selectExpr("grok(value) as grokCol")
    grokDF.show(false)

    /*val grokCol = grokDF("grokCol")
    //grokDF.withColumn("httpversion", grokCol("httpversion")).show
    val fields = GrokUtils.getPatternFields(patten)
    fields.foreach(field => {
      grokDF = grokDF.withColumn(field, grokCol(field))
    })
    grokDF = grokDF.drop("grokCol")
    grokDF.show()*/
    val fields = GrokUtils.getPatternFields(patten)
    val resDF = GrokUtils.unwrapColumns(grokDF, "grokCol", fields)
    resDF.show()
    spark.close()
  }
}
