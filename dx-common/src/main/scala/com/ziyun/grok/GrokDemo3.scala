package com.ziyun.grok

import com.ziyun.utils.GrokUtils
import io.krakens.grok.api.GrokCompiler

import java.io.{File, FileInputStream}
import scala.collection.JavaConverters._
import scala.collection.mutable

object GrokDemo3 {

  def main(args: Array[String]): Unit = {
    /* Create a new grokCompiler instance *//* Create a new grokCompiler instance */

      val grokCompiler = GrokUtils.createGrokCompiler()

    /* Grok pattern to compile, here httpd logs */
    val patten = "%{IPORHOST:addre} %{USER:ident} %{USER:auth} \\[%{HTTPDATE:timestamp}\\] \\\"%{WORD:http_method} %{NOTSPACE:request} HTTP/%{NUMBER:httpversion}\\\" %{NUMBER:status} (?:%{NUMBER:bytes}|-) \\\"(?:%{URI:http_referer}|-)\\\" \\\"%{GREEDYDATA:user_agent}\\\""
//    val grok = grokCompiler.compile("%{COMBINEDAPACHELOG}")

    /* Line of log to match */
    val log = "132.125.45.27 - - [19/Jul/2017:18:28:55 +0800] \"GET / HTTP/1.1\" 200 - \"http://www.baidu.com\" \"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36\""
//    val log = "112.169.19.192 - - [06/Mar/2013:01:36:30 +0900] \"GET / HTTP/1.1\" 200 44346 \"-\" \"Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_2) AppleWebKit/537.22 (KHTML, like Gecko) Chrome/25.0.1364.152 Safari/537.22\""

      val map = GrokUtils.getCompilerCapture(grokCompiler, patten, log)
      map.foreach(println)



  }
}
