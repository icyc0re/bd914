package input

import scala.io.BufferedSource
import vectors.{UserVector, AbstractVector}
import java.io.File

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
  
  // for testing purposes, maybe refactor processing to return Users instead of Vectors
  def processUsersInDir(dirName: String): Seq[User] = {
    val dir = new File(dirName)
    if (!dir.isDirectory){
      throw new Exception("Directory expected")
    }
    //ignore hidden files
    dir.listFiles.filter(!_.getName.startsWith(".")).filter(_.isFile()).map(x => new User(scala.io.Source.fromFile(x).mkString))
  }
}
