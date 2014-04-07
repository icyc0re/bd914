package features

/**
 * Representing the feature (attribute, property). Extend this class in order to create specific features.
 * @author Ivan Gavrilović
 */
abstract class Feature[+K, +V](key:K, value:V) {
  override def toString(): String = "Feature: " + key.toString + " = " + value.toString
}
