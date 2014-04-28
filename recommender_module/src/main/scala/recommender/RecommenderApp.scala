package recommender

import input._
import filtering.MockVectorSimilarity
import vectors.{UserVector, VenueVector, ContextVector}
import context._

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
    //val u : Seq[UserVector] = new UserInputProcessor().processInDir("../dataset/sample/users/")
    // get users
    val users : Seq[User] = new UserInputProcessor().processUsersInDir("../dataset/sample/users/")
    
    // Pre Filtering:
    val context = Context.grab()
    PreFilter.apply(v, context)
    if (1==1) return
    
    val topKVenues = users(0).getTopKVenues(5, v)
    // apply venue features to user vector
    //val newUsers = u.map((x:UserVector) => x.applyVenues(v))
    println(v)
    //println(u)
    println(topKVenues)
  }
}
