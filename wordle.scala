// Main Part 2 about Evil Wordle
//===============================


object M2 { 

import io.Source
import scala.util._

// ADD YOUR CODE BELOW
//======================


//(1)
def get_wordle_list(url: String): List[String] = {
  try {
    val source = Source.fromURL(url)
    val words = source.getLines().toList
    source.close()
    words.filter(_.length == 5)  
  } catch {
    case e: Exception =>
      println(s"Error fetching word list: ${e.getMessage}")
      List()  
  }
}

// val secrets = get_wordle_list("https://nms.kcl.ac.uk/christian.urban/wordle.txt")
// secrets.length // => 12972
// secrets.filter(_.length != 5) // => Nil

//(2)
def removeN[A](xs: List[A], elem: A, n: Int): List[A] = {
  def removeHelper(lst: List[A], elem: A, count: Int): List[A] = lst match {
    case Nil => Nil 
    case x :: xs if x == elem && count > 0 => 
      removeHelper(xs, elem, count - 1) 
    case x :: xs => 
      x :: removeHelper(xs, elem, count) 
  }

  removeHelper(xs, elem, n)
}


// removeN(List(1,2,3,2,1), 3, 1)  // => List(1, 2, 2, 1)
// removeN(List(1,2,3,2,1), 2, 1)  // => List(1, 3, 2, 1)
// removeN(List(1,2,3,2,1), 1, 1)  // => List(2, 3, 2, 1)
// removeN(List(1,2,3,2,1), 0, 2)  // => List(1, 2, 3, 2, 1)

// (3)
abstract class Tip
case object Absent extends Tip
case object Present extends Tip
case object Correct extends Tip


def pool(secret: String, word: String): List[Char] = {
  secret.zip(word).collect { case (s, w) if s != w => s }.toList
}

def aux(secret: List[Char], word: List[Char], pool: List[Char]): List[Tip] = (secret, word) match {
  case (Nil, Nil) => Nil 
  case (s :: ss, w :: ws) =>
    if (s == w) { 
      Correct :: aux(ss, ws, pool)
    } else if (pool.contains(w)) { 
      Present :: aux(ss, ws, pool.diff(List(w)))
    } else { 
      Absent :: aux(ss, ws, pool)
    }
}


def score(secret: String, word: String): List[Tip] = {
  aux(secret.toList, word.toList, pool(secret, word))
}


// score("chess", "caves") // => List(Correct, Absent, Absent, Present, Correct)
// score("doses", "slide") // => List(Present, Absent, Absent, Present, Present)
// score("chess", "swiss") // => List(Absent, Absent, Absent, Correct, Correct)
// score("chess", "eexss") // => List(Present, Absent, Absent, Correct, Correct)

// (4)
def eval(t: Tip) : Int = {
  t match{
    case Correct=>10
    case Present=>1
    case Absent=>0
  }
}

def iscore(secret: String, word: String) : Int = {
  score(secret, word).map(eval).sum
}

//iscore("chess", "caves") // => 21
//iscore("chess", "swiss") // => 20

// (5)
def lowest(secrets: List[String], word: String, current: Int, acc: List[String]): List[String] = {
  secrets match {
    case Nil => acc 
    case s :: ss =>
      val scoreValue = iscore(s, word) 
      if (scoreValue < current) {
        lowest(ss, word, scoreValue, List(s))
      } else if (scoreValue == current) {
        lowest(ss, word, current, s :: acc)
      } else {
        lowest(ss, word, current, acc)
      }
  }
}

def evil(secrets: List[String], word: String) : List[String] = {
  lowest(secrets, word, Int.MaxValue, List())
}


//evil(secrets, "stent").length
//evil(secrets, "hexes").length
//evil(secrets, "horse").length
//evil(secrets, "hoise").length
//evil(secrets, "house").length

// (6)
def frequencies(secrets: List[String]): Map[Char, Double] = {
  val allChars = secrets.mkString.groupBy(_.toLower).mapValues(_.length.toDouble)
  allChars.mapValues(freq => 1 - (freq / allChars.values.sum)).toMap
}

// (7)
def rank(frqs: Map[Char, Double], s: String): Double = {
    s.toLowerCase.foldLeft(0.0) { (acc, char) =>
      acc + frqs.getOrElse(char, 0.0)
    }
}

def ranked_evil(secrets: List[String], word: String) : List[String] = {
  
    val evilWords = evil(secrets, word)

    val freqMap = frequencies(secrets)

    val rankedWords = evilWords.map(w => (w, rank(freqMap, w)))

    
    val maxRank = rankedWords.map(_._2).max
    
    rankedWords.filter(_._2 == maxRank).map(_._1)
}



}
