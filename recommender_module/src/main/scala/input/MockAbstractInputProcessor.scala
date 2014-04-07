package input

import scala.io.BufferedSource
import vectors.{VenueVector, AbstractVector}
import features.IntFeature

/**
 * @author Ivan Gavrilović
 */
class MockAbstractInputProcessor extends AbstractInputProcessor{
  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return collection of vectors
   */
  override def processData(input: BufferedSource): Seq[AbstractVector] = {

    val features = for {a<- 1 to 100} yield IntFeature("id = " + a, a*10)

    List(new VenueVector(features, Nil), new VenueVector(features, Nil))
  }
}
