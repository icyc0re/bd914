package test

import features._
import utils._
import input._
import vectors._
import context._

object Test {

  def testPrefiltering = {
    // Pre Filtering:
    val dummyContext: ContextVector = Context.grab()
    var dummyVenues: Seq[VenueVector] = Seq(
    		new VenueVector(Seq(
    				new CategoryFeature(Cons.CATEGORY, List("Food")),
    				new CoordinatesFeature(Cons.GPS_COORDINATES, (40.5, -73.0)),
    				new PriceFeature(Cons.PRICE, new VenuePrice(Option(2), Option("Cheap food"), Option("USD")))
				), null),
    		new VenueVector(Seq(new CategoryFeature(Cons.CATEGORY, List("Nightlife Spot"))), null)
    )
    
    dummyVenues = PreFilter.apply(dummyVenues, dummyContext)
  }
  
}