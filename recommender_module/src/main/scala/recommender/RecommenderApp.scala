package recommender

import filtering.MockVectorSimilarity
import input.MockAbstractInputProcessor
import input.Venue
import input.User

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args:Array[String]){
    // todo

    //example on how to extract feature from a JSON file 
    val venue : Venue = new Venue("../dataset/sample/venues/4a9fb11cf964a520343d20e3")
    val user : User = new User("../dataset/sample/users/8550439")
    //venue.displayFeatures()
    //println("\nIt's easy to extract features : "+venue.name+"\t"+venue.likes+"\n")

    val processor:MockAbstractInputProcessor = new MockAbstractInputProcessor

    val vectors = processor.processData(null)
    println(vectors)
    
    println(venue.categories)
    println(user.checkins.count)

    println(MockVectorSimilarity.calculateSimilarity(vectors(0), vectors(1)))
  }
}
