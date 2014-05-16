package sets

import vectors.AbstractVector

/**
 * All classes representing the set of vectors should implement this
 * @author Ivan Gavrilović
 */
trait AbstractVectorSet {
  type T <: AbstractVector

  /**
   * Get all of the vectors associated to this set
   * @return all vectors
   */
  def getVectors(): Seq[T]

  /**
   * Get size of this vector set
   * @return size of set
   */
  def size(): Int
}
