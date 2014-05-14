package context

import vectors.{UserVector, AbstractVector, VenueVector, ContextVector}
import scala.math
import features.DoubleFeature
import utils.Cons
import utils.Haversine
import scala.collection.mutable

object PostFilter {

  //In construction: will be improved once we have the weights and functions for the features (sprlye)

  def computerRatingModificator(venue: VenueVector, context: ContextVector, similarity: Double): Double = {

    var new_similarity = similarity //the final new rating that will be returned by the function
    val w_distance = 0.1            //weight to control the importance of the distance to the venue
    val w_time = 0.1
    //Initialize the weight vector with predefined weights for each feature



    //Add the location component
   val distance = Haversine.getDistance(venue.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get, context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get);

    new_similarity *= w_distance*1/(distance + 1e-5);

//Add the time component
    val timeFactor =   1  // Time criterion:
  context.getFeatureValue[List[((Int, Int, Int), (Int, Int, Int))]](Cons.TIME) match {
    case Some(_) => {
      venue.getFeatureValue(Cons.TIME) match {
        case x if x.isEmpty =>
        case Some(_) => {
          val open = venue.isOpenUser(context.getFeatureValue[List[((Int, Int, Int), (Int, Int, Int))]](Cons.TIME).get);
          open match{//-1: No data, 0:The place is closed at the time the user asked (context vector), 1: the venue is open at that time
            case -1 => 1
            case 0 => 1.1
            case 1 => 0.9
          }
        }
        case None => 1
      }
    }
    case None => 1
  }

    new_similarity *= w_time*timeFactor;
    new_similarity

  }

  def apply(vectors: Seq[VenueVector], context: ContextVector, similarities: Seq[Double]): Unit = {
    //For each venue, we compute the rating modificator and then apply it.

    var newSimilarities : Seq[Double] = { vectors zip similarities map(x => {
      computerRatingModificator(x._1, context, x._2)
    })
    }
  }

  def applyPostFiltering(users: Seq[UserVector], venues: Seq[VenueVector], contexts: Seq[ContextVector], similarities: Seq[(String, Seq[(String, Double)])]) : Seq[(String, Seq[(String, Double)])] = {
    var similarities: mutable.Seq[(String, Seq[(String, Double)])] = mutable.MutableList.empty
    for ((user,context) <- users zip contexts) {
      val sim : Seq[Double] = similarities.map( x => x._1 match {case user_id => x._2.map(a => a._2) }).flatten
      val user_id = user.getFeatureValue[String](Cons.USER_ID).get
      var rankings: mutable.Seq[(String, Double)] = mutable.MutableList.empty
      val start = System.currentTimeMillis
      for ((venue,s) <- venues zip sim) {
        val venue_id = venue.getFeatureValue[String](Cons.VENUE_ID).get
        //val s : Double = similarities.map( x => x._1 match {case user_id => x._2.map(a => a._1 match {case venue_id => computerRatingModificator(venue, context, a._2) }) }).flatten
        rankings :+=(venue_id, computerRatingModificator(venue, context, s))
      }
      similarities :+=(user_id, rankings)
      println("Id = " + user.getFeatureValue[String](Cons.USER_ID).get + "; took ms = " + (System.currentTimeMillis() - start) + "; num user categories = " + user.getFeatureValue[Seq[String]](Cons.CATEGORY).get)
    }
    similarities.toList
  }

}