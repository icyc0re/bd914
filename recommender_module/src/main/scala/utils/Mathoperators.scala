package utils

/**
 * Created by Julia Y. Chatain on 14/05/2014.
 */
object Mathoperators {

  def inclusion(timeInt1: ((Int, Int, Int), (Int, Int, Int)), timeInt2: ((Int, Int, Int), (Int, Int, Int))): Boolean = {
    //return true is timeInt2 is included in timeInt1
    var included = false
    if ((timeInt1._1._1 <= timeInt2._1._1) && (timeInt1._1._2 <= timeInt2._1._2) && (timeInt1._1._3 <= timeInt2._1._3)) {
      if ((timeInt1._2._1 >= timeInt2._2._1) && (timeInt1._2._2 >= timeInt2._2._2) && (timeInt1._2._3 >= timeInt2._2._3)) {
        included = true
      }
    }
    included
  }

}
