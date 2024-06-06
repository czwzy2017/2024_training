import scala.io.StdIn

case class TextAnalyzer(var input: String) {
    val words = input.toLowerCase.split("\\W+")

    def wordCount: Int = {
        words.length
    }

    def upperCaseLetters: Int = {
        input.count(_.isUpper)
    }

    def uniqueWordsSorted: Array[String] = {
        words.distinct.sorted
    }

    def containsWord(word: String): Boolean = {
        words.contains(word.toLowerCase)
    }
}

object Main {
    def isEnglish(input: String): Boolean = {
        val englishPattern = "^[a-zA-Z0-9 .,!?;:'\"()-]*$"
        input.matches(englishPattern)
    }

    def main(args: Array[String]): Unit = {
        val input = StdIn.readLine()
        //        val input = "Hello World! This is a Test Sentence for Programming Exercise."
        if (isEnglish(input)) {
            val textAnalyzer = TextAnalyzer(input)
            println(s"单词数量：${textAnalyzer.wordCount}")
            println(s"大写字母数量：${textAnalyzer.upperCaseLetters}")
            println(s"不重复的单词列表：${textAnalyzer.uniqueWordsSorted.mkString("[\"", "\",\"", "\"]")}")
            println("请输入一个单词，来判断是否存在句子中(不区分大小写），退出请输入\"#\"")
            var word = StdIn.readLine()
            while (word != "#") {
                println("是否存在\"" + word + "\":" + textAnalyzer.containsWord(word))
                word = StdIn.readLine()
            }
        } else {
            println("输入的不是英文句子")
        }
    }
}