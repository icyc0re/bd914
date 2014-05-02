package vectors

import features.DoubleFeature

/**
 * Use this class to specify the general transformations/normalizations of the values for the feature vectors
 * @author Ivan Gavrilović
 */
object VectorTransform {
  def applyLogToDouble(feature: DoubleFeature) = DoubleFeature(feature.key, Math.log(feature.value))
}
