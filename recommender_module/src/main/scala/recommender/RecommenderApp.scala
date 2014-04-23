package recommender

import input._
import filtering.MockVectorSimilarity
import vectors.{UserVector, VenueVector}

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

    // get venue features
    val v : Seq[VenueVector] = new VenueInputProcessor().processInDir("../dataset/sample/venues/")
    // get user features
    val u : Seq[UserVector] = new UserInputProcessor().processInDir("../dataset/sample/users/")
    // apply venue features to user vector
    val newUsers = u.map((x:UserVector) => x.applyVenues(v))
    println(v)
    println(u)
  }
}
