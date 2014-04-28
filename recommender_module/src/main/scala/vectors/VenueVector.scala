package vectors

import features.{IntFeature, Feature}
import sets.AbstractVectorSet
import scala.reflect.ClassTag

/**
 * See [[input.Venue]] for the list of features that make the vector
 * @author Ivan Gavrilović
 */
class VenueVector(var features: Seq[Feature[_,_]], sets:Seq[AbstractVectorSet]) extends AbstractVector{
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
