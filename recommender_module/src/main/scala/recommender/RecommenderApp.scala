package recommender

import filtering.MockVectorSimilarity
import vectors.{UserVector, VenueVector}
import scala.collection.mutable
import input.{Checkins, CheckinsCount}
import java.io.File

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

    val similarities : Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v);
    val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities);
    MockVectorSimilarity.printTopKSimilarities(sorted, 5);

    // checkins parser test
    val file = new File("../dataset/sample/checkinstest.json")
    val response = new Checkins(scala.io.Source.fromFile(file).mkString)
    response.displayFeatures()
  }
}
