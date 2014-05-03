package input

import scala.io.BufferedSource
import vectors.{VenueVector, AbstractVector}
import java.io.File

/**
 * @author Ivan Gavrilović
 */
class VenueInputProcessor extends AbstractInputProcessor{
  type T = VenueVector
  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return collection of vectors
   */
  override def processData(input: BufferedSource): T = {
    Venue.featureVector(new Venue(input.mkString).venue)
  }
}