package recommender

import input.MockAbstractInputProcessor
import filtering.MockVectorSimilarity

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp{
  def main(args:Array[String]){
    // todo
    val processor:MockAbstractInputProcessor = new MockAbstractInputProcessor

    val vectors = processor.processData(null)
    println(vectors)

    println(MockVectorSimilarity.calculateSimilarity(vectors(0), vectors(1)))
  }
}
