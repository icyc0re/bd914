package input

import utils.Cons


import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import features.{TextFeature, IntFeature}
import utils.Cons
import vectors.VenueVector

/**
 * @author Matteo Pagliardini
 */
case class VenueContact(twitter: Option[String], facebook: Option[String], phone: Option[String])

case class VenueStats(checkinsCount: Int, usersCount: Option[Int], tipCount: Option[Int])

case class VenueLocation(address: Option[String], crossStreet: Option[String], city: Option[String],
                         state: Option[String], postalCode: Option[String], lat: Option[Double], lng: Option[Double])

case class VenueCategory(id: Option[String], name: String, pluralName: Option[String], shortName: Option[String],
                         primary: Option[Boolean])

case class VenuePrice(tier: Option[Int], message: Option[String], currency: Option[String])

case class VenueReasonsItem(summary: Option[String], tyype: Option[String], reasonName: Option[String])

case class VenueReasons(count: Option[Int], items: Option[List[VenueReasonsItem]])

case class VenueMayor(count: Option[Int], id: Option[String], firstName: Option[String], lastName: Option[String],
                      gender: Option[String], homeCity: Option[String], relationship: Option[String])

case class VenueAttributeItem(displayName: Option[String], displayValue: Option[String], priceTier: Option[Int])

case class VenueAttribute(tyype: Option[String], name: Option[String], summary: Option[String],
                          count: Option[Int], items: Option[List[VenueAttributeItem]])

case class VenueAttributes(groups: Option[List[VenueAttribute]], dummy: Option[Int])

case class VenueHoursRenderedTime(renderedTime: Option[String], dummy: Option[Int])

case class VenueHoursTimeFrames(days: Option[String], open: Option[List[VenueHoursRenderedTime]])

case class VenueHours(dummy: Option[Int], timeframes: Option[List[VenueHoursTimeFrames]])

case class VenuePhotosGroupItemUser(id: Option[String], firstName: Option[String], lastName: Option[String], gender: Option[String])

case class VenuePhotosGroupItem(id: Option[String], visibility: Option[String], user: Option[VenuePhotosGroupItemUser])

case class VenuePhotosGroup(tyype: Option[String], count: Option[Int], items: Option[List[VenuePhotosGroupItem]])

case class VenuePhotos(count: Option[Int], groups: Option[List[VenuePhotosGroup]])

case class VenueTipsGroupItem(id: Option[String], text: Option[String], likes: Option[Int])

case class VenueTipsGroup(user: Option[VenuePhotosGroupItemUser], tyype: Option[String], count: Option[Int],
                          items: Option[List[VenueTipsGroupItem]])

case class VenueTips(count: Option[Int], groups: Option[List[VenueTipsGroup]])

case class VenuePhrases(phrase: Option[String], count: Option[Int])

case class VenueCompact(categories: Option[List[VenueCategory]], id: String, name: String, url: Option[String], stats: VenueStats,
                        price: Option[VenuePrice], likes: Option[Int], rating: Option[Int], reasons: Option[VenueReasons],
                        mayor: Option[VenueMayor], tags: List[String], contact: Option[VenueContact], attributes: Option[VenueAttributes],
                        hours: Option[VenueHours], verified: Option[Boolean], photos: Option[VenuePhotos], tips: Option[VenueTips],
                        phrases: Option[List[VenuePhrases]])

class Venue(jsonString: String) {
  val jsonFile: JsValue = Json.parse(jsonString)

  //Jackson way :
  /*val mapper = new ObjectMapper() with ScalaObjectMapper
mapper.registerModule(DefaultScalaModule)
val obj = mapper.readValue[Map[String, Map[String, Object]]](jsonString)

val map = mapper.readValue[Map[String, Any]](obj.get("venue").get("stats"))
println(map)*/

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

  implicit val categoryRead: Reads[VenueCategory] = (
    (__ \ "id").readNullable[String] and
      (__ \ "name").read[String] and
      (__ \ "pluralName").readNullable[String] and
      (__ \ "shortName").readNullable[String] and
      (__ \ "primary").readNullable[Boolean]
    )(VenueCategory.apply _)

  implicit val venueStatRead: Reads[VenueStats] = (
    (__ \ "checkinsCount").read[Int] and
      (__ \ "usersCount").readNullable[Int] and
      (__ \ "tipCount").readNullable[Int]
    )(VenueStats.apply _)

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

  implicit val venueCompactRead: Reads[VenueCompact] = (
    (JsPath \ "venue" \ "categories").readNullable[List[VenueCategory]] and
      (JsPath \ "venue" \ "id").read[String] and
      (JsPath \ "venue" \ "name").read[String] and
      (JsPath \ "venue" \ "url").readNullable[String] and
      (JsPath \ "venue" \ "stats").read[VenueStats] and
      (JsPath \ "venue" \ "price").readNullable[VenuePrice] and
      (JsPath \ "venue" \ "likes" \ "count").readNullable[Int] and
      (JsPath \ "venue" \ "rating").readNullable[Int] and
      (JsPath \ "venue" \ "reasons").readNullable[VenueReasons] and
      (JsPath \ "venue" \ "mayor").readNullable[VenueMayor] and
      (JsPath \ "venue" \ "tags").read[List[String]] and
      (JsPath \ "venue" \ "contact").readNullable[VenueContact] and
      (JsPath \ "venue" \ "attributes").readNullable[VenueAttributes] and
      (JsPath \ "venue" \ "hours").readNullable[VenueHours] and
      (JsPath \ "venue" \ "verified").readNullable[Boolean] and
      (JsPath \ "venue" \ "photos").readNullable[VenuePhotos] and
      (JsPath \ "venue" \ "tips").readNullable[VenueTips] and
      (JsPath \ "venue" \ "phrases").readNullable[List[VenuePhrases]]
    )(VenueCompact.apply _)

  var venue: VenueCompact = jsonFile.validate[VenueCompact](venueCompactRead).get

  /**
   * Display the features in a nice form
   */
  /*
  def displayFeatures() {
  	println("\n****************************************************")
  	println(  "VENUE : "+venue.id)
  	println(  "****************************************************\n")
	println(  "\n-------------- Basic info : ")
	println(  "name := "+venue.name)
	println(  "url := "+venue.url)
	println(  "likes := "+venue.likes)
	println(  "rating := "+venue.rating)
	println(  "verified := "+venue.verified)
	println(  "\n-------------- Stats : ")
	println(  "checkinsCount := "+venue.stats.checkinsCount)
	println(  "tipCount := "+venue.stats.tipCount)
	println(  "\n-------------- Price : ")
	if(venue.price != None){
		println(  "tier := "+venue.price.tier)
		println(  "message := "+venue.price.message)
		println(  "currency := "+venue.price.currency)
	} else{
		println(  "None")
	}
	println(  "\n-------------- Reasons : ")
	if(venue.reasons != None){
		println(  "count := "+venue.reasons.count)
		println(  "Wait ! There is more, look by yourself ...")
	} else{
		println(  "None")
	}
	println(  "\n-------------- Mayor : ")
	if(venue.mayor != None){
		println(  "count := "+venue.mayor.count)
		println(  "id := "+venue.mayor.id)
		println(  "firstName := "+venue.mayor.firstName)
		println(  "lastName := "+venue.mayor.lastName)
		println(  "gender := "+venue.mayor.gender)
		println(  "homeCity := "+venue.mayor.homeCity)
		println(  "relationship := "+venue.mayor.relationship)
	} else{
		println(  "None")
	}
	println(  "\n-------------- Tags : ")
	println(  "Tags := "+venue.tags)
	println(  "\n-------------- Contact : ")
	if(venue.contact != None){
		println(  "twitter := "+venue.contact.twitter)
		println(  "facebook := "+venue.contact.facebook)
		println(  "phone := "+venue.contact.phone)
	} else{
		println(  "None")
	}
	println(  "\n-------------- Attributes : ")
	if(venue.contact != None){
		println(  "groups := "+venue.attributes.groups)
	} else{
		println(  "None")
	}
	println(  "\nAnd even more stuff like hours / photos / tips / phrases ... !!! ")
	println("\n****************************************************")
  	println(  "****************************************************\n")
  }*/
}

object Venue{
  /**
   * Create venue feature vector from the parsed [[Venue]] object
   * @param v parsed venue
   * @return venue feature vector
   */
  def featureVector(v: Venue):VenueVector = {
    val features = List(
      IntFeature(Cons.CHECKINS_COUNT, v.venue.stats.checkinsCount),
      IntFeature(Cons.TIP_COUNT, v.venue.stats.tipCount.get),
      IntFeature(Cons.USERS_COUNT, v.venue.stats.usersCount.get),
      TextFeature(Cons.VENUE_ID, v.venue.id)
    )
    new VenueVector(features, null)
  }
}
