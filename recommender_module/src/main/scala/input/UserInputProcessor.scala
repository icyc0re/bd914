package input

import scala.io.BufferedSource
import vectors.{UserVector, AbstractVector}
import java.io.File
import utils.Cons

/**
 * @author Ivan Gavrilović
 */
class UserInputProcessor extends AbstractInputProcessor{
  type T = UserVector
  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return collection of vectors
   */
  override def processData(input: BufferedSource): T = {
    User.featureVector(new User(input.mkString))
  }
}
