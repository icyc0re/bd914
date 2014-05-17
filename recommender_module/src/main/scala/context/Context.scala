package context

import vectors.ContextVector
import utils.Cons
import features._
import input._

/*
 * Interface used to grab all context related features: Location, TimeOfDay, some explicitly defined preferences, etc. 
 */
object Context {

  var contextVector: ContextVector = null

  // Input: args -- lat, lng, rad, time1, time2, days
  def setContext(args: Array[String]) = {
    if (args.length == 7) {
      var list: List[Feature[_,_]] = List.empty      
      if (args(1) != "~" && args(2) != "~")
        list :+ CoordinatesFeature(Cons.GPS_COORDINATES, (args(1).toDouble, args(2).toDouble)) // Current Location of the user
      if (args(3) != "~")
        list :+ DoubleFeature(Cons.RADIUS, args(3).toDouble) // Radius to search around location
      if (args(4) != "~" && args(5) != "~") {
        val sH: Int = args(4).split(":")(0).toInt
        val sM: Int = args(4).split(":")(1).toInt
        
        val eH: Int = args(5).split(":")(0).toInt
        val eM: Int = args(5).split(":")(1).toInt
        
        if (args(6) != "~") {
          for (i <- 1 to 7)
            list :+ TimeFeature(Cons.TIME, List(((i, sH, sM), (i, eH, eM))))
        } else {
          args(6).map(_.asDigit).map(x => {
            list :+ TimeFeature(Cons.TIME, List(((x, sH, sM), (x, eH, eM))))
          })
        }
      }
      
      contextVector = new ContextVector(list, null)
    }
    // No Context provided
    else {
      println("No Context provided");
    }
  }

  def grab(): ContextVector = {
    contextVector
  }

  def grabTestContextVector(num: Int): ContextVector = {
    if (num == 1) createContextVector("Food", 1, "Cheap food", "USD", 40.809976486485596, -73.96180629730225, 6, 20, 15, 6, 23, 0)
    else if (num == 2) createContextVector("Nightlife Spot", 2, null, "USD", 40.82, -74.00, 4, 15, 0, 6, 11, 0)
    else createContextVector("Museum", 1, null, "USD", 41.00, -74.15, 2, 14, 0, 4, 12, 0)
  }

  def createContextVector(category: String, priceTier: Int,
                          price: String, priceCurrency: String,
                          coordLat: Double, coordLon: Double,
                          sDay: Int, sMinutes: Int, sHours: Int,
                          eDay: Int, eMinutes: Int, eHours: Int): ContextVector = {
    return new ContextVector(List(
      TextFeature(Cons.CATEGORY, category), // User selected that he only wants Food or Nightlife venues
      PriceFeature(Cons.PRICE, new VenuePrice(Option(priceTier), Option(price), Option(priceCurrency))), // user selected he wants cheap locations
      CoordinatesFeature(Cons.GPS_COORDINATES, (coordLat, coordLon)), //Current Location of the user
      TimeFeature(Cons.TIME, List(((sDay, sMinutes, sHours), (eDay, eMinutes, eHours)))) // ... other context related features (i.e. current location)
    ), null)
  }
}