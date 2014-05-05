package input

/**
 * @author Matteo Pagliardini
 */

import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import vectors.VenueVector
import features.{CategoryFeature, CoordinatesFeature, DoubleFeature, TextFeature}
import utils.Cons

case class VenueContact(twitter: Option[String], facebook: Option[String], phone: Option[String])

case class VenueStats2(checkinsCount: Option[Int], usersCount: Option[Int], tipCount: Option[Int])

case class VenueLocation(city: Option[String], cc: Option[String], state: Option[String],
                          lat: Option[Double], lng: Option[Double])

case class VenueCategory2(id: Option[String], name: Option[String], pluralName: Option[String], shortName: Option[String],
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

case class VenueCompact2(categories: Option[List[VenueCategory2]], id: Option[String], name: Option[String], url: Option[String], stats: Option[VenueStats2],
                        price: Option[VenuePrice], likes: Option[Int], rating: Option[Int], reasons: Option[VenueReasons],
                        mayor: Option[VenueMayor], tags: Option[List[String]], contact: Option[VenueContact], attributes: Option[VenueAttributes],
                        hours: Option[VenueHours], verified: Option[Boolean], photos: Option[VenuePhotos], tips: Option[VenueTips],
                        phrases: Option[List[VenuePhrases]], location: Option[VenueLocation])

case class InteractionCompact(cnt:Option[Int], items: Option[List[InteractionItem]])
case class InteractionItem(text : Option[String], venue : Option[VenueCompact2])


abstract class Interactions(js:String) {
	val jsonString = js

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

  implicit val category2Read: Reads[VenueCategory2] = (
    (__ \ "id").readNullable[String] and
      (__ \ "name").readNullable[String] and
      (__ \ "pluralName").readNullable[String] and
      (__ \ "shortName").readNullable[String] and
      (__ \ "primary").readNullable[Boolean]
    )(VenueCategory2.apply _)

  implicit val venueStat2Read: Reads[VenueStats2] = (
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

  implicit val venueCompact2Read: Reads[VenueCompact2] = (
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

  val compact:InteractionCompact = null
}

object Interactions {
  /**
   * Create venue feature vector from the parsed [[Venue]] object
   * @param v parsed venue
   * @return venue feature vector
   */
  def featureVector(v: VenueCompact2):VenueVector = {
    val (checkinsCount, tipCount ,userCount) = v.stats match {
      case Some(x:VenueStats2) => (x.checkinsCount.getOrElse(0), x.tipCount.getOrElse(0), x.usersCount.getOrElse(0))
      case None => (0, 0, 0)
    }
    val (lat, lng):(Double, Double) = v.location match {
      case None => (0, 0)
      case Some(x:VenueLocation) => (x.lat.getOrElse(0), x.lng.getOrElse(0))
    }
    val cats = v.categories match {
      case None => List.empty
      case Some(x:Seq[VenueCategory2]) => x.map(_.name.get)
    }
    val features = List(
      TextFeature(Cons.VENUE_ID, v.id.get),
      DoubleFeature(Cons.POPULARITY, Venue.compute_popularity(checkinsCount, tipCount, userCount)),
      CoordinatesFeature(Cons.GPS_COORDINATES, (lat, lng)),
      CategoryFeature(Cons.CATEGORY, cats)
    )
    new VenueVector(features, null)
  }
}