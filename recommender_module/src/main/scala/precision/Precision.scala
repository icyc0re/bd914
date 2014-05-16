package precision

import utils.Cons
import vectors.{VenueVector, VenueListType, UserVector}

import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer
import vectors.{VenueVector, UserVector}
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
    def calculatePrecisionForOneUser(similarities: (String, Seq[(String, Double)]), deletedInteractions:  Seq[VenueVector], nonDeletedInteractions: Seq[VenueVector], topNumber :Integer): Double = {
      var sum = 0.0
      var numberOfNonDeletedItemsInRecommendations = 0.0
      //TODO change to collection.mutable
      var topKWithoutNotDeleted: Seq[String] = collection.mutable.ArrayBuffer.empty
      // Get top recommended venues without notDeleted
      var count = 0
      var exists = false
      println("Similariteis")
      // Create venue similarities set

      val similaritiesSet: collection.mutable.Set[String] = collection.mutable.Set.empty[String]
      for ((venueID, value) <- similarities._2) {
        similaritiesSet += venueID
      }

      //      for ((venueID, value) <- similarities._2) {
      //        println(venueID)
      //      }
      //      println("smilarities set")
      //      for (venueID <- similaritiesSet) {
      //        println(venueID)
      //      }
      //      println("deleted")
      //      for (venueVector <- deletedInteractions) {
      //        println(venueVector.getFeatureValue[String](Cons.VENUE_ID).get)
      //      }
      val deletedSet: collection.mutable.Set[String] = collection.mutable.Set.empty[String]
      for (venueVector <- deletedInteractions) {
        deletedSet += venueVector.getFeatureValue[String](Cons.VENUE_ID).get
      }
      //      println("deleted set")
      //      for (venueID <- deletedSet) {
      //        println(venueID)
      //      }
      //      println("nondeleted")
      //      for (venueVector <- nonDeletedInteractions) {
      //        println(venueVector.getFeatureValue[String](Cons.VENUE_ID).get)
      //      }
      val nonDeletedSet: collection.mutable.Set[String] = collection.mutable.Set.empty[String]
      for (venueVector <- nonDeletedInteractions) {
        nonDeletedSet += venueVector.getFeatureValue[String](Cons.VENUE_ID).get
      }
      //      println("nondeleted set")
      //      for (venueID <- nonDeletedSet) {
      //        println(venueID)
      //      }
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
      //      println("Top K without deleted")
      //      for (venueID <- topKWithoutNotDeleted) {
      //        println(venueID)
      //      }
      for(venueID <-  topKWithoutNotDeleted){
        for(deletedVenueId <- deletedSet) {
          if(venueID == deletedVenueId) {
            sum+=1.0
          }
        }
        //        for(venueVector <- nonDeletedInteractions) {
        //          if(venueID == venueVector.getFeatureValue[String](Cons.VENUE_ID).get) {
        //            numberOfNonDeletedItemsInRecommendations+=1.0
        //            println("Detected nondeleted venue")
        //          }
        //        }
      }


      var result = sum/count
      //if(similarities._2.length - numberOfNonDeletedItemsInRecommendations > 0 )
      // result = sum / (similarities._2.length - numberOfNonDeletedItemsInRecommendations)
      println(result)
      result
    }

  def calculatePrecision(similarities: Seq[(String, Seq[(String, Double)])], modified: Map[String, Map[VenueListType.VenueListType, Seq[VenueVector]]] , topNumber: Integer ): Double = {
    var sum = 0.0
    for ((userId, userVenues) <- similarities) {
      sum += calculatePrecisionForOneUser((userId, userVenues),
        modified(userId)(VenueListType.deleted),
        modified(userId)(VenueListType.notDeleted),
        topNumber)
    }

    val precision = sum / similarities.length
    println(f"Precision: $precision%2.20f")
    sum / similarities.length
  }
  }

