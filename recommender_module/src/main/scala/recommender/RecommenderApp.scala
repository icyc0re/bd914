package recommender

import input._
import filtering.MockVectorSimilarity
import vectors.{UserVector, VenueVector, ContextVector}
import context._
import utils.Cons

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
    val v : Seq[VenueVector] = VenueVector.getAll
    // get user features
    val u : Seq[UserVector] = UserVector.getAll
    // get users
    val users : Seq[User] = new UserInputProcessor().processUsersInDir(Cons.USERS_PATH)
    
    // Pre Filtering:
//    val dummyContext = Context.grab() 
//    val dummyVenues  = Venue.getDummyVector()
//    PreFilter.apply(dummyVenues, dummyContext)
//    if (1==1) return
    
    val topKVenues = users(0).getTopKVenues(5, v)
    // apply venue features to user vector
    //val newUsers = u.map((x:UserVector) => x.applyVenues(v))
    println(v)
    //println(u)
    println(topKVenues)
  }
}
