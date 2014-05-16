package input

import vectors.{VenueVector, UserVector}
import utils.{FileSys, Cons}

/**
 * @author Jakub Swiatkoswki
 */
class RawUserInputProcessor extends AbstractInputProcessor {
  type T = (UserVector, Seq[VenueVector])

  /**
   * Parse input file to get UserVector Seq[VenueVector] pairs
   * @param input input source to read
   * @return collection of vectors
   */
  override def processData(input: String): T = {
    val vector = User.featureVector(new User(input))
    val userId = vector.getFeatureValue[String](Cons.USER_ID).get

    // get all of the interactions
    // IteractionInputProcessor: tips, photos, mayorships, checkins venuesTips ++ venuesPhotos =
    var venues = List.empty[VenueVector]
    InteractionType.values.foreach {
      (intType: InteractionType.InteractionType) =>
        try {
          venues ++= InteractionsInputProcessor.processData(FileSys.readFile(Cons.INTERACTIONS_PATH + intType + "/" + userId), InteractionType.tips)
        }
        catch {
          case e: Exception => //
        }
    }

    (vector, venues)
  }
}