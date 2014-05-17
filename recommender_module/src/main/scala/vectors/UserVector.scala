package vectors

import features._
import input.UserInputProcessor
import sets.AbstractVectorSet
import utils.Cons
import features.BooleanFeature
import features.IntFeature
import features.DoubleFeature
import scala.Some


/**
 * * See [[input.User]] for the list of features that make the vector
 * @author Ivan Gavrilović
 */
class UserVector(var features: Seq[Feature[_, _]], sets: Seq[AbstractVectorSet]) extends AbstractVector {
  /**
   * Get all sets to which this vector belongs to
   * @return all vector sets associated
   */
  override def getVectorSets: Seq[AbstractVectorSet] = sets

  /**
   * Set all of the vector's features
   */
  override protected def setFeatures(feats: Seq[Feature[_, _]]): Unit = features = feats

  /**
   * Get all of the vector's features
   * @return all features
   */
  override def getFeatures: Seq[Feature[_, _]] = features

  /**
   * Updates the feature vector according to the venues that user interacted with
   * @param venues collection of venues
   */
  def applyVenues(venues: Seq[VenueVector]): UserVector = {
    // add all of the interaction venue categories to the existing user categories
    val categories = CategoryFeature(Cons.CATEGORY, getFeatureValue[Seq[String]](Cons.CATEGORY).getOrElse(List.empty[String]) ++
      venues.collect {
        case (x: VenueVector) if !x.getFeatureValue[Seq[String]](Cons.CATEGORY).getOrElse(List.empty).contains(Cons.HOME_PRIVATE) => x.getFeatureValue[Seq[String]](Cons.CATEGORY).get
      }.flatten)

    // combine gps
    val gps = CoordinatesFeature(Cons.GPS_COORDINATES, getGPSCenter(venues.map(_.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).getOrElse((.0, .0))) :+ getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).getOrElse((.0, .0))))
    // combine popularity
    val popularity = DoubleFeature(Cons.POPULARITY, computePopularity(venues.map(_.getFeatureValue[Double](Cons.POPULARITY).get) :+ getFeatureValue[Double](Cons.POPULARITY).get))

    val userId = TextFeature(Cons.USER_ID, getFeatureValue[String](Cons.USER_ID).get)
    // new, updated user vector
    new UserVector(List(userId, categories, gps, popularity), null)
  }

  def computePopularity(pops: Seq[Double]): Double = {
    pops.sum / pops.size
  }

  /**
   * compute the average of gps coordinates of the user interactions
   * @return average of gps coordinates of the user interactions
   */
  def getGPSCenter(coords: Seq[(Double, Double)]): (Double, Double) = {
    var lat: Double = 0
    var lng: Double = 0

    def isInNY(venueLat: Double, venueLng: Double): Boolean = {
      Cons.NY_AREA("s") <= venueLat && venueLat <= Cons.NY_AREA("n") &&
        Cons.NY_AREA("w") <= venueLng && venueLng <= Cons.NY_AREA("e")
    }

    var validOnes = 0
    for ((ilat, ilng) <- coords) {
      isInNY(ilat, ilng) match {
        case true =>
          //get venue gps location, check it is in NY area
          lat += ilat
          lng += ilng
          validOnes += 1
        case false => // ignore this one
      }
    }

    if (validOnes == 0) (40.7056308, -73.9780035)
    else (lat / validOnes, lng / validOnes)
  }

  def combineFeatures(old: Feature[_, _], n: Feature[_, _], weight: Double) = {
    val oldVal = old match {
      case IntFeature(key, value) => value
      case DoubleFeature(key, value) => value
      case _ => 0
    }

    val newVal = n match {
      case IntFeature(key, value) => value
      case DoubleFeature(key, value) => value
      case _ => 0
    }

    features = features diff List(old)
    features = features :+ DoubleFeature(old.key.asInstanceOf[String], oldVal + weight * newVal)
  }
}

/**
 * User vector companion object, use it to specify global operations on user vectors
 */
object UserVector {
  var vectors: Seq[UserVector] = Nil

  def getAll: Seq[UserVector] = {
    vectors match {
      case Nil =>
        vectors = new UserInputProcessor().processInDir(Cons.USERS_PATH, Cons.USERS_MAX )
      case _ => // nothing
    }
    vectors
  }

  def getById(id: String): UserVector = getAll.find((x: UserVector) =>
    x.getFeatureValue[String](Cons.USER_ID).get == id
  ) match {
    case Some(x) => x
    case None => null
  }

  def homeCity(city: String): BooleanFeature = BooleanFeature(Cons.HOME_CITY, city.toLowerCase == "new york" || city.toLowerCase == "ny")
}
