package vectors

import features._
import sets.AbstractVectorSet
import input.VenueInputProcessor
import utils.{FileSys, Cons, Mathoperators}
import features.DoubleFeature
import scala.Some
import features.TextFeature
import java.net.{URLEncoder, URLDecoder}
import java.io.{File, PrintWriter}

object VenueListType extends Enumeration {
  type VenueListType = Value
  val deleted, notDeleted = Value
}

/**
 * See [[input.Venue]] for the list of features that make the vector
 * @author Ivan Gavrilović
 */
class VenueVector(var features: Seq[Feature[_, _]], sets: Seq[AbstractVectorSet]) extends AbstractVector {
  /**
   * Get all sets to which this vector belongs to
   * @return all vector sets associated
   */
  override def getVectorSets: Seq[AbstractVectorSet] = sets

  /**
   * Set all of the vector's features
   * @return all features
   */
  override protected def setFeatures(feats: Seq[Feature[_, _]]) =
    features = feats

  /**
   * Get all of the vector's features
   * @return all features
   */
  override def getFeatures: Seq[Feature[_, _]] = features


  def isOpenUser(userTime: Seq[((Int, Int, Int), (Int, Int, Int))]): Int = {
    //Returns 1 if the place is open, 0 if it is closed, -1 if we don't know
    userTime match {
      case x if !x.isEmpty => {
        //Data: we now wants to see if the place is open or not
        x.map((x: ((Int, Int, Int), (Int, Int, Int))) => isOpenTime(x)).reduce(math.max);
      }
      case _ => -1 //No data
    }
  }

  def isOpenTime(userTimeInterval: ((Int, Int, Int), (Int, Int, Int))): Int = {
    //Returns 1 if the place is open, 0 if it is closed, -1 if we don't know

    this.getFeatureValue[List[((Int, Int, Int), (Int, Int, Int))]](Cons.TIME) match {
      case Some(_) => {

        this.getFeatureValue[List[((Int, Int, Int), (Int, Int, Int))]](Cons.TIME) match {
          case x if !x.get.isEmpty => {
            //Data: we now wants to see if the place is open or not
            x.get.map((venueTimeInterval: ((Int, Int, Int), (Int, Int, Int))) => {
              //Check if the days fit
              if (Mathoperators.inclusion(venueTimeInterval, userTimeInterval)) {
                1
              }
              else {
                0
              }
            }).max
          }
          case _ => -1 //No data
        }
      }
      case None => -1
    }

  }
}

object VenueVector {
  var vectors: Seq[VenueVector] = Nil

  def getAll: Seq[VenueVector] = {
    vectors match {
      case Nil => {
        val f: File = new File(Cons.VENUES_SERIALIZED)
        if (f.isFile) {
          readFromDisk(Cons.VENUES_SERIALIZED)
        }
        else {
          vectors = new VenueInputProcessor().processInDir(Cons.VENUES_PATH)
          saveToDisk(Cons.VENUES_SERIALIZED)
        }
      }
      case _ => // nothing
    }
    vectors
  }

  def getById(id: String): VenueVector = getAll.find((x: VenueVector) =>
    x.getFeatureValue[String](Cons.VENUE_ID).get == id
  ) match {
    case Some(x) => x
    case None => null
  }

  def getId(x: VenueVector): String = {
    x.getFeatureValue[String](Cons.VENUE_ID).get
  }

  private def saveToDisk(path: String) = {
    val writer = new PrintWriter(path)
    vectors.foreach {
      v =>
        writer.println(serialize(v))
    }
    writer.close()
  }

  def readFromDisk(path: String) = {
    println("Reading venues from disk: " + path)
    vectors = for (l <- FileSys.getLines(path)) yield deSerialize(l)
  }

  def serialize(v: VenueVector): String = {
    var line = new StringBuilder("")
    // get categories
    line ++= v.getFeatureValue[String](Cons.VENUE_ID).getOrElse("n/a") ++ " "
    // get popularity
    line ++= v.getFeatureValue[Double](Cons.POPULARITY).getOrElse(.0).toString ++ " "
    // get coordinates
    val coord = v.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).getOrElse((.0, .0))
    line ++= coord._1.toString ++ " " ++ coord._2.toString ++ " "

    line ++= v.getFeatureValue[Seq[String]](Cons.CATEGORY).getOrElse(List("Bar")).foldRight("")((a, b) => URLEncoder.encode(a, "UTF-8") + "," + b) ++ " "

    // time series
    val times = v.getFeatureValue[Seq[((Int, Int, Int), (Int, Int, Int))]](Cons.TIME).getOrElse(List(((0, 0, 0), (0, 0, 0)))) match {
      case x if x.isEmpty => List(((0, 0, 0), (0, 0, 0)))
      case x => x
    }
    line ++= times.foldLeft[String]("")(
      (b, a) => a._1._1 + "," + a._1._2 + "," + a._1._3 + "," + a._2._1 + "," + a._2._2 + "," + a._2._3 + "," + b
    )

    line.mkString
  }

  def deSerialize(in: String): VenueVector = {
    val components = in.split("\\s")
    // get categories
    val venueId = TextFeature(Cons.VENUE_ID, components(0))
    // get popularity
    val popularity = DoubleFeature(Cons.POPULARITY, components(1).toDouble)
    // get coordinates
    val coordinates = CoordinatesFeature(Cons.GPS_COORDINATES, (components(2).toDouble, components(3).toDouble))
    // categories
    val cats = CategoryFeature(Cons.CATEGORY, parseCats(components(4)))
    // time series
    val times = TimeFeature(Cons.TIME, parseTimes(components(5)))

    new VenueVector(List(venueId, popularity, coordinates, cats, times), null)
  }

  def parseCats(cats: String): Seq[String] = {
    cats.split("\\,").map(x => URLDecoder.decode(x, "UTF-8"))
  }

  def parseTimes(times: String): Seq[((Int, Int, Int), (Int, Int, Int))] = {
    val parts = times.split("\\,")
    val res = for (i <- 0 until (parts.size / 6)) yield {
      ((parts(i * 6 + 0).toInt, parts(i * 6 + 1).toInt, parts(i * 6 + 2).toInt), (parts(i * 6 + 3).toInt, parts(i * 6 + 4).toInt, parts(i * 6 + 5).toInt))
    }
    res match {
      case List(((0, 0, 0), (0, 0, 0))) => List.empty[((Int, Int, Int), (Int, Int, Int))]
      case x => x
    }
  }
}