package features
import features.Feature

/**
 * Case class representing the features that have [[scala.Int]]] values
 * @author Ivan Gavrilović
 */
case class DoubleFeature(key:String, value:Double) extends Feature[String, Double](key, value){
}
