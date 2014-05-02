package input

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
 * @author Matteo Pagliardini
 */

case class MayorshipItems(dummy : Option[String], venue : Option[VenueCompact2]) //why dummy ? Ask me ... 

case class UserMayorshipCompact(count : Option[Int], items : Option[List[MayorshipItems]])

class UserMayorship( override val jsonString: String) extends InteractionInputProcessor(jsonString ) {

	val jsonFile:JsValue = Json.parse(jsonString)

	implicit val mayorshipItemsRead: Reads[MayorshipItems] = (
      (__ \ "dummy").readNullable[String] and
      (__ \ "venue").readNullable[VenueCompact2]
    )(MayorshipItems.apply _)

	implicit val userMayorshipCompactRead: Reads[UserMayorshipCompact] = (
      (JsPath \ "mayorships" \ "count").readNullable[Int] and
      (JsPath \ "mayorships" \ "items").readNullable[List[MayorshipItems]]
    )(UserMayorshipCompact.apply _)

    val userTips : UserMayorshipCompact  = {
		var JSvenue: JsResult[UserMayorshipCompact] = jsonFile.validate[UserMayorshipCompact](userMayorshipCompactRead)
    	JSvenue match {
	  		case s: JsSuccess[UserMayorshipCompact] => s.get.asInstanceOf[UserMayorshipCompact]
	 		case e: JsError => UserMayorshipCompact(None, None)
		}
	}

}