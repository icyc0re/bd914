package input

import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer
import vectors.UserVector

import utils.Cons
import features.DoubleFeature
import features.GenderFeature
import features.TextFeature

case class Checkins(count: Double)

case class Contact(twitter: Option[String], facebook: Option[String], phone: Option[String], email: Option[String])

object Gender extends Enumeration {
  type Gender = Value
  val male, female, none = Value
}

class User(jsonString: String) {

  import Gender._

  val json: Option[Any] = JSON.parseFull(jsonString)

  private val map: Map[String, Any] = json.get.asInstanceOf[Map[String, Any]]
  private val user: Map[String, Any] = map("user").asInstanceOf[Map[String, Any]]

  val gender: Gender = withName(user("gender").asInstanceOf[String])
  val homeCity: String = user("homeCity").asInstanceOf[String]
  val id: String = user("id").asInstanceOf[String]

  private val _checkins: Map[String, Any] = user("checkins").asInstanceOf[Map[String, Any]]
  val checkins = Checkins(_checkins("count").asInstanceOf[Double])

  val _contact: Map[String, String] = user("contact").asInstanceOf[Map[String, String]]
  val contact = Contact(_contact.get("twitter"), _contact.get("facebook"), _contact.get("phone"), _contact.get("email"))

  private val friends: Map[String, Any] = user("friends").asInstanceOf[Map[String, Any]]
  private val groups: List[Any] = friends("groups").asInstanceOf[List[Any]]
  private val mayorships: Map[String, Any] = user("mayorships").asInstanceOf[Map[String, Any]]
  val mayorshipsCount: Double = mayorships("count").asInstanceOf[Double]
  var friendsList = new ListBuffer[String]
  var friendsCount: Double = 0

  //TODO get the list of venues for this user i.e. id of venues that this user interacted with
  val venues:Set[String] = Set.empty

  groups.foreach(group => {
    val grp: Map[String, Any] = group.asInstanceOf[Map[String, Any]]
    friendsCount = grp("count").asInstanceOf[Double]
    val items: List[Any] = grp("items").asInstanceOf[List[Any]]
    items.foreach(item => {
      val it: Map[String, Any] = item.asInstanceOf[Map[String, Any]]
      val id: String = it("id").asInstanceOf[String]
      friendsList += id
    })
  })


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
}

object User {
  def featureVector(u: User): UserVector = {
    val features = List(
      DoubleFeature(Cons.USER_CHECKINS, u.checkins.count),
      DoubleFeature(Cons.TIP_COUNT, u.friendsCount),
      GenderFeature(Cons.GENDER, u.gender),
      UserVector.homeCity(u.homeCity),
      TextFeature(Cons.USER_ID, u.id)
    )
    new UserVector(features, null)
  }
}


