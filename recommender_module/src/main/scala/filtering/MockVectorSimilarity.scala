package filtering

import vectors.{VenueVector, UserVector, AbstractVector}
import utils.Cons
import input.Category
import scala.collection.mutable


/**
 * @author Ivan Gavrilović
 */
object MockVectorSimilarity extends VectorSimilarity {
  /**
   * Get the similarity between two collections of vectors
   * @param fst first collection of vectors
   * @param snd second collection of vectors
   * @return list of similarities between vectors
   */
  def calculateSimilarity(fst: Seq[AbstractVector], snd: Seq[AbstractVector]): Seq[Double] = {
    if (fst.size != snd.size) throw new Exception("Two vector sets do not have the same length!")

    (fst, snd).zipped.map {
      case (x, y) => calculateSimilarity(x, y)
    }
  }

  /**
   * Get the similarity between two collections of vectors
   * @param users collection of user vectors
   * @param venues collection of  vectors
   * @return map - key = userId, value = (venueId, similarity to the user vector)
   */
  def calculateSimilaritiesBetweenUsersAndVenues(users: Seq[UserVector], venues: Seq[VenueVector]): Seq[(String, Seq[(String, String, Double)])] = {
    var similarities: mutable.MutableList[(String, Seq[(String, String, Double)])] = mutable.MutableList.empty
    for (user <- users) {
      val user_id = user.getFeatureValue[String](Cons.USER_ID).get
      var rankings: mutable.MutableList[(String, String, Double)] = mutable.MutableList.empty
      val start = System.currentTimeMillis
      for (venue <- venues) {
        val venueName: String = venue.getFeatureValue[String](Cons.VENUE_NAME).getOrElse("N/A")
        rankings += ((venue.getFeatureValue[String](Cons.VENUE_ID).get, venueName, calculateSimilarity(user, venue)))
      }
      similarities += ((user_id, rankings))
      println("Id = " + user.getFeatureValue[String](Cons.USER_ID).get + "; took ms = " + (System.currentTimeMillis() - start))
    }
    similarities.toList
  }

  def sortUserVenueSimilarities(similarities: Seq[(String, Seq[(String, String, Double)])]): Seq[(String, Seq[(String, String, Double)])] = {
    //Sort by value
    var sorted: mutable.MutableList[(String, Seq[(String, String, Double)])] = mutable.MutableList.empty
    for ((user, values) <- similarities) {
      sorted += ((user, values.toSeq.sortBy(-_._3)))
    }
    sorted.toList
  }

  def printTopKSimilarities(similarities: Seq[(String, Seq[(String, String, Double)])], k: Int) = {
    for ((user, values) <- getTopKSimilarities(similarities, k)) {
      println("\n user - " + user)
      for ((venueId, venueName, venueScore) <- values) {
        println("name - " + venueName + " venue - " + venueId + " score - " + venueScore)
      }
    }
  }

  def getTopKSimilarities(similaritiesSorted: Seq[(String, Seq[(String, String, Double)])], k: Int) = {
    var top5k: mutable.MutableList[(String, Seq[(String, String, Double)])] = mutable.MutableList.empty
    for ((user, values) <- similaritiesSorted) {
      top5k += ((user, values.take(k)))
    }
    top5k.toList
  }

  def getTopKSimilaritiesForUserString(userIndex: Int, similarities: Seq[(String, Seq[(String, Double)])], k: Int): String = {
    var values = similarities(userIndex)._2;
    var output: String = "[";
    if (k > 0) {
      output += values(0)._1;
      for (i <- 1 to k - 1) {
        output += "," + values(i)._1;
      }
    }
    output += "]"
    output
  }

  /**
   * Get the similarity between a vector and a collection of vectors
   * @param vec vector
   * @param col collection of vectors
   * @return list of similarities between vectors
   */
  def calculateSimilarity(vec: AbstractVector, col: Seq[AbstractVector]): Seq[Double] = {
    col.map {
      s => calculateSimilarity(s, vec)
    }
  }

  /**
   * Get the similarity between two vectors
   * @param fst first item
   * @param snd second item
   * @return similarity expressed as { @code double}
   */
  def calculateSimilarity(fst: AbstractVector, snd: AbstractVector): Double = {
    // categories similarity
    val userCategories = fst.getFeatureValue[Seq[String]](Cons.CATEGORY).get
    val venuesCategories = snd.getFeatureValue[Seq[String]](Cons.CATEGORY).get
    var catSim = .0
    userCategories.foreach(cat1 =>
      venuesCategories.foreach(cat2 =>
        try {
          catSim += Category.getCategoriesSimilarity(cat1, cat2)
        }
        catch {
          case e: Exception => //println(e.getMessage)
        }
      )
    )

    if (userCategories.size * venuesCategories.size == 0) catSim = 0
    else catSim /= userCategories.size * venuesCategories.size

    //coordinates' similarities
    val user_coord = fst.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get
    val user_pop = fst.getFeatureValue[Double](Cons.POPULARITY).get

    val venue_coord = snd.getFeatureValue[(Double, Double)](Cons.GPS_COORDINATES).get
    val venue_pop = snd.getFeatureValue[Double](Cons.POPULARITY).get

    val dotProduct = user_coord._1 * venue_coord._1 + user_coord._2 * venue_coord._2 + user_pop * venue_pop

    val norm = Math.sqrt(user_coord._1 * user_coord._1 + user_coord._2 * user_coord._2 + user_pop * user_pop) * Math.sqrt(venue_coord._1 * venue_coord._1 + venue_coord._2 * venue_coord._2 + venue_pop * venue_pop)


    dotProduct / norm + catSim
  }
}
