package vectors

import features.{IntFeature, Feature}
import sets.AbstractVectorSet
import scala.reflect.ClassTag

/**
 * @author Ivan Gavrilović
 */
class VenueVector(features: Seq[IntFeature], sets:Seq[AbstractVectorSet]) extends AbstractVector{


  /**
   * Get all sets to which this vector belongs to
   * @return all vector sets associated
   */
  override def getVectorSets(): Seq[AbstractVectorSet] = ???

  /**
   * Gets the value of the feature that is associated with the key
   * @param key key of the feature
   * @tparam K key's type
   * @tparam V value's type
   * @return value of the feature
   */
  override def getFeatureValue[K, V](key: K): V = ???

  /**
   * Sets the value of the feature
   * @param key key of the feature
   * @param value feature's value
   * @tparam K key's type
   * @tparam V value's type
   * @return created feature instance
   */
  override def putFeature[K, V, T <: Feature[K, V]](key: K, value: V): T = ???

  /**
   * Get all of the vector's features
   * @return all features
   */
  override def getFeatures(): Seq[Feature[_, _]] = ???

  /**
   * Get only the specific features of the vector
   * @param keys keys of the features that we need
   * @tparam K key's type
   * @return features with the specified keys
   */
  override def getFeatures[K](keys: Seq[K]): Seq[Feature[K, _]] = ???

  /**
   * Get all features with specific type
   * @tparam T feature type
   * @return list of features with specified type
   */
  override def getFeaturesTyped[T<: Feature[_, _] : ClassTag](): Seq[T] = features.collect{case (x:T) => x}
}
