package context

import vectors.AbstractVector
import vectors.VenueVector
import vectors.ContextVector
import utils._
import input.Category
import features.CategoryFeature
import input.VenuePrice

object PreFilter {

  def passesCriteria(venue: VenueVector, context: ContextVector): Boolean = { //Returns true is the venue passes the prefiltering criteria (and should be kept) or false otherwise (and should be deleted)

    // Location criterion:
    context.getFeatureValue(Cons.GPS_COORDINATES) match {
      case Some(_) => {
        venue.getFeatureValue(Cons.GPS_COORDINATES) match {
          case Some(_) => {
            val distance = Haversine.getDistance(venue.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get, context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get)
            if (distance >= 2.0) { //Threshold in kilometers
              println("NO :: distance")
              return false
            }
          }
          case None =>
        }
      }
      case None =>
    }

    // Categories criterion:
    context.getFeatureValue(Cons.CATEGORY) match {
      case Some(_) => {
        venue.getFeatureValue[Seq[String]](Cons.CATEGORY).get.map((cat: String) => {
          if (Category.getCategoriesSimilarity(cat, context.getFeatureValue[String](Cons.CATEGORY).get) == 0) {
            println("NO :: category")
            return false
          }
        })
      }
      case None =>
    }
    
    // Price criterion:
    context.getFeatureValue(Cons.PRICE) match {
      case Some(_) => {
        if (venue.getFeatureValue[VenuePrice](Cons.PRICE).get.tier.get > context.getFeatureValue[VenuePrice](Cons.PRICE).get.tier.get) {
          println("NO :: price")
          return false
        }
      }
      case None =>
    }

    println("Yay")
    true

  }

  // applies simple prefiltering to the feature-venue
  def apply(vectors: Seq[VenueVector], context: ContextVector): Seq[VenueVector] = {
    // for each f: Filterable in the context vector, run f.compareTo on the corresponding feature in vectors
    // context.filter(Filterable).map(c: ContextVector => {vf: VenueVector.Filterable-Feature c.compareTo(vf)}) ... don't know how to scala lol

    vectors.filter((v: VenueVector) => passesCriteria(v, context))
  }

}