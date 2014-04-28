package context

import vectors.AbstractVector
import vectors.VenueVector
import vectors.ContextVector

object PreFilter {
  
  
  // applies simple prefiltering to the feature-venue
  def apply(vectors: Seq[VenueVector], context: ContextVector): Unit = {
	// for each f: Filterable in the context vector, run f.compareTo on the corresponding feature in vectors
    // context.filter(Filterable).map(c: ContextVector => {vf: VenueVector.Filterable-Feature c.compareTo(vf)}) ... don't know how to scala lol
  }

}