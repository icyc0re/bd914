package vectors

import features.Feature
import sets.AbstractVectorSet
import scala.reflect.ClassTag

/**
 * All classes representing the vector in the recommendation system should implement this
 * @author Ivan Gavrilović
 */
trait AbstractVector {
  /**
   * Get all of the vector's features
   * @return all features
   */
  def getFeatures():Seq[Feature[_,_]]

  /**
   * Get all features with specific type
   * @tparam T feature type
   * @return list of features with specified type
   */
  def getFeaturesTyped[T <: Feature[_, _] : ClassTag]():Seq[T]

  /**
   * Get only the specific features of the vector
   * @param keys keys of the features that we need
   * @tparam K key's type
   * @return features with the specified keys
   */
  def getFeatures[K](keys:Seq[K]):Seq[Feature[K, _]]

  /**
   * Sets the value of the feature
   * @param key key of the feature
   * @param value feature's value
   * @tparam K key's type
   * @tparam V value's type
   * @return created feature instance
   */
  def putFeature[K, V, T <: Feature[K, V]](key: K, value: V):T

  /**
   * Gets the value of the feature that is associated with the key
   * @param key key of the feature
   * @tparam K key's type
   * @tparam V value's type
   * @return value of the feature
   */
  def getFeatureValue[K, V](key:K):V

  /**
   * Get all sets to which this vector belongs to
   * @return all vector sets associated
   */
  def getVectorSets():Seq[AbstractVectorSet]
}
