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
    println(vectors)
    
    // process the context data into
    val context = null
    
    // fetch user data into
    val user = null
    	
    PreFilter.apply(vectors, context, user)
    println(MockVectorSimilarity.calculateSimilarity(vectors(0), vectors(1)))
    PostFilter.apply(vectors, context)
  }
}
