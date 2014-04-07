package input

import scala.io.BufferedSource
import vectors.AbstractVector

/**
 * Implement this if you want to process the input files, and create object representation of the read data
 * @author Ivan Gavrilović
 */
trait AbstractInputProcessor {

  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return collection of vectors
   */
  def processData(input: BufferedSource):Seq[AbstractVector]
}
