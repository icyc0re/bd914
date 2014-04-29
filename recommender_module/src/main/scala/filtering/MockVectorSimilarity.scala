package filtering

import vectors.{VenueVector, UserVector, AbstractVector}
import features.{DoubleFeature, IntFeature, CoordinatesFeature}
import utils.{Cons, Haversine}
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
  def calculateSimilaritiesBetweenUsersAndVenues(users: Seq[UserVector], venues: Seq[VenueVector]): mutable.Map[String, Seq[(String, Double)]] = {
    var similarities = mutable.Map.empty[String, Seq[(String, Double)]]
    for (user <- users) {
      for (venue <- venues) {
        val user_id = user.getFeatureValue[String](Cons.USER_ID).get
        similarities += user_id -> (similarities.getOrElse(user_id, List.empty)
          :+ (venue.getFeatureValue[String](Cons.VENUE_ID).get -> MockVectorSimilarity.calculateSimilarity(user, venue)))
      }
    }
    similarities
  }

  def sortUserVenueSimilarities(similarities : mutable.Map[String, Seq[(String, Double)]]) = {
    //Sort by value
    for ((user, values) <- similarities) {
      similarities += user -> values.toSeq.sortBy(-_._2)
    }
  }

  def printTopKSimilarities(similarities : mutable.Map[String, Seq[(String, Double)]], k: Int) = {
    for ((user, values) <- similarities) {
      print("user - top similarities [ ")
      for(i <- 0 to k-1) {
        print(i + " ")
      }
      print("] " + f"$user%-10s")
      for(i <- 0 to k-1) {
        var value : Double = values(i)._2;
        print(" " + f"$value%-17.15f")
      }
      println()
    }
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
    userCategories.foreach(cat1=>
      venuesCategories.foreach(cat2=>
        catSim += Category.getCategoriesSimilarity(cat1, cat2)
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

    val norm =  Math.sqrt(user_coord._1*user_coord._1+user_coord._2*user_coord._2 +user_pop*user_pop) * Math.sqrt(venue_coord._1*venue_coord._1+venue_coord._2*venue_coord._2 +venue_pop*venue_pop)


    dotProduct/norm + catSim
//    // get (x0*y0 + x1*y1 + ... + x_n * y_n)
//    val dotProd = (fst.getFeaturesTyped[IntFeature], snd.getFeaturesTyped[IntFeature]).zipped.foldRight(0) {
//      (x: (IntFeature, IntFeature), b: Int) =>
//        b + x._1.value * x._2.value
//    }
//
//    // get ||x|| and ||y||
//    val intensityFst = Math.sqrt(fst.getFeaturesTyped[IntFeature].foldRight(0)((x:IntFeature, b:Int) => b + x.value*x.value))
//    val intensitySnd = Math.sqrt(snd.getFeaturesTyped[IntFeature].foldRight(0)((x:IntFeature, b:Int) => b + x.value*x.value))

    //dotProd / (intensityFst * intensitySnd)
  }
}
