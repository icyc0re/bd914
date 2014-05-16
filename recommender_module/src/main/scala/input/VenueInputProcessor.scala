package input

import vectors.VenueVector

/**
 * @author Ivan Gavrilović
 */
class VenueInputProcessor extends AbstractInputProcessor {
  type T = VenueVector

  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return collection of vectors
   */
  override def processData(input: String): T = {
    Venue.featureVector(new Venue(input).venue)
  }
}