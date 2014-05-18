package recommender

import java.io.File
import context._
import filtering.MockVectorSimilarity
import input.User
import precision._
import vectors._
import java.io.PrintWriter
import utils.Cons
import input.UserInputProcessor

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args: Array[String]) {
    println("using dataset: " + Cons.VENUES_PATH)

    // Set USER_ID
    val user_id = args.isEmpty match {
      case true => 0
      case false => {
        println("compute recommendations for user: " + args(0))
        args(0)
      }
    }
    if (args.length == 7) {
      // parse input arguments
      Context.setContext(args)
    }

    // PARSE VENUES
    var v: Seq[VenueVector] = VenueVector.getAll
    // APPLY PRE-FILTERING if context args are passed
    val context: ContextVector = Context.grab()
    if (context != null) {
      println("Applying pre-filtering")
      v = PreFilter.apply(v, context)
    }
    println("venues size " + v.size)

    // GET USER IF PRECISION, IF PASSED AS ARGUMENT, OR ALL USERS
    var u: Seq[UserVector] = List.empty[UserVector]
    var userInteractions: Map[UserVector, Map[VenueListType.VenueListType, Seq[VenueVector]]] = Map.empty
    if (args.size == 1 && args(0).contains(Cons.PRECISION)) {
      userInteractions = Precision.modifyUserInteractions(User.getAll)
      u = Precision.getUserVectorFromUserInteractions(userInteractions)

    } else if (args.size >= 2) {
      val userJson = new File(Cons.NEW_USER_DIRECTORY + args(0))
      val user: String = scala.io.Source.fromFile(userJson).mkString
      val userVector: UserVector = new UserInputProcessor().processData(user)

      u = List(userVector)
    } else {
      u = UserVector.getAll
    }

    var similarities: Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v)
    similarities = context match {
      case null => similarities
      case _ => PostFilter.applyPostFiltering(u, v, u.map(_ => context), similarities)
    }

    val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities)
    val topK = MockVectorSimilarity.getTopKSimilarities(sorted, Cons.TOP_K_COUNT)
    MockVectorSimilarity.printTopKSimilarities(topK)

    if (args.size == 1 && args(0).contains(Cons.PRECISION)) {
      Precision.calculatePrecision(topK, userInteractions, Cons.TOP_K_COUNT)
    }
    else {
      //write results to file
      if (user_id != 0 && user_id != Cons.PRECISION) {
        val venuesIDs = MockVectorSimilarity.getTopKSimilaritiesForUserString(0, sorted, Cons.TOP_K_COUNT)
        //write the recommendation
        val writer_venues = new PrintWriter(new File(Cons.RECOMMENDATIONS_DIRECTORY + user_id))
        venuesIDs.split(",").foreach(x => writer_venues.write(x + '\n'))
        writer_venues.close()

        //to write the features for the current user
        val writer_user = new PrintWriter(new File(Cons.RECOMMENDATIONS_DIRECTORY + user_id + "_profile"))
        u.foreach(x => writer_user.write("Id: " + x.getFeatureValue(Cons.USER_ID).get.toString + '\n' +
          "Popularity: " + x.getFeatureValue(Cons.POPULARITY).get.toString + '\n' +
          "Coordinates: " + context.getFeatureValue(Cons.GPS_COORDINATES).getOrElse(Cons.DEFAULT_COORDINATES).toString + '\n' +
          "Time: " + context.getFeatureValue(Cons.TIME).toString + '\n'))
        writer_user.close()
      }
    }
  }
}
