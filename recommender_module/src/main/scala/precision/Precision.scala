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
  Map[String, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]] = {
    var userInteractions: Map[String, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]] =
      Map.empty[String, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]]

    var count: Double = 0
    var numberOfVenues: Double = 0
    for ((userVector, venueVectors) <- userVenues) {
      var userId: String = userVector.getFeatureValue[String](Cons.USER_ID).get
      //println(userVector);
      count = 0
      numberOfVenues = venueVectors.length
      userInteractions += userId -> Map((VenueListType.deleted -> collection.mutable.ArrayBuffer()),
        (VenueListType.notDeleted -> collection.mutable.ArrayBuffer()))
      for (venueVector <- venueVectors) {
        count = count + 1
        if (count / numberOfVenues < Cons.PRECISION_DELETION_FACTOR) {
          userInteractions(userId)(VenueListType.deleted) += venueVector
        }
        else {
          userInteractions(userId)(VenueListType.notDeleted) += venueVector
        }
      }
    }
    userInteractions
  }

  /**
   * Calculate UserVector based only on notDeleted interactions
   * @param userInteractions map of both deleted and notDeleted user interactions
   */
  def getUserVectorFromUserInteractions(userInteractions: Map[String, Map[VenueListType.VenueListType, Seq[VenueVector]]]):
  Seq[UserVector] = {
    userInteractions.map(x => UserVector.getById(x._1).applyVenues(x._2(VenueListType.notDeleted))).toSeq
  }

  //for one sampled user

  /**
   * Precision is calculated as
   * Number of interactions that were deleted from the user found in the recommendation /
   * Number of recommendations - number of nonDeletedItems found in the recommendations
   */
  def calculatePrecisionForOneUser(topK: (String, Seq[(String, Double)]), deletedInteractions: Seq[VenueVector], nonDeletedInteractions: Seq[VenueVector]): Double = {
    var sum = 0.0
    var numberOfNonDeletedItemsInRecommendations = 0
    for ((venueID, value) <- topK._2) {
      if (deletedInteractions.exists((a: VenueVector) => a == VenueVector.getById(venueID)))
        sum += 1.0
      if (nonDeletedInteractions.exists((a: VenueVector) => a == VenueVector.getById(venueID)))
        numberOfNonDeletedItemsInRecommendations += 1
    }


    var result = 0.0
    if (topK._2.length - numberOfNonDeletedItemsInRecommendations > 0)
      result = sum / (topK._2.length - numberOfNonDeletedItemsInRecommendations)

    result
  }

  def calculatePrecision(usersTopK: Seq[(String, Seq[(String, Double)])], modified: Map[String, Map[VenueListType.VenueListType, Seq[VenueVector]]]): Double = {
    var sum = 0.0
    for ((userId, userVenues) <- usersTopK) {
      sum += calculatePrecisionForOneUser((userId, userVenues),
        modified(userId)(VenueListType.deleted),
        modified(userId)(VenueListType.notDeleted))
    }

    println("Precision: " + sum / usersTopK.length)
    sum / usersTopK.length
  }
}

