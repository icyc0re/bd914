package utils

import utils.Config

/**
 * Use this class to specify all constants that are used
 * @author Ivan Gavrilović
 */
object Cons {

  val CHECKINS_COUNT = "checkinsCount"
  val USERS_COUNT = "usersCount"
  val TIP_COUNT = "tipCount"
  val VENUE_ID = "venue.id"
  val VENUE_ISVERIFIED = "venue.isVerified"

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

  val DATA_ROOT = "/data/"
  val SAMPLE_PATH = DATA_ROOT
  val USERS_PATH = SAMPLE_PATH + "users/"
  val VENUES_PATH = SAMPLE_PATH + "venues/"
  val INTERACTIONS_PATH = SAMPLE_PATH + "users/"

  val NEW_USER_DIRECTORY = DATA_ROOT + "new_user/"
  val CHECKINS_DIRECTORY = NEW_USER_DIRECTORY + "checkins/"
  val RECOMMENDATIONS_DIRECTORY = NEW_USER_DIRECTORY + "recommendations/"

  val CATEGORIES_INPUT_PATH = DATA_ROOT+"ordered_categories.txt"
  val CATEGORIES_MATRIX_INPUT_PATH = DATA_ROOT+"categories_similarity.txt"

  val WEBAPP_RESULT_URL = "http://webapp.com:80/result"
  val WEBAPP_RESULT_PARAM_USERID = "userId"
  val WEBAPP_RESULT_PARAM_VENUEIDS = "venueIds"


  val FEATURE_RATING = "Rating" //Name given to the feature containing the rating of a venue. This feature is added during the similarity computation phase

  val NY_AREA = Map[String, Double]("s" -> 40.526851, "n" -> 40.913515, "w" -> -74.271888, "e" -> -73.689612)
  val DEFAULT_COORDINATES = ((NY_AREA("s")+NY_AREA("n"))/2,(NY_AREA("w")+NY_AREA("e"))/2)
  
  
  val HOME_PRIVATE = "Home (private)"

  val PRECISION_DELETION_FACTOR = 0.3
  val PRECISION = "precision"

  val USERS_MAX = 10
  val VENUES_SERIALIZED = DATA_ROOT+"venues_serialized"

  val TOP_K_COUNT = 20

  val MAYORSHIPS_WEIGHT = 4
  val CATEGORIES_WEIGHT = 10
  val WEIGHT_DISTANCE = 1 //weight to control the importance of the distance to the venue
  val WEIGHT_TIME = 0.1
  val WEIGHT_ISVERRIFIED = 0.01
}


