package input

import utils.Cons

/**
 * Keeps list of all categories and similarity between any two categories
 * @author Ivan Gavrilović
 */
object Category {
  var categories: Seq[String] = Nil
  var similarity: Map[String, Map[String, Double]] = Map.empty

  /**
   * Get list of all categories
   * @return
   */
  def allCats: Seq[String] = {
    categories match {
      case Nil => init(Cons.CATEGORIES_INPUT_PATH)
      case _ => //nothing
    }
    categories
  }

  /**
   * Load all categories
   * @param path path to file
   */
  private def init(path: String) = {
    categories = Array.empty[String]
    for (line <- scala.io.Source.fromFile(path).getLines()) {
      categories :+= line
    }
  }

  /**
   * Load similarity matrix for categories
   * @param path path to file
   */
  private def initMatrix(path: String) = {
    similarity = Map.empty
    for ((line, index) <- scala.io.Source.fromFile(path).getLines().zipWithIndex) {
      val values: Seq[Double] = line.split(" ").map(_.toDouble)

      for ((v, i) <- values.zipWithIndex){
        similarity += allCats(index) -> (similarity.getOrElse(allCats(index), Map.empty) + (allCats(i) -> v))
      }
    }
  }

  /**
   * Get similarity for two categories
   * @param category1 first category
   * @param category2 second category
   * @return similarity value
   */
  def getCategoriesSimilarity(category1: String, category2: String): Double = {
    similarity match {
      case x if similarity.isEmpty => initMatrix(Cons.CATEGORIES_MATRIX_INPUT_PATH)
      case _ => // nothing
    }

    similarity.get(category1) match {
      case Some(x:Map[String, Double]) => x.get(category2) match {
        case Some(v:Double) => v
        case None => throw new Exception("No such category: "+category2)
      }
      case _ => throw new Exception("No such category: "+category1)
    }
  }
}
