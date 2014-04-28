package input

import scala.util.parsing.json.JSON
import collection.JavaConversions._
import scala.collection.mutable.ListBuffer
import vectors.UserVector
import utils.Cons
import features.DoubleFeature
import features.GenderFeature
import features.TextFeature
//import java.nio.file.Files
//import java.nio.file.Paths
import vectors.VenueVector
import filtering.MockVectorSimilarity
import scala.collection.mutable.HashSet
import features.CoordinatesFeature

case class Checkins(count: Double)

case class Contact(twitter: Option[String], facebook: Option[String], phone: Option[String], email: Option[String])

case class Interactions(count: Double, items: List[String])

case class Friends(count: Double, items: List[String])

object Gender extends Enumeration {
  type Gender = Value
  val male, female, none = Value
}

case class GPSCoordinates(lat : Double, lon: Double)

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
  val checkins = Checkins(_checkins("count").asInstanceOf[Double])

  //social networks
  private val _contact: Map[String, String] = user("contact").asInstanceOf[Map[String, String]]
  val contact = Contact(_contact.get("twitter"), _contact.get("facebook"), _contact.get("phone"), _contact.get("email"))

  //partial list of friends
  private val _friends: Map[String, Any] = user("friends").asInstanceOf[Map[String, Any]]
  private val groups: List[Any] = _friends("groups").asInstanceOf[List[Any]]
  var friendsList = new ListBuffer[String]
  var friendsCount: Double = 0
  
  //interactions
  	//mayorships
  private val _mayorships: Map[String, Any] = user("mayorships").asInstanceOf[Map[String, Any]]
  val mayorships = Interactions (_mayorships("count").asInstanceOf[Double], readInteractions("mayorships"))
	//photos
  private val _photos: Map[String, Any] = user("photos").asInstanceOf[Map[String, Any]]
  val photos = Interactions (_photos("count").asInstanceOf[Double], readInteractions("photos"))
	//tips
  private val _tips: Map[String, Any] = user("tips").asInstanceOf[Map[String, Any]]
  val tips = Interactions (_tips("count").asInstanceOf[Double], readInteractions("tips"))
  	//all interactions
  val interactions = Interactions (mayorships.count+photos.count+tips.count, mayorships.items ++ photos.items ++ tips.items) 

  
  //TODO get the list of venues for this user i.e. id of venues that this user interacted with
  val venues:List[String] = List.empty

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
  
  /**
   * get venues associated to interaction_type
   * @param user user id
   * @return list of venues associated with the interaction
   */
  def readInteractions(interaction_type: String): List[String] = {
    //TODO: change absolute path
    val interaction_path : String = "../dataset/sample/users/"+interaction_type+"/"+id
    //check that interaction exists
    if(!new java.io.File(interaction_path).exists)
        return List.empty
        
    val jsonString = scala.io.Source.fromFile(interaction_path).mkString
    val json: Option[Any] = JSON.parseFull(jsonString)
    val map: Map[String, Any] = json.get.asInstanceOf[Map[String, Any]]
	val interactions: Map[String, Any] = map(interaction_type).asInstanceOf[Map[String, Any]]
    var interactionsList: ListBuffer[String] = new ListBuffer[String]
    
    val items: List[Any] = interactions("items").asInstanceOf[List[Any]]	    
    items.foreach(item => {
      val it : Map[String, Any] = item.asInstanceOf[Map[String, Any]]
      val venue: Map[String, Any] = it("venue").asInstanceOf[Map[String, Any]]
      val venue_id: String = venue("id").asInstanceOf[String]
      interactionsList += venue_id
    })
    
    interactionsList.toList
  }
  
  def getGPSCenter(): (Double, Double) = {

    var lat: Double = 0
    var lng: Double = 0
    
    for( interaction <- interactions.items){
      //get venue gps location
      val venue_path: String = "../dataset/sample/venues/"+interaction
      val jsonString = scala.io.Source.fromFile(venue_path).mkString
      val venue = new Venue(jsonString)
      lat += venue.venue.location.get.lat.get
      lng += venue.venue.location.get.lng.get
    }
    
    (lat / interactions.count, lng / interactions.count)
  }
  
  def getTopKVenues(k : Int, allVenueVectors: Seq[VenueVector]): Seq[String] = {
    
    // TODO fix this
    val userVenueVectors : Seq[VenueVector] = allVenueVectors.filter(x => interactions.items.contains(x.getFeatureValue(Cons.VENUE_ID).get))
    
    val userVector : UserVector = User.featureVector(this)
    userVector.applyVenues(userVenueVectors.toSeq)
    
    val similarities : Seq[Double]  = MockVectorSimilarity.calculateSimilarity(userVector, allVenueVectors)
    
    val seq = (userVenueVectors).zip(similarities)
    
    val sortedSeq : Seq[(VenueVector, Double)] = seq.sortWith((e1, e2) => (e1._2 compareTo e2._2) < 0)
    
    val topKVenueVectors : Seq[String] = sortedSeq.splitAt(k)._1.collect{
        case (x : (VenueVector, Double)) => x._1.getFeatureValue(Cons.VENUE_ID).get
    }
    
	return topKVenueVectors
  }

}

object User {
  def featureVector(u: User): UserVector = {
    val features = List(
        //TODO: use some of commented features
//      DoubleFeature(Cons.USER_CHECKINS, u.checkins.count),
//      DoubleFeature(Cons.TIP_COUNT, u.tips.count),
//      GenderFeature(Cons.GENDER, u.gender),
//      UserVector.homeCity(u.homeCity),
//      TextFeature(Cons.USER_ID, u.id),
      CoordinatesFeature(Cons.GPS_COORDINATES, u.getGPSCenter()),
      DoubleFeature(Cons.POPULARITY, compute_popularity(u.friends.count, u.interactions.count))
      //TODO: use different weights for the different types of interactions
    )
    new UserVector(features, null)
  }
  
  def compute_popularity(friendsCount: Double, interactionsCount: Double): Double = {
    
    friendsCount + interactionsCount
  }
}

