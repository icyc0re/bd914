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

  val FRIENDS_COUNT = "user.friends.count"
  val USER_CHECKINS = "user.checkins.count"
  val GENDER = "gender"
  val HOME_CITY = "homeCity"
  val USER_ID = "user.id"

  val CATEGORY = "venue.category"
  val PRICE = "venue.price"
  val GPS_COORDINATES = "gps_coordinates"
  val RADIUS = "radius" 
  val POPULARITY = "popularity"
  val TIME = "time"

  val USERS_PATH = "../dataset/small_sample/users/"
  val VENUES_PATH = "../dataset/sample/venues/"
  val INTERACTIONS_PATH = "../dataset/small_sample/users/"

  val DATA_ROOT = "../../../dataset/"
  val NEW_USER_DIRECTORY = DATA_ROOT + "new_user/"
  val CHECKINS_DIRECTORY = NEW_USER_DIRECTORY + "checkins/"
  val RECOMMENDATIONS_DIRECTORY = NEW_USER_DIRECTORY + "new_user/recommendations/"

  val CATEGORIES_INPUT_PATH = "../dataset/sample/ordered_categories.txt"
  val CATEGORIES_MATRIX_INPUT_PATH = "../dataset/sample/categories_similarity.txt"

  val WEBAPP_RESULT_URL = "http://webapp.com:80/result"
  val WEBAPP_RESULT_PARAM_USERID = "userId"
  val WEBAPP_RESULT_PARAM_VENUEIDS = "venueIds"


  val FEATURE_RATING = "Rating" //Name given to the feature containing the rating of a venue. This feature is added during the similarity computation phase

  val NY_AREA = Map[String, Double]("s" -> 40.526851, "n" -> 40.913515, "w" -> -74.271888, "e" -> -73.689612)

  val HOME_PRIVATE = "Home (private)"

  val PRECISION_DELETION_FACTOR = 0.3
}


