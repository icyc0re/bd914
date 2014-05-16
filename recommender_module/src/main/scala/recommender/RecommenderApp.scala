package recommender

import java.io.File
import scala.collection.mutable
import context._
import filtering.MockVectorSimilarity
import input.Checkins
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

    // Set USER_ID
    val user_id = args.isEmpty match {
      case true => 0
      case false => args(0)
    }
    if (args.length == 7) {
    // parse input arguments
    	Context.setContext(args)
    }
    
    println("compute recommendations for user: "+user_id);
    println("using dataset: "+Cons.VENUES_PATH)
    
    var u: Seq[UserVector] = mutable.MutableList.empty;
    var userInteractions: Map[String, Map[VenueListType.VenueListType, Seq[VenueVector]]] = Map.empty
    if (args.size == 1 && args(0).contains("precision")) {
      userInteractions = Precision.modifyUserInteractions(User.getAll)
      u = Precision.getUserVectorFromUserInteractions(userInteractions)
    } else if(args.size >= 2){
      val userJson = new File(Cons.NEW_USER_DIRECTORY+args(0));
      val user: String = scala.io.Source.fromFile(userJson).mkString
      val userVector: UserVector = new UserInputProcessor().processData(user)

      u :+ userVector;
    } else {
      u = UserVector.getAll
    }

    // get venue features
    var v: Seq[VenueVector] = VenueVector.getAll    
    
	// PREFILTERING - Do prefiltering if not calculating precision
    if(!(args.size == 1 && args(0).contains("precision")) && context != null) {
      // Plug in PreFiltering here once we have an actual context
      v = PreFilter.apply(v, context)
	}

    var similarities: Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v)
	
	// Do postfiltering if not calculating precision
    if(!(args.size == 1 && args(0).contains("precision"))) {
      similarities = PostFilter.applyPostFiltering(u, v, u.map(_ => context), similarities);
    }

    val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities)
    MockVectorSimilarity.printTopKSimilarities(sorted, 5)

    if(args.size == 1 && args(0).contains("precision")) {
      Precision.calculatePrecision(sorted, userInteractions,10)
    }
    //ResponseToWebApp.replyToWebApp(sorted, 3, 0)

    //write results to file
    
    if (user_id != 0 && user_id != "precision") {
//      val venues_id = List("3fd66200f964a52005e71ee3", "3fd66200f964a52008e81ee3", "3fd66200f964a52023eb1ee3",
//        "3fd66200f964a5200ae91ee3", "3fd66200f964a52015e51ee3")

	  val venuesIDs = MockVectorSimilarity.getTopKSimilaritiesForUserString(0, sorted, 5)
      //write the recommendation
      val writer_venues = new PrintWriter(new File(Cons.RECOMMENDATIONS_DIRECTORY + user_id))
      venuesIDs.split(",").foreach(x => writer_venues.write(x+'\n'))
      writer_venues.close()

      if(args.size >= 2) { //to write the features for the current user
        val writer_user = new PrintWriter(new File(Cons.RECOMMENDATIONS_DIRECTORY + user_id +"_profile"))
        u.foreach(x => writer_user.write("Id: "+x.getFeatureValue(Cons.USER_ID).get.toString+'\n'+
                                         "Popularity: "+x.getFeatureValue(Cons.POPULARITY).get.toString+'\n'+
                                         "Coordinates: "+context.getFeatureValue(Cons.GPS_COORDINATES).getOrElse(Cons.DEFAULT_COORDINATES).toString+'\n'+
                                         "Time: "+context.getFeatureValue(Cons.TIME).toString+'\n'))
        writer_user.close()
      }
    }
  }
}
