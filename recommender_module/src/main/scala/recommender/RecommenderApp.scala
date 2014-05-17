package recommender

import scala.collection.mutable
import filtering.MockVectorSimilarity
import input.User
import precision._
import vectors._

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args: Array[String]) {
    val start = System.currentTimeMillis()

    val v: Seq[VenueVector] = VenueVector.getAll

    var u: Seq[UserVector] = mutable.MutableList.empty
    var userInteractions: Map[String, Map[VenueListType.VenueListType, Seq[VenueVector]]] = Map.empty
    if (args.size == 1 && args(0).contains("precision")) {
      userInteractions = Precision.modifyUserInteractions(User.getAll)
      u = Precision.getUserVectorFromUserInteractions(userInteractions)
    }
    else {
      // get user features
      u = UserVector.getAll
    }

    val similarities: Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v)

    val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities)
    MockVectorSimilarity.printTopKSimilarities(sorted, 5)

    if (args.size == 1 && args(0).contains("precision")) {
      Precision.calculatePrecision(MockVectorSimilarity.getTopKSimilarities(sorted, 500000), userInteractions)
    }

    println("Recommender took [ms] = " + (System.currentTimeMillis() - start))
  }
}
