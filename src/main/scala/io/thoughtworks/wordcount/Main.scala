package io.thoughtworks.wordcount

import java.io.File
import java.net.URL

import scala.io.Source
import scala.util.Try

object Main {

  def main(args: Array[String]): Unit = {
    print("hello world")
  }
}

object Main2 extends App {
  val wordCount = new WordCount
  val input = Try {
    Source.fromURL(this.getClass.getClassLoader.getResource("news.txt")).getLines.toList.mkString
  }.toEither

  input
    .map(wordCount.listWords)
    .fold(
      error => println(error),
      _.reverse.take(10).foreach { case WordFrequency(word, counts) => println(s"$word $counts") }
    )
}

object Main3 extends App {
//  val wordCount = new WordCount
//  val path      = sys.env("INPUT2")
//  if (path.startsWith("URL")) {
//    val index  = path.indexOf(":")
//    val url    = path.substring(index + 1)
//    val result = Source.fromURL(new URL(url)).getLines().toList.mkString
//    println(result)
//  } else if (path.startsWith("FILE")) {
//    val index  = path.indexOf(":")
//    val url    = path.substring(index + 1)
//    println(url)
//  }

//  val wordCount = new WordCount
//  val source = sys.env.get("INPUT") match {
//    case Some(input) => input
//    case None => throw(new Throwable("The input env does not exist."))
//  }
//  val index = source.indexOf(":")
//  val typeValue = source.substring(0, index);
//  val path = source.substring(index + 1);
//  var text = if(typeValue.equals("URL")) {
//    Try {
//      Source.fromURL(new URL(path)).getLines().toList.mkString
//    }.toEither
//  } else {
//    Try {
//      Source.fromFile(new File(path)).getLines().toList.mkString
//    }.toEither
//  }
//  text.map(wordCount.listWords).fold(
//    error => println(error),
//    _.reverse.take(10).foreach { case WordFrequency(word, counts) => println(s"$word $counts") }
//  )

  implicit val urlTransformer: Transformer[URL] = new Transformer[URL] {
    override def transform(input: String): URL = new URL(input)
  }

  implicit val fileTransformer: Transformer[File] = new Transformer[File] {
    override def transform(input: String): File = new File(input)
  }

  implicit class StringOps(input: String) {
    def as[T](implicit transformer: Transformer[T]):T = transformer.transform(input)
  }

  val wordCount = new WordCount
  val input = Try {
    sys.env.getOrElse("INPUT", "INVALID").split(":").toList match {
      case "FILE" :: file :: Nil => Source.fromFile(file.as[File]).getLines().toList.mkString
      case "URL" :: tail         => Source.fromURL(tail.mkString(":").as[URL]).getLines().toList.mkString
      case _        => throw (new Throwable("The input env does not exist."))
    }
  }

  input
    .map(wordCount.listWords)
    .fold(
      error => println(error),
      _.reverse.take(10).foreach { case WordFrequency(word, counts) => println(s"$word $counts") }
    )
}

trait Transformer[T] {
  def transform(input: String): T //Either[Throwable, URL]
}