package input

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

/**
 * @author Matteo Pagliardini
 */

case class PhotosItems(suffix : Option[String], venue : Option[VenueCompact2])

case class UserPhotosCompact(count : Option[Int], items : Option[List[PhotosItems]])

class UserPhotos( override val jsonString: String) extends InteractionInputProcessor(jsonString ) {

	val jsonFile:JsValue = Json.parse(jsonString)

	implicit val photosItemsRead: Reads[PhotosItems] = (
      (__ \ "suffix").readNullable[String] and
      (__ \ "venue").readNullable[VenueCompact2]
    )(PhotosItems.apply _)

	implicit val userPhotosCompactRead: Reads[UserPhotosCompact] = (
      (JsPath \ "tips" \ "count").readNullable[Int] and
      (JsPath \ "tips" \ "items").readNullable[List[PhotosItems]]
    )(UserPhotosCompact.apply _)

    val userTips : UserPhotosCompact  = {
		var JSvenue: JsResult[UserPhotosCompact] = jsonFile.validate[UserPhotosCompact](userPhotosCompactRead)
    	JSvenue match {
	  		case s: JsSuccess[UserPhotosCompact] => s.get.asInstanceOf[UserPhotosCompact]
	 		case e: JsError => UserPhotosCompact(None, None)
		}
	}

}