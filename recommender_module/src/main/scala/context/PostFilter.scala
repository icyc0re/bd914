package context

import vectors.AbstractVector
import vectors.VenueVector
import vectors.ContextVector
import scala.math
import features.DoubleFeature
import utils.Cons
import utils.Haversine

object PostFilter {

  //In construction: will be improved once we have the weights and functions for the features (sprlye)

  def computerRatingModificator(venue: VenueVector, context: ContextVector): Double = {

    //Initialize the weight vector with predefined weights for each feature

    var weights = new ContextVector(Seq(DoubleFeature("Location", 0.000001), DoubleFeature("Time", 0.000003)), Seq());

    //val weights = venue; //TODO: modify to put a vector of weights for each feature

    var sum = 0.0; //Will contain the rating modificator

    //For each of the feature, we compute it's contribution to the modification vector
    //Feature Price [Example]
    //sum += venue.getFeatureValue[Int]("Price").get * weights.getFeatureValue[Int]("Price").get



    //Feature Location [Example]
    //sum += math.sqrt(math.pow(venue.getFeatureValue[Int]("Location X").get - context.getFeatureValue[Int]("Location X").get,
    //  2) + math.pow(venue.getFeatureValue[Int]("Location Y").get - context.getFeatureValue[Int]("Location Y").get, 2)) * weights.getFeatureValue[Int]("Price").get

    //Etc

    //Add the location component
    //val distance = math.sqrt( math.pow(venue.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get._1 - context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get._1 , 2)  +  math.pow(venue.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get._2 - context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get._2 , 2)   )//Distance between the user and the venue
    val distance = Haversine.getDistance(venue.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get, context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get);

    sum -= weights.getFeatureValue[Double]("Location").get * distance;



    //Add the time component
    //If we are sure that the venue is open, we increase de ranking
    val increasingFactor = 2.0;
    var open = 0;

    //We check if the venue is open. If it is, we change "open" to 1.

    sum += weights.getFeatureValue[Double]("Time").get * increasingFactor * open;

    sum + venue.getFeatureValue[Double](Cons.FEATURE_RATING).get;
  }

  def apply(vectors: Seq[VenueVector], context: ContextVector): Unit = {
    //For each venue, we compute the rating modificator and then apply it.


    vectors.map((x: VenueVector) => {
      x.putFeature(DoubleFeature(Cons.FEATURE_RATING, computerRatingModificator(x, context)))
    })


  }

}