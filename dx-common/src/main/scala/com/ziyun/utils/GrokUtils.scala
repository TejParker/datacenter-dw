package com.ziyun.utils

import io.krakens.grok.api.{Grok, GrokCompiler}
import org.apache.spark.sql.DataFrame

import java.io.{File, FileInputStream}
import scala.collection.JavaConverters.mapAsScalaMapConverter
import scala.collection.mutable

object GrokUtils {

  def createGrokCompiler(): GrokCompiler = {
    val grokCompiler = GrokCompiler.newInstance
    //    grokCompiler.registerDefaultPatterns
    val curDir = new File(".")
    val currAbsoluteDir = curDir.getAbsolutePath
    val grokDir = currAbsoluteDir + "/plugins/patterns"
    val grokDirFile = new File(grokDir)
    grokDirFile.listFiles().foreach(f => {
      if (f.isFile) {
        grokCompiler.register(new FileInputStream(f))
      }
    })
    grokCompiler
  }

  /**
   * 解析文本，返回解析后的数据
   * @param grokCompiler
   * @param patten
   * @param text
   * @return
   */
  def getCompilerCapture(grokCompiler: GrokCompiler,
                         patten: String,
                         text: String): Map[String, String] = {
    val grok = grokCompiler.compile(patten)
    val gm = grok.`match`(text)
    val capture = gm.capture()
    val fields = getPatternFields(patten)
    val map = capture.asScala.filter(x => fields.contains(x._1))
    map.toMap.mapValues(_.asInstanceOf[String])
  }

  /**
   * 使用grok对数据进行解析
   * @param grok
   * @param patten
   * @param text
   * @return
   */
  def getCapture(grok: Grok,
                         patten: String,
                         text: String): Map[String, String] = {
    val gm = grok.`match`(text)
    val capture = gm.capture()
    val fields = getPatternFields(patten)
    val map = capture.asScala.filter(x => fields.contains(x._1))
    map.toMap.mapValues(_.asInstanceOf[String])
  }

  /**
   * 根据patten表达式返回字段列表
   * @param patten
   * @return
   */
  def getPatternFields(patten: String): mutable.MutableList[String] = {
    val arr = patten.split("%\\{")
    val fileds = new mutable.MutableList[String]
    for (item <- arr) {
      if (item.length>0) {
        val field = item.substring(item.indexOf(":") + 1, item.indexOf("}"))
        fileds.+=(field)
      }
    }
    fileds
  }

  def unwrapColumns(grokDF: DataFrame,
                    grokColumn: String,
                    fields: mutable.MutableList[String]): DataFrame = {
    val grokCol = grokDF(grokColumn)
    //grokDF.withColumn("httpversion", grokCol("httpversion")).show
    var unwrapedDF = grokDF
    fields.foreach(field => {
      unwrapedDF = unwrapedDF.withColumn(field, grokCol(field))
    })
    unwrapedDF.drop(grokColumn)
  }
}
