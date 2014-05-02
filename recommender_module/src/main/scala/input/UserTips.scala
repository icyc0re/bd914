package input

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
 * @author Matteo Pagliardini
 */

case class TipsItems(text : Option[String], venue : Option[VenueCompact2])

case class UserTipsCompact(count : Option[Int], items : Option[List[TipsItems]])

class UserTips( override val jsonString: String) extends InteractionInputProcessor(jsonString ) {

	val jsonFile:JsValue = Json.parse(jsonString)

	implicit val tipsItemsRead: Reads[TipsItems] = (
      (__ \ "text").readNullable[String] and
      (__ \ "venue").readNullable[VenueCompact2]
    )(TipsItems.apply _)

	implicit val userTipsCompactRead: Reads[UserTipsCompact] = (
      (JsPath \ "tips" \ "count").readNullable[Int] and
      (JsPath \ "tips" \ "items").readNullable[List[TipsItems]]
    )(UserTipsCompact.apply _)

    val userTips : UserTipsCompact  = {
		var JSvenue: JsResult[UserTipsCompact] = jsonFile.validate[UserTipsCompact](userTipsCompactRead)
    	JSvenue match {
	  		case s: JsSuccess[UserTipsCompact] => s.get.asInstanceOf[UserTipsCompact]
	 		case e: JsError => UserTipsCompact(None, None)
		}
	}

}