package filtering

import vectors.AbstractVector
import features.IntFeature

/**
 * @author Ivan Gavrilović
 */
object MockVectorSimilarity extends VectorSimilarity {
  /**
   * Get the similarity between two collections of vectors
   * @param fst first collection of vectors
   * @param snd second collection of vectors
   * @return list of similarities between vectors
   */
  def calculateSimilarity(fst: Seq[AbstractVector], snd: Seq[AbstractVector]): Seq[Double] = {
    if (fst.size != snd.size) throw new Exception("Two vector sets do not have the same length!")

    (fst, snd).zipped.map {
      case (x, y) => calculateSimilarity(x, y)
    }
  }

  /**
   * Get the similarity between two vectors
   * @param fst first item
   * @param snd second item
   * @return similarity expressed as { @code double}
   */
  def calculateSimilarity(fst: AbstractVector, snd: AbstractVector): Double = {

    // get (x0*y0 + x1*y1 + ... + x_n * y_n)
    val dotProd = (fst.getFeaturesTyped[IntFeature](), snd.getFeaturesTyped[IntFeature]()).zipped.foldRight(0) {
      (x: (IntFeature, IntFeature), b: Int) =>
        b + x._1.value * x._2.value
    }

    // get ||x|| and ||y||
    val intensityFst = Math.sqrt(fst.getFeaturesTyped[IntFeature]().foldRight(0)((x:IntFeature, b:Int) => b + x.value*x.value))
    val intensitySnd = Math.sqrt(snd.getFeaturesTyped[IntFeature]().foldRight(0)((x:IntFeature, b:Int) => b + x.value*x.value))

    dotProd / (intensityFst * intensitySnd)
  }
}
