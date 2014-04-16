package recommender

import input.MockAbstractInputProcessor
import input.Venue
import filtering.MockVectorSimilarity

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args:Array[String]){
    // todo
    /*val processor:MockAbstractInputProcessor = new MockAbstractInputProcessor

    val vectors = processor.processData(null)
    println(vectors)

    println(MockVectorSimilarity.calculateSimilarity(vectors(0), vectors(1)))*/


    var v : Venue = new Venue("/home/mat/Documents/Git/bd914/dataset/sample/venues/4a9fb11cf964a520343d20e3")
  }
}
