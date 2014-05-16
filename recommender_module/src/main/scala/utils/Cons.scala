package utils


/**
 * Use this class to specify all constants that are used
 * @author Ivan Gavrilović
 */
object Cons {

  val CHECKINS_COUNT = "checkinsCount"
  val USERS_COUNT = "usersCount"
  val TIP_COUNT = "tipCount"
  val VENUE_ID = "venue.id"
  val VENUE_NAME = "venue.name"

  val FRIENDS_COUNT = "user.friends.count"
  val USER_CHECKINS = "user.checkins.count"
  val GENDER = "gender"
  val HOME_CITY = "homeCity"
  val USER_ID = "user.id"

  val CATEGORY = "venue.category"
  val PRICE = "venue.price"
  val GPS_COORDINATES = "gps_coordinates"
  val POPULARITY = "popularity"
  val TIME = "time"

  val FEATURE_RATING = "Rating" //Name given to the feature containing the rating of a venue. This feature is added during the similarity computation phase

  val NY_AREA = Map[String, Double]("s" -> 40.526851, "n" -> 40.913515, "w" -> -74.271888, "e" -> -73.689612)

  val HOME_PRIVATE = "Home (private)"

  /**
   * CONFIGURATION PARAMETERS
   */
  val DATA_ROOT = "/data"

  val USERS_PATH = DATA_ROOT + "/users/"
  val VENUES_PATH = DATA_ROOT + "/venues/"
  val INTERACTIONS_PATH = DATA_ROOT + "/users/"

  val CATEGORIES_INPUT_PATH = DATA_ROOT + "/ordered_categories.txt"
  val CATEGORIES_MATRIX_INPUT_PATH = DATA_ROOT + "/categories_similarity.txt"

  val WEBAPP_RESULT_URL = "http://webapp.com:80/result"
  val WEBAPP_RESULT_PARAM_USERID = "userId"
  val WEBAPP_RESULT_PARAM_VENUEIDS = "venueIds"

  val JOB_DONE_URL: String = "https://google.com"


  val SPARK_HOME: String = System.getenv("SPARK_HOME")
  val SPARK_MASTER = "spark://bigdataig1:7077"
  val SPARK_JOB = "RecommenderApp"
  val SPARK_JARS: Seq[String] = List("target/scala-2.10/Recommender_cluster-assembly-1.0.jar")
  val PRECISION_DELETION_FACTOR = 0.3
}

