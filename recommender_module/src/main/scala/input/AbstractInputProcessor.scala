package input

import utils.FileSys
import scala.collection
import scala.collection.mutable

/**
 * Implement this if you want to process the input files, and create object representation of the read data
 * @author Ivan Gavrilović
 */
trait AbstractInputProcessor {
  type T

  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return feature vector
   */
  def processData(input: String): T

  /**
   * Parse all files in a directory
   * @param dirName path to the directory
   * @return collection of vectors
   */
  def processInDir(dirName: String, maxItems: Int = Int.MaxValue): Seq[T] = {
    var cnt = 0
    val fileIt = FileSys.readDir(dirName).iterator
    val vectors: collection.mutable.MutableList[T] = mutable.MutableList.empty[T]
    while (fileIt.hasNext && cnt < maxItems) {
      cnt match {
        case 0 => println("Started parsing...")
        case x if cnt % 1000 == 0 => println("Parsed " + cnt)
        case _ => // no logging
      }
      cnt += 1
      vectors += processData(FileSys.readFile(fileIt.next()))
    }
    vectors.toList
  }
}
