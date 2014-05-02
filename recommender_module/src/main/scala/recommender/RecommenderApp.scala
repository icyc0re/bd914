package recommender

import filtering.MockVectorSimilarity
import input.{User, Checkins, CheckinsCount}
import java.io.File
import input.Category
import weboutput.{ResponseToWebApp}
import javax.print.attribute.standard.ReferenceUriSchemesSupported
import scalaj.http.Http
import scala.collection.mutable
import vectors._
import context._

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args: Array[String]) {
    var u : Seq[UserVector] = mutable.MutableList.empty;
    // get venue features
    var v: Seq[VenueVector] = VenueVector.getAll
    if(args.size == 2){
      val userJson = new File(args(0));
      val user : User = new User(scala.io.Source.fromFile(userJson).mkString);
      val userVector : UserVector = User.featureVector(user);

      val checkinsJson = new File(args(1));
      val checkins : Checkins = new Checkins(scala.io.Source.fromFile(checkinsJson).mkString);
      // TODO create venue vectors from checkins and apply them to user vector

      u :+ userVector;
    } else {
      // get user features
      u = UserVector.getAll

      // checkins parser test
      //val file = new File("../dataset/sample/checkinstest.json")
      //val response = new Checkins(scala.io.Source.fromFile(file).mkString)
      //response.displayFeatures()
    }
    
    // Plug in PreFiltering here once we have an actual context
    val dummyContext: ContextVector = Context.grab()
    v = PreFilter.apply(v, dummyContext)

    val similarities : Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v);
    val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities);
    MockVectorSimilarity.printTopKSimilarities(sorted, 5);

    //ResponseToWebApp.replyToWebApp(sorted, 3, 0);
  }
}
