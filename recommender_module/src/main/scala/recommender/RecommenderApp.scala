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

    //example on how to extract feature from a JSON file 
    val filePath : String = "venue_test"
    val venue : Venue = new Venue(filePath)
    venue.displayFeatures()
    println("\nIt's easy to extract features : "+venue.name+"\t"+venue.likes+"\n")

    val processor:MockAbstractInputProcessor = new MockAbstractInputProcessor

    val vectors = processor.processData(null)
    println(vectors)

    println(MockVectorSimilarity.calculateSimilarity(vectors(0), vectors(1)))
  }
}
