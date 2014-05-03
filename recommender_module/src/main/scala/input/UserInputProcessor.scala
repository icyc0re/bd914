package input

import scala.io.BufferedSource
import vectors.{VenueVector, UserVector}
import java.io.File
import utils.Cons

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
  override def processData(input: BufferedSource): T = {
    val vector = User.featureVector(new User(input.mkString))
    val userId = vector.getFeatureValue[String](Cons.USER_ID).get

    // get all of the interactions
    // IteractionInputProcessor: tips, photos, mayorships, checkins venuesTips ++ venuesPhotos =
    var venues = List.empty[VenueVector]
    InteractionType.values.foreach{(intType:InteractionType.InteractionType) =>
      try{
        venues ++= InteractionsInputProcessor.processData(scala.io.Source.fromFile(new File(Cons.INTERACTIONS_PATH + intType + "/" + userId)), InteractionType.tips)
      }
      catch {
        case e:Exception => //
      }
    }

    vector.applyVenues(venues)
  }
}

object InteractionsInputProcessor {
  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @param interactionType type of interaction
   * @return list of venue feature vector
   */
  def processData(input: BufferedSource, interactionType: InteractionType.InteractionType): Seq[VenueVector] = {
    val interaction = interactionType match {
      case InteractionType.`tips` => new UserTips(input.mkString)
      case InteractionType.`mayorships` => new UserMayorships(input.mkString)
      case InteractionType.`photos` => new UserPhotos(input.mkString)
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

object CheckinInputProcessor {
  /**
   * Parse input file and create collection of vectors from the file
   * @param input input source to read
   * @return list of venue feature vector
   */
  def processData(input: BufferedSource): Seq[VenueVector] = {
    val venues = new Checkins(input.mkString).compact.items match {
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