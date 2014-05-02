package context

import vectors.AbstractVector
import vectors.VenueVector
import vectors.ContextVector
import utils.{Cons, Haversine}

object PreFilter {

  def passesCriteria(venue: VenueVector, context: ContextVector): Boolean = {//Returns true is the venue passes the prefiltering criteria (and should be kept) or false otherwise (and should be deleted)

    var result = true;

    //Location criterion
    val distance = Haversine.getDistance(venue.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get, context.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get);
    result = result&&(distance < 2.0)//Threshold in kilometers

    result
  }

    // applies simple prefiltering to the feature-venue
  def apply(vectors: Seq[VenueVector], context: ContextVector): Unit = {
	// for each f: Filterable in the context vector, run f.compareTo on the corresponding feature in vectors
    // context.filter(Filterable).map(c: ContextVector => {vf: VenueVector.Filterable-Feature c.compareTo(vf)}) ... don't know how to scala lol

      vectors.filter( (v:VenueVector) => passesCriteria(v, context))


  }

}