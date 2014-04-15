package input

import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.data.validation.ValidationError
import scala.util.parsing.json.JSON
import scala.collection.mutable.ListBuffer
	
class User(jsonFile : String) {
  
  	case class Checkins(count:Double)
  	case class Contact(twitter:Option[String], facebook:Option[String])
  
	val jsonString = scala.io.Source.fromFile(jsonFile).mkString
  	val json:Option[Any] = JSON.parseFull(jsonString)

  	private val map:Map[String,Any] = json.get.asInstanceOf[Map[String, Any]]
  	private val user:Map[String,Any] = map.get("user").get.asInstanceOf[Map[String,Any]]
	val gender:String = user.get("gender").get.asInstanceOf[String]
	val homeCity:String = user.get("homeCity").get.asInstanceOf[String]
  	private val _checkins:Map[String,Any] = user.get("checkins").get.asInstanceOf[Map[String,Any]]
	val checkins = Checkins(_checkins.get("count").get.asInstanceOf[Double])
	val _contact: Map[String,Any] = user.get("contact").get.asInstanceOf[Map[String,Any]]
	//val contact = Contact(_contact.get("twitter").get.asInstanceOf[Option[String]],
	//					  _contact.get("facebook").get.asInstanceOf[Option[String]])
	private val friends:Map[String,Any] = user.get("friends").get.asInstanceOf[Map[String,Any]]
	private val groups:List[Any] = friends.get("groups").get.asInstanceOf[List[Any]]
	private val mayorships: Map[String,Any] = user.get("mayorships").get.asInstanceOf[Map[String,Any]]
	val mayorshipsCount: Double = mayorships.get("count").get.asInstanceOf[Double]
	var friendsList = new ListBuffer[String]
	var friendsCount : Double = 0
		
	groups.foreach( group => {
		val grp: Map[String,Any] = group.asInstanceOf[Map[String,Any]]
		friendsCount = grp.get("count").get.asInstanceOf[Double]
		val items:List[Any] = grp.get("items").get.asInstanceOf[List[Any]]
		items.foreach( item => {
			val it: Map[String,Any] = item.asInstanceOf[Map[String,Any]]
			val id : String = it.get("id").get.asInstanceOf[String]
			friendsList += id
		})
	})
	
}

