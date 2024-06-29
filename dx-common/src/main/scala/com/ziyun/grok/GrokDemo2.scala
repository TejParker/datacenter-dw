package com.ziyun.grok

import io.krakens.grok.api.GrokCompiler

import java.io.{File, FileInputStream}
import scala.collection.JavaConverters._
import scala.collection.mutable

object GrokDemo2 {

  def main(args: Array[String]): Unit = {
    /* Create a new grokCompiler instance *//* Create a new grokCompiler instance */

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

    /* Grok pattern to compile, here httpd logs */
    val patten = "%{IPORHOST:addre} %{USER:ident} %{USER:auth} \\[%{HTTPDATE:timestamp}\\] \\\"%{WORD:http_method} %{NOTSPACE:request} HTTP/%{NUMBER:httpversion}\\\" %{NUMBER:status} (?:%{NUMBER:bytes}|-) \\\"(?:%{URI:http_referer}|-)\\\" \\\"%{GREEDYDATA:user_agent}\\\""
    val grok = grokCompiler.compile(patten)
//    val grok = grokCompiler.compile("%{COMBINEDAPACHELOG}")

    /* Line of log to match */
    val log = "132.125.45.27 - - [19/Jul/2017:18:28:55 +0800] \"GET / HTTP/1.1\" 200 - \"http://www.baidu.com\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36\""
//    val log = "112.169.19.192 - - [06/Mar/2013:01:36:30 +0900] \"GET / HTTP/1.1\" 200 44346 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.152 Safari/537.22\""

    val gm = grok.`match`(log)

    /* Get the map with matches */
    val capture = gm.capture.asScala
    //capture.foreach(x => println(x._1 + ":" + x._2))

    val arr = patten.split("%\\{")
    val fileds = new mutable.MutableList[String]
    for (item <- arr) {
      if (item.length>0) {
        val field = item.substring(item.indexOf(":") + 1, item.indexOf("}"))
        fileds.+=(field)
      }
    }
    capture.filter(x => fileds.contains(x._1))
      .foreach(println)


  }
}
