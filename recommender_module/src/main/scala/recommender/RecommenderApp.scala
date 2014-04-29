package recommender

import filtering.MockVectorSimilarity
import vectors.{UserVector, VenueVector}
import utils.Cons
import scala.collection.mutable

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args: Array[String]) {
    // get venue features
    val v: Seq[VenueVector] = VenueVector.getAll
    // get user features
    val u: Seq[UserVector] = UserVector.getAll

    // Pre Filtering:
    //    val dummyContext = Context.grab()
    //    val dummyVenues  = Venue.getDummyVector()
    //    PreFilter.apply(dummyVenues, dummyContext)
    //    if (1==1) return

    var similarities = mutable.Map.empty[String, Seq[(String, Double)]]
    for (user <- u) {
      for (venue <- v) {
        val user_id = user.getFeatureValue[String](Cons.USER_ID).get
        similarities += user_id -> (similarities.getOrElse(user_id, List.empty)
          :+ (venue.getFeatureValue[String](Cons.VENUE_ID).get -> MockVectorSimilarity.calculateSimilarity(user, venue)))
      }
    }

    //Sort by value
    for ((user, values) <- similarities) {
      similarities += user -> values.toSeq.sortBy(-_._2)
    }

    for ((user, values) <- similarities) {
      println("user - value [0, 1, 2] " + user + " " + values(0)._2 + " " + values(1)._2 + " " + values(2)._2)
    }
  }
}
