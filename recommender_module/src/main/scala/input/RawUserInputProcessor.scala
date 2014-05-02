package input

import vectors.UserVector
import scala.io.BufferedSource
import java.io.File
import utils.Cons

/**
 * Created by emmafagerholm on 02/05/2014.
 */
class RawUserInputProcessor extends AbstractInputProcessor{

  type T = User
  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return collection of vectors
   */
  override def processData(input: BufferedSource): T = {
    new User(input.mkString)
  }

}
