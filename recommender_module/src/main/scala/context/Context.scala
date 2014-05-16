package context

import vectors.ContextVector
import utils.Cons
import features._
import input._

/*
 * Interface used to grab all context related features: Location, TimeOfDay, some explicitly defined preferences, etc. 
 */
object Context {
  //  // FIXME: to be modified later to actually grab context related features. For now this returns a test context
  //  def grab(): ContextVector = {
  //    return new ContextVector(List(
  //      TextFeature(Cons.CATEGORY, "Food"), // User selected that he only wants Food or Nightlife venues
  //      PriceFeature(Cons.PRICE, new VenuePrice(Option(1), Option("Cheap food"), Option("USD"))), // user selected he wants cheap locations
  //      CoordinatesFeature(Cons.GPS_COORDINATES, (40.809976486485596, -73.96180629730225)), //Current Location of the user
  //      TimeFeature(Cons.TIME, List(((0, 0, 0), (-1, -1, -1)))) // ... other context related features (i.e. current location)
  //    ), null)
  //  }

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