package recommender

import input._
import filtering.MockVectorSimilarity
import vectors.{UserVector, VenueVector, ContextVector}
import context._
import utils.Cons
import scala.collection.mutable

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

    // Pre Filtering:
//    val dummyContext = Context.grab() 
//    val dummyVenues  = Venue.getDummyVector()
//    PreFilter.apply(dummyVenues, dummyContext)
//    if (1==1) return
    
    //Map (user_id -> (venue -> similarity))
    var similarities = mutable.Map.empty[String, Map[String, Double]]
    for (user <- u){
      for (venue <- v){
      	val user_id = user.getFeatureValue[String](Cons.USER_ID).get
      	similarities += user_id -> (similarities.getOrElse(user_id, Map.empty) 
      	    + (venue.getFeatureValue[String](Cons.VENUE_ID).get -> MockVectorSimilarity.calculateSimilarity(user, venue)))
      }
    }
    	

    
    val topKVenues = users(0).getTopKVenues(5, v)
    // apply venue features to user vector
    //val newUsers = u.map((x:UserVector) => x.applyVenues(v))
    println(v)
    //println(u)
    println(topKVenues)
  }
}
