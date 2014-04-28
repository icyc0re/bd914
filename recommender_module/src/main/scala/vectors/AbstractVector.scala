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
   * Gets the value of the feature that is associated with the key
   * @param key key of the feature
   * @tparam V value's type
   * @return value of the feature
   */
  def getFeatureValue[V](key: Any): Option[V] = getFeatures.find {
    case (t: Feature[_, V]) => t.key == key
  } match {
    case Some(x:Feature[_, V]) => Some(x.value)
    case _ => None
  }

  def findFeature(key:Any) = getFeatures.find(_.key == key)


  /**
   * Sets the value of the feature
   * @param feat new feature to be added
   * @return created feature instance
   */
  def putFeature(feat: Feature[_, _]): Unit = {
    getFeatures.find(_.key == feat.key) match {
      case Some(_) => throw new Exception("Key already exists!")
      case None => setFeatures(getFeatures :+ feat)
    }
  }

  /**
   * Get only the specific features of the vector
   * @param keys keys of the features that we need
   * @tparam K key's type
   * @return features with the specified keys
   */
  def getFeatures[K](keys: Seq[K]): Seq[Feature[K, _]] = getFeatures.collect {
    case (x: Feature[K, _]) if keys.contains(x.key) => x
  }

  /**
   * Get all features with specific type
   * @tparam T feature type
   * @return list of features with specified type
   */
  def getFeaturesTyped[T <: Feature[_, _]: ClassTag]: Seq[T] = getFeatures.collect {
    case (x: T) => x
  }

  /**
   * Get all of the vector's features
   * @return all features
   */
  def getFeatures: Seq[Feature[_, _]]
  /**
   * Set all of the vector's features
   */
  protected def setFeatures(feats: Seq[Feature[_,_]])

  /**
   * Get all sets to which this vector belongs to
   * @return all vector sets associated
   */
  def getVectorSets: Seq[AbstractVectorSet]
}
