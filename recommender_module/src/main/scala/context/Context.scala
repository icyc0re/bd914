package context

import vectors.ContextVector
import utils.Cons
import features._
import input._

/*
 * Interface used to grab all context related features: Location, TimeOfDay, some explicitly defined preferences, etc. 
 */
object Context {
	// Sample category list:
  // TODO: to be moved to some sort of data model
	val categories = List(
	      new VenueCategory(Option("4d4b7105d754a06374d81259"), "Food", Option("Foods"), Option("Food"),
	                     Option(true)),
	      new VenueCategory(Option("4d4b7105d754a06376d81259"), "Nightlife Spot", Option("Nightlife Spots"), Option("Nightlife Spot"),
	                     Option(true)),
	      new VenueCategory(Option("4d4b7105d754a06377d81259"), "Outdoors & Recreation", Option("Outdoors & Recreation"), Option("Outdoors & Recreation"),
	                     Option(true))
	)
  
  // FIXME: to be modified later to actually grab context related features. For now this returns a test context 
  def grab(): ContextVector = {
    return new ContextVector(List(
      CategoryFeature(Cons.CATEGORY, List(categories(0), categories(1))), // User selected that he only wants Food or Nightlife venues
      PriceFeature(Cons.PRICE, new VenuePrice(Option(1), Option("Cheap food"), Option("USD"))) // user selected he wants cheap locations
      // ... other context related features (i.e. current location)
    ), null)
  }
  
}