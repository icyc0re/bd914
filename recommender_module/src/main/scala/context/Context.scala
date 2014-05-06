package context

import vectors.ContextVector
import utils.Cons
import features._
import input._

/*
 * Interface used to grab all context related features: Location, TimeOfDay, some explicitly defined preferences, etc. 
 */
object Context {
  // FIXME: to be modified later to actually grab context related features. For now this returns a test context 
  def grab(): ContextVector = {
    return new ContextVector(List(
      TextFeature(Cons.CATEGORY, "Food"), // User selected that he only wants Food or Nightlife venues
      PriceFeature(Cons.PRICE, new VenuePrice(Option(1), Option("Cheap food"), Option("USD"))), // user selected he wants cheap locations
      CoordinatesFeature(Cons.GPS_COORDINATES, (40.809976486485596, -73.96180629730225)),       //Current Location of the user
      TimeFeature(Cons.TIME, List(((0,0,0),(-1,-1,-1)))) // ... other context related features (i.e. current location)
    ), null)
  }
  
}