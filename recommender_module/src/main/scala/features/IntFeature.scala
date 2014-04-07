package features

/**
 * Case class representing the features that have [[scala.Int]]] values
 * @author Ivan Gavrilović
 */
case class IntFeature(key:String, value:Int) extends Feature[String, Int](key, value){
}
