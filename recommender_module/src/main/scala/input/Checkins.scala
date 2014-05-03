package input

/**
 * Created by Boris on 01/05/2014.
 */

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._


case class CheckinItem(id: String, tyype: String, timeZoneOffset: Int, createdAt: Long, venue: Option[VenueCompact2]) //, venue: Option[VenueCompact]

case class CheckinsCompact(count: Int, items: Option[List[CheckinItem]])

class Checkins(jsonString: String) {

  var jsonFile = Json.parse(jsonString)

  //Play way :
  implicit val venuePhrasesRead: Reads[VenuePhrases] = (
    (__ \ "phrase").readNullable[String] and
      (__ \ "count").readNullable[Int]
    )(VenuePhrases.apply _)

  implicit val venueTipsGroupItemRead: Reads[VenueTipsGroupItem] = (
    (__ \ "id").readNullable[String] and
      (__ \ "text").readNullable[String] and
      (__ \ "likes" \ "count").readNullable[Int]
    )(VenueTipsGroupItem.apply _)

  implicit val venueTipsGroupRead: Reads[VenueTipsGroup] = (
    (__ \ "user").readNullable[VenuePhotosGroupItemUser] and
      (__ \ "type").readNullable[String] and
      (__ \ "count").readNullable[Int] and
      (__ \ "items").readNullable[List[VenueTipsGroupItem]]
    )(VenueTipsGroup.apply _)

  implicit val venueTipsRead: Reads[VenueTips] = (
    (__ \ "count").readNullable[Int] and
      (__ \ "groups").readNullable[List[VenueTipsGroup]]
    )(VenueTips.apply _)

  implicit val venuePhotosGroupItemUserRead: Reads[VenuePhotosGroupItemUser] = (
    (__ \ "id").readNullable[String] and
      (__ \ "firstName").readNullable[String] and
      (__ \ "lastName").readNullable[String] and
      (__ \ "gender").readNullable[String]
    )(VenuePhotosGroupItemUser.apply _)

  implicit val venuePhotosGroupItemRead: Reads[VenuePhotosGroupItem] = (
    (__ \ "id").readNullable[String] and
      (__ \ "visibility").readNullable[String] and
      (__ \ "user").readNullable[VenuePhotosGroupItemUser]
    )(VenuePhotosGroupItem.apply _)

  implicit val venuePhotosGroupRead: Reads[VenuePhotosGroup] = (
    (__ \ "type").readNullable[String] and
      (__ \ "count").readNullable[Int] and
      (__ \ "items").readNullable[List[VenuePhotosGroupItem]]
    )(VenuePhotosGroup.apply _)

  implicit val venuePhotosRead: Reads[VenuePhotos] = (
    (__ \ "count").readNullable[Int] and
      (__ \ "groups").readNullable[List[VenuePhotosGroup]]
    )(VenuePhotos.apply _)

  implicit val venueHoursRenderedTimeRead: Reads[VenueHoursRenderedTime] = (
    (__ \ "renderedTime").readNullable[String] and
      (__ \ "ignoreThis").readNullable[Int]
    )(VenueHoursRenderedTime.apply _)

  implicit val venueHoursTimeFramesRead: Reads[VenueHoursTimeFrames] = (
    (__ \ "days").readNullable[String] and
      (__ \ "open").readNullable[List[VenueHoursRenderedTime]]
    )(VenueHoursTimeFrames.apply _)

  implicit val venueHoursRead: Reads[VenueHours] = (
    (__ \ "dummy").readNullable[Int] and
      (__ \ "timeframes").readNullable[List[VenueHoursTimeFrames]]
    )(VenueHours.apply _)

  implicit val venueAttributeItemRead: Reads[VenueAttributeItem] = (
    (__ \ "displayName").readNullable[String] and
      (__ \ "displayValue").readNullable[String] and
      (__ \ "priceTier").readNullable[Int]
    )(VenueAttributeItem.apply _)

  implicit val venueAttributeRead: Reads[VenueAttribute] = (
    (__ \ "type").readNullable[String] and
      (__ \ "name").readNullable[String] and
      (__ \ "summary").readNullable[String] and
      (__ \ "count").readNullable[Int] and
      (__ \ "items").readNullable[List[VenueAttributeItem]]
    )(VenueAttribute.apply _)

  implicit val venueAttributesRead: Reads[VenueAttributes] = (
    (__ \ "groups").readNullable[List[VenueAttribute]] and
      (__ \ "ignoreThis").readNullable[Int]
    )(VenueAttributes.apply _)

  implicit val venueContactRead: Reads[VenueContact] = (
    (__ \ "twitter").readNullable[String] and
      (__ \ "facebook").readNullable[String] and
      (__ \ "phone").readNullable[String]
    )(VenueContact.apply _)

  implicit val venueMayorRead: Reads[VenueMayor] = (
    (__ \ "count").readNullable[Int] and
      (__ \ "id").readNullable[String] and
      (__ \ "firstName").readNullable[String] and
      (__ \ "lastName").readNullable[String] and
      (__ \ "gender").readNullable[String] and
      (__ \ "homeCity").readNullable[String] and
      (__ \ "relationship").readNullable[String]
    )(VenueMayor.apply _)

  implicit val categoryRead: Reads[VenueCategory2] = (
    (__ \ "id").readNullable[String] and
      (__ \ "name").readNullable[String] and
      (__ \ "pluralName").readNullable[String] and
      (__ \ "shortName").readNullable[String] and
      (__ \ "primary").readNullable[Boolean]
    )(VenueCategory2.apply _)

  implicit val venueStatRead: Reads[VenueStats2] = (
    (__ \ "checkinsCount").readNullable[Int] and
      (__ \ "usersCount").readNullable[Int] and
      (__ \ "tipCount").readNullable[Int]
    )(VenueStats2.apply _)

  implicit val venuePriceRead: Reads[VenuePrice] = (
    (__ \ "tier").readNullable[Int] and
      (__ \ "message").readNullable[String] and
      (__ \ "currency").readNullable[String]
    )(VenuePrice.apply _)

  implicit val venueReasonsItemRead: Reads[VenueReasonsItem] = (
    (__ \ "summary").readNullable[String] and
      (__ \ "type").readNullable[String] and
      (__ \ "reasonName").readNullable[String]
    )(VenueReasonsItem.apply _)

  implicit val venueReasonsRead: Reads[VenueReasons] = (
    (__ \ "count").readNullable[Int] and
      (__ \ "items").readNullable[List[VenueReasonsItem]]
    )(VenueReasons.apply _)

  implicit val VenueLocationRead: Reads[VenueLocation] = (
    (__ \ "city").readNullable[String] and
      (__ \ "cc").readNullable[String] and
      (__ \ "state").readNullable[String] and
      (__ \ "lat").readNullable[Double] and
      (__ \ "lng").readNullable[Double]
    )(VenueLocation.apply _)

  implicit val venueCompactRead: Reads[VenueCompact2] = (
    (__ \ "categories").readNullable[List[VenueCategory2]] and
      (__ \ "id").readNullable[String] and
      (__ \ "name").readNullable[String] and
      (__ \ "url").readNullable[String] and
      (__ \ "stats").readNullable[VenueStats2] and
      (__ \ "price").readNullable[VenuePrice] and
      (__ \ "likes" \ "count").readNullable[Int] and
      (__ \ "rating").readNullable[Int] and
      (__ \ "reasons").readNullable[VenueReasons] and
      (__ \ "mayor").readNullable[VenueMayor] and
      (__ \ "tags").readNullable[List[String]] and
      (__ \ "contact").readNullable[VenueContact] and
      (__ \ "attributes").readNullable[VenueAttributes] and
      (__ \ "hours").readNullable[VenueHours] and
      (__ \ "verified").readNullable[Boolean] and
      (__ \ "photos").readNullable[VenuePhotos] and
      (__ \ "tips").readNullable[VenueTips] and
      (__ \ "phrases").readNullable[List[VenuePhrases]] and
      (__ \ "location").readNullable[VenueLocation]
    )(VenueCompact2.apply _)


  implicit val checkinRead: Reads[CheckinItem] = (
    (__ \ "id").read[String] and
      (__ \ "type").read[String] and
      (__ \ "timeZoneOffset").read[Int] and
      (__ \ "createdAt").read[Long] and
      (__ \ "venue").readNullable[VenueCompact2]
    )(CheckinItem.apply _)

  implicit val checkinsResponseRead: Reads[CheckinsCompact] = (
    (JsPath \ "response" \ "checkins" \ "count").read[Int] and
      (JsPath \ "response" \ "checkins" \ "items").readNullable[List[CheckinItem]]
    )(CheckinsCompact.apply _)

  val compact: CheckinsCompact = {
    var JScheckins: JsResult[CheckinsCompact] = jsonFile.validate[CheckinsCompact](checkinsResponseRead)
    JScheckins match {
      case s: JsSuccess[CheckinsCompact] => s.get
      case e: JsError => CheckinsCompact(-1, None)
    }
  }

  /**
   * Display the features in a nice form / Show some examples on how to access the data
   */
  def displayFeatures() {
    println("\n****************************************************")
    println("VENUE : " + compact.count)
    println("****************************************************\n")
    print(compact.toString)
    //println("name := " + checkinsResponse.items.get(1).id)
  }
}
