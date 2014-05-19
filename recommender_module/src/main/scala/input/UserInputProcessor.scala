package input

import vectors.{VenueVector, UserVector}
import utils.{FileSys, Cons}

object InteractionType extends Enumeration {
  type InteractionType = Value
  val checkins, tips, photos, mayorships = Value
}

/**
 * @author Ivan Gavrilović
 */
class UserInputProcessor extends AbstractInputProcessor {
  type T = UserVector

  /**
   * Parse input file and create collection of vectors from the file
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

    vector.applyVenues(venues)
    vector
  }
}

object InteractionsInputProcessor {
  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @param interactionType type of interaction
   * @return list of venue feature vector
   */
  def processData(input: String, interactionType: InteractionType.InteractionType): Seq[VenueVector] = {
    if (interactionType == InteractionType.checkins) {
      val venues = new Checkins(input.mkString).compact.items match {
        case None => List.empty
        case Some(x: List[CheckinItem]) => x.map(y =>
          y.venue match {
            case Some(p: VenueCompact2) => p
          }
        )
      }
      venues.map(x => Interactions.featureVector(x))
    }
    else {
      val interaction = interactionType match {
        case InteractionType.`tips` => new UserTips(input)
        case InteractionType.`mayorships` => new UserMayorships(input)
        case InteractionType.`photos` => new UserPhotos(input)
      }

      val venues = interaction.compact.items match {
        case None => List.empty
        case Some(x: List[InteractionItem]) => x.map(y =>
          y.venue match {
            case Some(p: VenueCompact2) => p
          }
        )
      }
      venues.map(x => Interactions.featureVector(x))
    }
  }
}