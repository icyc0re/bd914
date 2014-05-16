package input

import utils.FileSys

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
  def processInDir(dirName: String): Seq[T] = {
    var cnt = 0
    FileSys.readDir(dirName).map(
      x => {
        if (cnt % 1000 == 0) println("Parsed "+cnt)
        cnt += 1
        processData(FileSys.readFile(x))
      }
    )
  }
}
