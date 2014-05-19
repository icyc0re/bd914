package recommender

import scala.collection.mutable
import filtering.MockVectorSimilarity
import input.{UserInputProcessor, VenueInputProcessor, Category, User}
import precision._
import vectors._
import utils.{FileSys, Cons}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * This is the main class of the recommender system.
 * @author Ivan Gavrilović
 */
object RecommenderApp {
  def main(args: Array[String]) {
    if (Cons.IS_SPARK) {
      runSpark()
    }
    else {
      val start = System.currentTimeMillis()

      val v: Seq[VenueVector] = VenueVector.getAll

      var u: Seq[UserVector] = mutable.MutableList.empty
      var userInteractions: Map[UserVector, Map[VenueListType.VenueListType, Seq[VenueVector]]] = Map.empty
      if (args.size == 1 && args(0).contains("precision")) {
        userInteractions = Precision.modifyUserInteractions(User.getAll)
        u = Precision.getUserVectorFromUserInteractions(userInteractions)
      }
      else {
        // get user features
        u = UserVector.getAll
      }

      val similarities: Seq[(String, Seq[(String, Double)])] = MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(u, v)

      val sorted = MockVectorSimilarity.sortUserVenueSimilarities(similarities)
      val topK = MockVectorSimilarity.getTopKSimilarities(sorted, Cons.TOP_K_COUNT)
      if (args.size == 1 && args(0).contains("precision")) {
        Precision.calculatePrecision(topK, userInteractions, Cons.TOP_K_COUNT)
      }
      else {
        MockVectorSimilarity.printTopKSimilarities(topK)
      }

      println("Recommender took [ms] = " + (System.currentTimeMillis() - start))
    }
  }

  // UNCOMMENT FOR SPARK
  def runSpark() = {
    val NUM_NODES: Int = 16
    // create spark context and connect to master node
    val conf = new SparkConf()
      .setMaster("spark://bigdataig1:7077")
      .setAppName("Recommender")
      .set("spark.executor.memory", "1g")
      .setSparkHome(System.getenv("SPARK_HOME"))
      .setJars(List("target/scala-2.10/recommender_module-assembly-1.0.jar"))
      .set("spark.akka.frameSize", "1000")
    val sc = new SparkContext(conf)

    /**
     * INIT CATEGORY SIMILARITY MATRIX; WE DO THIS LOCALLY AS THE FILE IS VERY SMALL
     */
    println("Starting category processing...")
    Category.initMatrix(Cons.CATEGORIES_MATRIX_INPUT_PATH)

    def splitThirds(whole: Seq[String]) = {
      val parts = whole.splitAt(whole.size / 2)
      val half1 = parts._1.splitAt(parts._1.size / 2)
      val half2 = parts._2.splitAt(parts._2.size / 2)
      List(half1._1, half1._2, half2._1, half2._2)
    }

    val venuesWholeList: Seq[String] = FileSys.readDir(Cons.VENUES_PATH).take(50000)
    val venuesList: Seq[Seq[String]] = splitThirds(venuesWholeList)
    val userWholeList: Seq[String] = FileSys.readDir(Cons.USERS_PATH).take(4)
    val userList: Seq[Seq[String]] = splitThirds(userWholeList)
    val userVenues = sc.makeRDD(venuesList, 4).cartesian[Seq[String]](sc.makeRDD(userList, 4))

    println("Sending out all pairs")
    val similarities = userVenues.map((paths: (Seq[String], Seq[String])) => {
      var cnt = 0
      val venueVec: Seq[VenueVector] = paths._1.map {
        if (cnt % 1000 == 0) println("Venues parsed " + cnt)
        cnt += 1
        venuePath => new VenueInputProcessor().processData(FileSys.readFile(venuePath))
      }

      cnt = 0
      val userVec: Seq[UserVector] = paths._2.map {
        userPath =>
          if (cnt % 1000 == 0) println("Users parsed " + cnt)
          cnt += 1
          new UserInputProcessor().processData(FileSys.readFile(userPath))
      }

      MockVectorSimilarity.calculateSimilaritiesBetweenUsersAndVenues(userVec, venueVec)
    }
    ).collect().flatten.groupBy(_._1).map(p => (p._1, p._2.map(_._2).toSeq)).toSeq

    similarities

    /*
     * READ USERS, AND FORM USER VECTORS
    */

    println("Calculated similarities...")
  }
}
