package filtering

import vectors.AbstractVector

/**
 * Classes calculating the similarity between vectors should implement this
 * @author Ivan Gavrilović
 */
trait VectorSimilarity {
  /**
   * Get the similarity between two vectors
   * @param fst first item
   * @param snd second item
   * @return similarity expressed as { @code double}
   */
  def calculateSimilarity(fst: AbstractVector, snd: AbstractVector): Double

  /**
   * Get the similarity between two collections of vectors
   * @param fst first collection of vectors
   * @param snd second collection of vectors
   * @return list of similarities between vectors
   */
  def calculateSimilarity(fst: Seq[AbstractVector], snd: Seq[AbstractVector]): Seq[Double]
}
