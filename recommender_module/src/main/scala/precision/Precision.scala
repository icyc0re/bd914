package precision


import vectors.{VenueVector, VenueListType, UserVector}
import utils.Cons
import scala.util.control.Breaks._

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
  Map[UserVector, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]] = {
    var userInteractions: Map[UserVector, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]] =
      Map.empty[UserVector, Map[VenueListType.VenueListType, collection.mutable.ArrayBuffer[VenueVector]]]

    var count: Double = 0
    var numberOfVenues: Double = 0
    // USE THIS TO CHECK PRECISION CORRECTNESS
    //var (userVector, venueVectors) = userVenues(1)
    // COMMENT OUT THIS LOOP TO CHECK PRECISION CORRECTNESS
    for ((userVector, venueVectors) <- userVenues) {
      //var userId: String = userVector.getFeatureValue[String](Cons.USER_ID).get
      count = 0
      numberOfVenues = venueVectors.length
      userInteractions += userVector -> Map((VenueListType.deleted -> collection.mutable.ArrayBuffer()),
        (VenueListType.notDeleted -> collection.mutable.ArrayBuffer()))
      for (venueVector <- venueVectors) {
        count = count + 1
        if (count / numberOfVenues < Cons.PRECISION_DELETION_FACTOR) {
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
  Seq[UserVector] = {
    userInteractions.map(x => x._1.applyVenues(x._2(VenueListType.notDeleted))).toSeq
    //userInteractions.map(x => UserVector.getById(x._1).applyVenues(x._2(VenueListType.notDeleted))).toSeq
  }



  /**
   * Precision is calculated as
   * Number of interactions that were deleted from the user found in the recommendation /
   * Number of recommendations - number of nonDeletedItems found in the recommendations
   */
  def calculatePrecisionForOneUser(similarities: (String, Seq[(String, Double)]), deletedInteractions:  Seq[VenueVector], nonDeletedInteractions: Seq[VenueVector], topNumber :Integer): Double = {
    var sum = 0.0
    var topKWithoutNotDeleted: Seq[String] = collection.mutable.ArrayBuffer.empty
    // Get top recommended venues without notDeleted
    var count = 0
    var exists = false

    // Create venue similarities set
    val similaritiesSet: collection.mutable.Set[String] = collection.mutable.Set.empty[String]
    for ((venueID, value) <- similarities._2) {
      similaritiesSet += venueID
    }

    val deletedSet: collection.mutable.Set[String] = collection.mutable.Set.empty[String]
    for (venueVector <- deletedInteractions) {
      deletedSet += venueVector.getFeatureValue[String](Cons.VENUE_ID).get
    }

    val nonDeletedSet: collection.mutable.Set[String] = collection.mutable.Set.empty[String]
    for (venueVector <- nonDeletedInteractions) {
      nonDeletedSet += venueVector.getFeatureValue[String](Cons.VENUE_ID).get
    }

    breakable {
      for (venueID <- similaritiesSet) {
        exists = false
        // Check if the venue from recommendation exists in nonDeletedInteractions
        breakable {
          for(nonDeletedVenueID <- nonDeletedSet) {
            if(venueID == nonDeletedVenueID) {
              exists = true
              //break()
            }
          }
        }
        if(exists == false) {
          topKWithoutNotDeleted :+= venueID
          count = count + 1
        }
        if(count == topNumber)
          break()
      }
    }

    for(venueID <-  topKWithoutNotDeleted){
      for(deletedVenueId <- deletedSet) {
        if(venueID == deletedVenueId) {
          sum+=1.0
        }
      }
    }
    var result = sum/count
    result
  }

  def calculatePrecision(similarities: Seq[(String, Seq[(String, Double)])], modified: Map[UserVector, Map[VenueListType.VenueListType, Seq[VenueVector]]] , topNumber: Integer ): Double = {
    var sum = 0.0
    for ((userId, userVenues) <- similarities) {
      // find deleted and nonDeleted for this user
      for ((modifiedUserVector, venueVectors) <- modified) {
        if (modifiedUserVector.getFeatureValue[String](Cons.USER_ID).get == userId) {
          sum += calculatePrecisionForOneUser((userId, userVenues),
            venueVectors(VenueListType.deleted),
            venueVectors(VenueListType.notDeleted),
            topNumber)
        }
      }
    }

    val precision = sum / similarities.length
    println(f"Precision: $precision%2.20f")
    sum / similarities.length
  }
}

