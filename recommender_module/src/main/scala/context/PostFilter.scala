package context

import vectors.AbstractVector
import vectors.VenueVector
import scala.math
import features.DoubleFeature

object PostFilter {

  //In construction: will be improved once we have the weights and functions for the features (sprlye)

  def computerRatingModificator(venue: VenueVector, context: AbstractVector): Double = {

    //Initialize the weight vector with predefined weights for each feature
    val weights = venue; //TODO: modify to put a vector of weights for each feature

    var sum = 0.0; //Will contain the rating modificator

    //For each of the feature, we compute it's contribution to the modification vector
    //Feature Price
    sum += venue.getFeatureValue[Int]("Price").get * weights.getFeatureValue[Int]("Price").get



    //Feature Location
    sum += math.sqrt(math.pow(venue.getFeatureValue[Int]("Location X").get - context.getFeatureValue[Int]("Location X").get,
      2) + math.pow(venue.getFeatureValue[Int]("Location Y").get - context.getFeatureValue[Int]("Location Y").get, 2)) * weights.getFeatureValue[Int]("Price").get

    //Etc

    sum
  }

  def apply(vectors: Seq[VenueVector], context: AbstractVector): Unit = {
    //For each venue, we compute the rating modificator and then apply it.

    //Idea: (TODO: implement it...)
    vectors.map((x: VenueVector) => {
      x.putFeature(DoubleFeature("Rating", computerRatingModificator(x, context)))
    })


  }

}