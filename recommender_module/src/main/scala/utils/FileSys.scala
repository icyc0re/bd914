package utils

import java.io.{FileInputStream, File}

/**
 * @author Ivan Gavrilović
 */
object FileSys {
  def readFile(path: String): String = {
    try {
      val is = new FileInputStream(path)
      val content = scala.io.Source.fromInputStream(is)(scala.io.Codec.UTF8).mkString
      is.close()
      content
    }
    catch {
      case e: Exception => ""
    }
  }

  def getLines(path: String): Seq[String] = {
    readFile(path).split("\n").toSeq
  }

  def readDir(path: String): Seq[String] = {
    val dir = new File(path)
    if (!dir.isDirectory) {
      throw new Exception("Directory expected")
    }
    dir.listFiles.filter((x: File) => !x.getName.startsWith(".") && x.isFile).map(_.getAbsolutePath)
  }
}
