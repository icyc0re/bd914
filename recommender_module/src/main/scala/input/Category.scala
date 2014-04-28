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
  def getAll: Seq[String] = {
    categories match {
      case Nil => init(Cons.CATEGORIES_INPUT_PATH)
    }
    categories
  }

  /**
   * Load all categories
   * @param path path to file
   */
  private def init(path: String) = {
    categories = Array.empty
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
        similarity += categories(index) -> (categories(i) -> v)
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
      case Nil => initMatrix(Cons.CATEGORIES_MATRIX_INPUT_PATH)
    }

    similarity(category1)(category2)
  }
}
