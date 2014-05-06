package input


import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer
import vectors.{VenueVector, UserVector}
import utils.Cons
import features.{TextFeature, DoubleFeature}


case class CheckinsCount(count: Double)

case class Contact(twitter: Option[String], facebook: Option[String], phone: Option[String], email: Option[String])

case class UserInteractions(count: Double, items: List[String])

case class Friends(count: Double, items: List[String])

object Gender extends Enumeration {
  type Gender = Value
  val male, female, none = Value
}

case class GPSCoordinates(lat: Double, lon: Double)

class User(jsonString: String) {

  import Gender._

  val json: Option[Any] = JSON.parseFull(jsonString)

  private val map: Map[String, Any] = json.get.asInstanceOf[Map[String, Any]]
  private val user: Map[String, Any] = map("user").asInstanceOf[Map[String, Any]]

  //basic info
  val gender: Gender = withName(user("gender").asInstanceOf[String])
  val homeCity: String = user("homeCity").asInstanceOf[String]
  val id: String = user("id").asInstanceOf[String]

  //checkins count
  private val _checkins: Map[String, Any] = user("checkins").asInstanceOf[Map[String, Any]]
  val checkinsCount = CheckinsCount(_checkins("count").asInstanceOf[Double])

  //social networks
  private val _contact: Map[String, String] = user("contact").asInstanceOf[Map[String, String]]
  val contact = Contact(_contact.get("twitter"), _contact.get("facebook"), _contact.get("phone"), _contact.get("email"))

  //partial list of friends
  private val _friends: Map[String, Any] = user("friends").asInstanceOf[Map[String, Any]]
  private val groups: List[Any] = _friends("groups").asInstanceOf[List[Any]]
  var friendsList = new ListBuffer[String]
  var friendsCount: Double = 0

  groups.foreach(group => {
    val grp: Map[String, Any] = group.asInstanceOf[Map[String, Any]]
    friendsCount += grp("count").asInstanceOf[Double]
    val items: List[Any] = grp("items").asInstanceOf[List[Any]]
    items.foreach(item => {
      val it: Map[String, Any] = item.asInstanceOf[Map[String, Any]]
      val id: String = it("id").asInstanceOf[String]
      friendsList += id
    })
  })

  val friends = Friends(friendsCount, friendsList.toList)

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

  def featureVector(u: User): UserVector = {
    val features = List(
      TextFeature(Cons.USER_ID, u.id),
      DoubleFeature(Cons.POPULARITY, u.friends.count)
    )
    new UserVector(features, null)
  }
}


