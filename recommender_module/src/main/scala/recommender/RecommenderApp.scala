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
    if (args.length == 6) {
    // parse input arguments
    	Context.setContext(args)
    }

    /*val user_id = args.isEmpty match {
      case true => 0
      case false => args(0)
    }
    if (args.length == 6) {
      val lat = args(1)
      val lng = args(2)
      val radius = args(3)
      val time1 = args(4) //min time
      val time2 = args(5) //max time
    }
    else {
      println("skip prefiltering");
    }*/
    
    println("compute recommendations for user: "+user_id);
    println("using dataset: "+Cons.VENUES_PATH)
    
    var u: Seq[UserVector] = mutable.MutableList.empty;
    var userInteractions: Map[String, Map[VenueListType.VenueListType, Seq[VenueVector]]] = Map.empty
    if (args.size == 1 && args(0).contains("precision")) {
      userInteractions = Precision.modifyUserInteractions(User.getAll)
      u = Precision.getUserVectorFromUserInteractions(userInteractions)
    } else {
      u = mutable.MutableList.empty;
    }

    // get venue features
    var v: Seq[VenueVector] = VenueVector.getAll
    if (args.size >= 2) {
      val userJson = new File(Cons.NEW_USER_DIRECTORY+args(0));
      val user: User = new User(scala.io.Source.fromFile(userJson).mkString);
      val userVector: UserVector = User.featureVector(user);

      val checkinsJson = new File(Cons.CHECKINS_DIRECTORY+args(0));
      val checkins: Checkins = new Checkins(scala.io.Source.fromFile(checkinsJson).mkString);
      // TODO create venue vectors from checkins and apply them to user vector

      u :+ userVector;
    } else {
      // get user features
      u = UserVector.getAll
    }

    // Plug in PreFiltering here once we have an actual context
//    val dummyContext: ContextVector = Context.grabTestContextVector(1)
//    v = PreFilter.apply(v, dummyContext)
    
    val context: ContextVector = Context.grab
    v = PreFilter.apply(v, context)


    val similarities: Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v)

    val newSimilarities = PostFilter.applyPostFiltering(u, v, u.map(_ => context), similarities);

    val sorted = MockVectorSimilarity.sortUserVenueSimilarities(newSimilarities)
    //val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities)
    MockVectorSimilarity.printTopKSimilarities(sorted, 5)

    if (args.size == 1 && args(0).contains("precision")) {
      Precision.calculatePrecision(MockVectorSimilarity.getTopKSimilarities(sorted, 10), userInteractions)
    }
    //ResponseToWebApp.replyToWebApp(sorted, 3, 0)

    //write results to file
    
    if (user_id != 0 && user_id != "precision") {
//      val venues_id = List("3fd66200f964a52005e71ee3", "3fd66200f964a52008e81ee3", "3fd66200f964a52023eb1ee3",
//        "3fd66200f964a5200ae91ee3", "3fd66200f964a52015e51ee3")

      val venuesIDs = MockVectorSimilarity.getTopKSimilaritiesForUserString(user_id.toString.toInt, sorted, 5)
      //write the recommendation
      val writer_venues = new PrintWriter(new File(Cons.RECOMMENDATIONS_DIRECTORY + user_id))
      venuesIDs.split(",").foreach(x => writer_venues.write(x+'\n'))
      writer_venues.close()

      if(args.size >= 2) { //to write the features for the current user
        val writer_user = new PrintWriter(new File(Cons.RECOMMENDATIONS_DIRECTORY + user_id +"_profile"))
        u.foreach(x => writer_user.write(x.getFeatureValue(Cons.USER_ID).toString()+' '+
                                         x.getFeatureValue(Cons.POPULARITY).toString()))
      }
    }
  }
}
