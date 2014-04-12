package context

import vectors.AbstractVector

trait AbstractFilter {

  def apply[Seq[AbstractVector]](): Unit
  
}