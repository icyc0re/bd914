package precision

import utils.Cons
import vectors.{VenueVector, VenueListType, UserVector}

import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer
import vectors.UserVector
import utils.Cons

/**
 * Keeps list of all categories and similarity between any two categories
 * @author Jakub Swiatkowski
 */


object Precision {

  /**
   * Modify user interactions by deleting percentage of venues for each user
   * @param userVenues sequence of users and venues they interacted with
   */
  def modifyUserInteractions(userVenues: Seq[(UserVector, Seq[VenueVector])]):
    Map[UserVector, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]]  = {
    var userInteractions: Map[UserVector, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]] =
      Map.empty[UserVector,Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]]

    var count: Double = 0
    var numberOfVenues: Double = 0
    for((userVector,venueVectors) <- userVenues) {
      //var userId: String = userVector.getFeatureValue[String](Cons.USER_ID).get
      count = 0
      numberOfVenues = venueVectors.length
      userInteractions += userVector -> Map((VenueListType.deleted -> collection.mutable.ArrayBuffer()),
        (VenueListType.notDeleted -> collection.mutable.ArrayBuffer()))
      for( venueVector <- venueVectors) {
        count = count + 1
        if(count/numberOfVenues < Cons.PRECISION_DELETION_FACTOR) {
          userInteractions(userVector)(VenueListType.deleted) += venueVector
        }
        else {
          userInteractions(userVector)(VenueListType.notDeleted) += venueVector
        }
      }
    }
    userInteractions
  }

  /**
   * Calculate UserVector based only on notDeleted interactions
   * @param userInteractions map of both deleted and notDeleted user interactions
   */
  def getUserVectorFromUserInteractions(userInteractions: Map[UserVector, Map[VenueListType.VenueListType, Seq[VenueVector]]]):
  Seq[UserVector]  = {
    userInteractions.map(x => x._1.applyVenues(x._2(VenueListType.notDeleted))).toSeq
  }


}
