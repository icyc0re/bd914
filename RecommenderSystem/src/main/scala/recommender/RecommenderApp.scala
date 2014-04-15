package recommender

import input.MockAbstractInputProcessor
import filtering.MockVectorSimilarity
import context.PostFilter
import context.PreFilter

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args: Array[String]): Unit = {
    // todo
    val processor:MockAbstractInputProcessor = new MockAbstractInputProcessor
    
    // Recommender pipeline:
    val vectors = processor.processData(null)
    val context = Vector(0,1,2);//The context vector has to be computed
    println(vectors)

    PreFilter.apply(vectors)
    println(MockVectorSimilarity.calculateSimilarity(vectors(0), vectors(1)))
    //PostFilter.apply(vectors, context) <= vectors has to be a Seq[VenueVector] here (once this transformation is done, this line can be uncommented to use the postfiltering)
  }
}
