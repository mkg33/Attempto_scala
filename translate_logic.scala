import sys.process._
import scala.language.postfixOps
import scala.util.matching.Regex
import scala.io.Source

//import scala.util.parsing.combinator._

object parseLogic {

    //def getURL(url: String) = scala.io.Source.fromURL(url).mkString

    def aceToTptp(text: String) : Any = {
        var query = text.replaceAll("\\s", "+")
        val url = s"http://attempto.ifi.uzh.ch/ws/ape/apews.perl?text=$query&ctptp=on&solo=tptp"
        val result = s"curl $url" !
        //return getURL(s"http://attempto.ifi.uzh.ch/ws/ape/apews.perl?text=$query&ctptp=on&solo=tptp")
    }

    def getAceOutput(text: String, method: String) : String = {
        var query = text.replaceAll("\\s", "+")
        var lexiconURL = "https://raw.githubusercontent.com/mkg33/Attempto_scala/c8cdcf00395048be160a21fc7c23960f7570b557/lex.pl"
        val result = s"http://attempto.ifi.uzh.ch/ws/ape/apews.perl?text=$query&solo=$method&ulexfile=$lexiconURL"
        "http://attempto.ifi.uzh.ch/ws/ape/apews.perl?text=Z+is+not+a+monkey+.&solo=paraphrase"
        return result
        //val result = s"curl $lexiconURL" !
        //return getURL(s"http://attempto.ifi.uzh.ch/ws/ape/apews.perl?text=$query&ctptp=on&solo=syntax&ulexfile=$lexiconURL")
    }

    def checkOS() : String = {
        if (System.getProperty("os.name").startsWith("Windows")){
            return "Windows"
        } else {
            return "other"
        }
    }

    def getUserInput() : String = {
        var userText = scala.io.StdIn.readLine("APE text: ")

        if (!userText.endsWith(".")) {
            userText = userText + "."
        }

        return userText
    }

    def ACEparaphrase(userInput: String, ulexPath: String) : Array[String] = {
        val runAPEparaphrase = s"./ape.exe -text \"$userInput\" -cparaphrase -ulexfile $ulexPath" !!
        val paraphraseResult = new Regex("(?<=<paraphrase>)[\\s\\S]*?(?=</paraphrase>)")
        val finalParaphrase = paraphraseResult.findFirstIn(runAPEparaphrase).getOrElse("no match")
        val paraphraseArr = finalParaphrase.split("\n")

        return paraphraseArr
    }

    def ACEsyntax(paraphrase: Array[String], ulexPath: String) : Array[String] = {
        val syntaxOut = new Regex("(?<=<syntax>)[\\s\\S]*?(?=</syntax>)")
        val syntaxArr = Array.fill(paraphrase.length)("")
        
        for (i <- 0 until paraphrase.length) {
            println(paraphrase(i))
            val paraphraseTxt = paraphrase(i)
            val runAPEsyntax = s"./ape.exe -text \"$paraphraseTxt\" -csyntax -ulexfile $ulexPath" !!               
            val syntaxResult = syntaxOut.findFirstIn(runAPEsyntax).getOrElse("no match")
            syntaxArr(i) = syntaxResult
            println(syntaxArr(i))
        }

        return syntaxArr
    }

        def main(args: Array[String]) : Unit = {

            val opSys = checkOS()
            val filename = "resources/ulex.pl"
            val userInput = getUserInput()
            val paraphraseArr = ACEparaphrase(userInput, filename)
            val syntaxArr = ACEsyntax(paraphraseArr, filename)

            
           /* for (line <- Source.fromFile(filename).getLines) {
                println(line)
            }*/

            //var t = get("http://attempto.ifi.uzh.ch/ws/ape/apews.perl?text=Not+every+mortal+human+is+Socrates.&ctptp=on&solo=tptp");

            //print(t);

            // var t = aceToTptp("Soc is mortal.");

            // print(t);

            // val result = "bash ./ap.sh".!

            // > /dev/null 2>&1 not needed to suppress output

            //val apeText = "bash ap.sh" !!

            //println(apeText)

            //val res = getAceOutput("Every man reads a book or John plays the piano.", "paraphrase")

            //println(res)

           // var op = s"curl $res" !!

            //op = op.split("\\s+")

            //var opArr = op.split("\n")

            //print(op)

            //var synOut = ""
            //var synTree = ""


           // for (i <- 0 until opArr.length by 2) {
            //  println(opArr(i).replace("Every", "For all"))
            //  synOut = getAceOutput(opArr(i), "syntax")
            //}

            //var syn2 = s"curl $synOut" !!

            //println(syn2)

            //println(op.replace("[det,every]", "for all"))

            //println(op)

            //get(s"http://attempto.ifi.uzh.ch/ws/ape/apews.perl?text=The+sum+of+Z+and+X+is+good.&solo=syntax")

        }
}
