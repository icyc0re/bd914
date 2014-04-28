package utils

import scala.math

/**
 * Created by Julia Y. Chatain on 28/04/2014.
 */
object Haversine {
  val r = 6.371//Earth radius in kilometers

  def getDistance( loc1: (Double, Double), loc2: (Double, Double)): Double = {
    val deltaLat= math.toRadians(loc2._1 - loc1._1)
    val deltaLong= math.toRadians(loc2._2 - loc1._2)

    val c = 2 * math.asin(math.sqrt(math.pow(math.sin(deltaLat/2.0), 2) + math.pow(math.sin(deltaLong/2.0), 2) * math.cos(math.toRadians(loc1._1)) * math.cos(math.toRadians(loc2._1)) ))

    r * c
  }

}
