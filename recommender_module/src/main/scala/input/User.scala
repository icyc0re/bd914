package input

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import vectors.{VenueVector, UserVector}
import utils.Cons
import features.{TextFeature, DoubleFeature}


case class Contact(twitter: Option[String], facebook: Option[String], phone: Option[String], email: Option[String])

case class Friends(count: Double, items: List[String])

case class UserCompact(id: String, gender: Option[String], homeCity: Option[String], checkinsCount: Option[Int],
                       contact: Option[Contact], friendsCount: Option[Int])

object Gender extends Enumeration {
  type Gender = Value
  val male, female, none = Value
}

case class GPSCoordinates(lat: Double, lon: Double)

class User(jsonString: String) {

  import Gender._

  val jsonFile: JsValue = Json.parse(jsonString)

  implicit val userContactRead: Reads[Contact] = (
    (__ \ "twitter").readNullable[String] and
      (__ \ "facebook").readNullable[String] and
      (__ \ "phone").readNullable[String] and
      (__ \ "email").readNullable[String]
    )(Contact.apply _)

  implicit val userCompactRead: Reads[UserCompact] = (
    (JsPath \ "user" \ "id").read[String] and
      (JsPath \ "user" \ "gender").readNullable[String] and
      (JsPath \ "user" \ "homeCity").readNullable[String] and
      (JsPath \ "user" \ "checkins" \ "count").readNullable[Int] and
      (JsPath \ "user" \ "contact").readNullable[Contact] and
      (JsPath \ "user" \ "friends" \ "count").readNullable[Int]
    )(UserCompact.apply _)

  val user: UserCompact = {
    var JSuser: JsResult[UserCompact] = jsonFile.validate[UserCompact](userCompactRead)
    JSuser match {
      case s: JsSuccess[UserCompact] => s.get
      case e: JsError => UserCompact("JsError", None, None, None, None, None)
    }
  }

  //basic info
  val gender: Gender = withName(user.gender.get)
  val homeCity: String = user.homeCity.get
  val id: String = user.id

  //checkins count
  val checkinsCount = user.checkinsCount.get

  //social networks
  val contact = user.contact.get

  //partial list of friends
  val friends = Friends(user.friendsCount.get, Nil)

  def canEqual(other: Any): Boolean = other.isInstanceOf[User]

  override def equals(other: Any): Boolean = other match {
    case that: User =>
      (that canEqual this) &&
        id == that.id
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(id)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }


  def displayUser() {
    println("User : " + id + "\n")
  }

}


object User {

  var users: Seq[(UserVector, Seq[VenueVector])] = Nil

  def getAll: Seq[(UserVector, Seq[VenueVector])] = {
    users match {
      case Nil =>
        users = new RawUserInputProcessor().processInDir(Cons.USERS_PATH)
      case _ => // nothing
    }
    users
  }

  //TODO: add features category and location
  def featureVector(u: User): UserVector = {
    val features = List(
      TextFeature(Cons.USER_ID, u.id),
      DoubleFeature(Cons.POPULARITY, u.friends.count)
    )
    new UserVector(features, null)
  }
}


