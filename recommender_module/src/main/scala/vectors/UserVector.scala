package vectors

import features._
import sets.AbstractVectorSet
import utils.Cons
import features.BooleanFeature
import features.DoubleFeature
import scala.Some
import input.UserInputProcessor

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
    val size = venues.size
    venues.foreach((x: VenueVector) =>
      x.features.foreach((f: Feature[_, _]) =>
        findFeature(f.key) match {
          case Some(old: Feature[_, _]) => combineFeatures(old, f, 1 / size)
          case None => putFeature(f)
        }
      )
    )
    this
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
        vectors = new UserInputProcessor().processInDir(Cons.USERS_PATH)
      case _ => // nothing
    }
    vectors
  }

  def getById(id: String): UserVector = getAll.find((x: UserVector) =>
    x.getFeatureValue[String](Cons.USER_ID) == id
  ) match {
    case Some(x) => x
    case None => null
  }

  def homeCity(city: String): BooleanFeature = BooleanFeature(Cons.HOME_CITY, city.toLowerCase == "new york" || city.toLowerCase == "ny")
}
