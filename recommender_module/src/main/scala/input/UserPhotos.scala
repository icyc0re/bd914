package input

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
 * @author Matteo Pagliardini
 */


class UserPhotos(override val jsonString: String) extends Interactions(jsonString) {

  val jsonFile: JsValue = Json.parse(jsonString)

  implicit val photosItemsRead: Reads[InteractionItem] = (
    (__ \ "suffix").readNullable[String] and
      (__ \ "venue").readNullable[VenueCompact2]
    )(InteractionItem.apply _)

  implicit val userPhotosCompactRead: Reads[InteractionCompact] = (
    (JsPath \ "tips" \ "count").readNullable[Int] and
      (JsPath \ "tips" \ "items").readNullable[List[InteractionItem]]
    )(InteractionCompact.apply _)

  override val compact: InteractionCompact = {
    var JSvenue: JsResult[InteractionCompact] = jsonFile.validate[InteractionCompact](userPhotosCompactRead)
    JSvenue match {
      case s: JsSuccess[InteractionCompact] => s.get.asInstanceOf[InteractionCompact]
      case e: JsError => InteractionCompact(None, None)
    }
  }

}