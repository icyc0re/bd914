package vectors

import features.{DoubleFeature, IntFeature, Feature}
import sets.AbstractVectorSet
import input.VenueInputProcessor
import utils.Cons

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

  def getById(id:String):VenueVector = getAll.find((x:VenueVector)=>
    x.getFeatureValue[String](Cons.VENUE_ID).get == id
  ) match{
    case Some(x) => x
    case None => null
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
