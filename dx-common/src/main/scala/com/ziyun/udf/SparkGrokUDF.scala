package com.ziyun.udf

import com.ziyun.utils.GrokUtils
import org.apache.spark.sql.SparkSession

class SparkGrokUDF(spark: SparkSession) {
  def registerGrokUDF(patten: String) = {
    spark.udf.register("grok", (text: String) => {
      val grokCompiler = GrokUtils.createGrokCompiler()
      GrokUtils.getCompilerCapture(grokCompiler, patten, text)
    })
  }

  def registerGrokUDF2(patten: String) = {
    val grokCompiler = GrokUtils.createGrokCompiler()
    val grok = grokCompiler.compile(patten)
    spark.udf.register("grok", (text: String) => {
      GrokUtils.getCapture(grok, patten, text)
    })
  }

}
