package io.thoughtworks.wordcount

case class WordFrequency(word: String, counts: Int)

class WordCount {
  def listWords(text: String): List[WordFrequency] =
    (extractWords _)
      .andThen(countWords)
      .apply(text)
      .map {
        case (word, count) => WordFrequency(word, count)
      }
      .toList
      .sortBy(_.counts)

//  def listWords(text: String): List[WordFrequency] =
//    countWords(extractWords(text)).toList
//      .map {
//        case (word, count) =>
//          WordFrequency(word, count)
//      }
//      .sortBy(_.counts)

  def countWords(words: List[String]): Map[String, Int] =
    words.groupBy(identity).map { case (word, value) => (word, value.size) }

  def extractWords(text: String): List[String] =
    text
      .replace(",", "")
      .replace("-", " ")
      .replace("\"", "")
      .replace(".", "")
      .toLowerCase
      .split("\\s+")
      .filter(_.nonEmpty)
      .toList
}
