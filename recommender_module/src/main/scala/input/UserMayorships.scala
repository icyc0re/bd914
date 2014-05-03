package input

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
 * @author Matteo Pagliardini
 */


class UserMayorships(override val jsonString: String) extends Interactions(jsonString) {

  val jsonFile: JsValue = Json.parse(jsonString)

  implicit val mayorshipItemsRead: Reads[InteractionItem] = (
    (__ \ "dummy").readNullable[String] and
      (__ \ "venue").readNullable[VenueCompact2]
    )(InteractionItem.apply _)

  implicit val userMayorshipCompactRead: Reads[InteractionCompact] = (
    (JsPath \ "mayorships" \ "count").readNullable[Int] and
      (JsPath \ "mayorships" \ "items").readNullable[List[InteractionItem]]
    )(InteractionCompact.apply _)

  override val compact: InteractionCompact = {
    val JSvenue: JsResult[InteractionCompact] = jsonFile.validate[InteractionCompact](userMayorshipCompactRead)
    JSvenue match {
      case s: JsSuccess[InteractionCompact] => s.get
      case e: JsError => InteractionCompact(None, None)
    }
  }

}