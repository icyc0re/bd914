package vectors

import features.{DoubleFeature, IntFeature, Feature}
import sets.AbstractVectorSet
import input.VenueInputProcessor
import utils.Cons
import utils.Mathoperators

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
      case Nil =>
        vectors = new VenueInputProcessor().processInDir(Cons.VENUES_PATH)
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

  def serialize(v: VenueVector): String = {
    var line = new StringBuilder("")
    // get categories
    line ++= ""
    // get num people
    line ++= ""
    // get coordinates
    line ++= ""
    line.mkString
  }

  def deSerialize(in: String): VenueVector = {
    val components = in.split("\\s")
    // get categories
    val categories = IntFeature("categories", components(0).toInt)
    // get num people
    val numPeople = IntFeature("numPeople", components(1).toInt)
    // get coordinates
    val coordinates = DoubleFeature("coordsLan", components(2).toDouble)


    new VenueVector(List(categories, numPeople, coordinates), null)
  }
}
