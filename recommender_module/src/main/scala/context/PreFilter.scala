package context

import vectors.VenueVector
import vectors.ContextVector
import utils._
import input.Category
import input.VenuePrice

object PreFilter {

  def passesCriteria(venue: VenueVector, context: ContextVector): Boolean = {
    //Returns true is the venue passes the prefiltering criteria (and should be kept) or false otherwise (and should be deleted)

    // Location criterion:
    context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES) match {
      case Some(_) => {
        venue.getFeatureValue(Cons.GPS_COORDINATES) match {
          case Some(_) => {
            val distance = Haversine.getDistance(venue.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get, context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get)
            if (distance >= 2.0) {
              //Threshold in kilometers
              //println("NO :: distance")
              return false
            }
          }
          case None =>
        }
      }
      case None =>
    }

    // Time criterion:
    context.getFeatureValue[List[((Int, Int, Int), (Int, Int, Int))]](Cons.TIME) match {
      case Some(_) => {
        venue.getFeatureValue(Cons.TIME) match {
          case x if x.isEmpty =>
          case Some(_) => {
            val open = venue.isOpenUser(context.getFeatureValue[List[((Int, Int, Int), (Int, Int, Int))]](Cons.TIME).get);
            open match {
              //-1: No data, 0:The place is closed at the time the user asked (context vector), 1: the venue is open at that time
              case -1 =>
              case 0 => return false
              case 1 =>
            }
          }
          case None =>
        }
      }
      case None =>
    }

    // Categories criterion:
    context.getFeatureValue[Seq[String]](Cons.CATEGORY) match {
      case Some(_) => {
        venue.getFeatureValue[Seq[String]](Cons.CATEGORY).get.map((cat: String) => {
          try {
            if (Category.getCategoriesSimilarity(cat, context.getFeatureValue[String](Cons.CATEGORY).get) == 0) {
              //println("NO :: category")
              return false
            }
          }
          catch {
            case _: Exception => return false
          }
        })
      }
      case None =>
    }

    // Price criterion:
    context.getFeatureValue[VenuePrice](Cons.PRICE) match {
      case Some(contextPrice: VenuePrice) => {
        venue.getFeatureValue[VenuePrice](Cons.PRICE) match {
          case Some(x: VenuePrice) => x.tier match {
            case Some(p: Int) => if (p > contextPrice.tier.get) return false
            case None => //
          }
          case None => //
        }
      }
      case None =>
    }

    //println("Yay")
    true

  }

  // applies simple prefiltering to the feature-venue
  def apply(vectors: Seq[VenueVector], context: ContextVector): Seq[VenueVector] = {
    // for each f: Filterable in the context vector, run f.compareTo on the corresponding feature in vectors
    // context.filter(Filterable).map(c: ContextVector => {vf: VenueVector.Filterable-Feature c.compareTo(vf)}) ... don't know how to scala lol

    vectors.filter((v: VenueVector) => passesCriteria(v, context))
  }

}