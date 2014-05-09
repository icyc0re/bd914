package precision

import utils.Cons
import vectors.{VenueVector, VenueListType, UserVector}

import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer
import vectors.{VenueVector, UserVector}
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


   //for one sampled user

    /*
      Precision is calculated as
      Number of interactions that were deleted from the user found in the recommendation /
      Number of recommendations - number of nonDeletedItems found in the recommendations
     */

  def calculatePrecision(topK: (String, Seq[(String, Double)]), deletedInteractions:  Seq[VenueVector], nonDeletedInteraction:  Seq[VenueVector] ): Double = {
    var sum = 0.0
    var numberOfNonDeletedItemsInRecommendations = 0
    for((venue, value) <-  topK._2){
      if(deletedInteractions.exists((a:VenueVector) => a == VenueVector.getById(venue)))
        sum+=1.0
      if(nonDeletedInteraction.exists((a:VenueVector) => a == VenueVector.getById(venue)))
        numberOfNonDeletedItemsInRecommendations +=1
    }

    var result = 0.0
    if(topK._2.length != numberOfNonDeletedItemsInRecommendations )
      result = sum / (topK._2.length - numberOfNonDeletedItemsInRecommendations)

    println(result);
    result
  }
}
