package recommender

import filtering.MockVectorSimilarity
import vectors.{UserVector, VenueVector}
import scala.collection.mutable
import input.{Checkins, CheckinsCount}
import java.io.File
import input.Category

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
    
    // Plug in PreFiltering here
    // See usage in test.Test.testPreFiltering

    val similarities : Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v);
    val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities);
    MockVectorSimilarity.printTopKSimilarities(sorted, 5);

    // checkins parser test
    val file = new File("../dataset/sample/checkinstest.json")
    val response = new Checkins(scala.io.Source.fromFile(file).mkString)
    response.displayFeatures()
  }
}
