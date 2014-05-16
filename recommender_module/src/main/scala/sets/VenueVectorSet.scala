package sets

import vectors.VenueVector

/**
 * @author Ivan Gavrilović
 */

class VenueVectorSet(val name: String) extends AbstractVectorSet {
  type T = VenueVector

  var vectors: Seq[T] = Nil

  /**
   * Get size of this vector set
   * @return size of set
   */
  override def size(): Int = if (vectors == Nil) 0 else vectors.size

  /**
   * Get all of the vectors associated to this set
   * @return all vectors
   */
  override def getVectors(): Seq[T] = vectors
}

object VenueVectorSet {
  var sets: Seq[VenueVectorSet] = Nil

  /**
   * Create new venue set and
   * @param name name of the new set
   * @return throws exception if name is already taken, returns newly created venue set
   */
  def createNew(name: String): VenueVectorSet = {
    sets.find((x: VenueVectorSet) => x.name == name) match {
      case t: Some[_] => throw new Exception("Choose unique name!")
      case _ => {
        val newSet = new VenueVectorSet(name)
        sets = sets :+ newSet
        newSet
      }
    }
  }
}
