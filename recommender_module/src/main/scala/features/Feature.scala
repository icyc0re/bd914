package features

import input.Gender.Gender
import input.VenueCategory
import input.VenuePrice

/**
 * Representing the feature (attribute, property). Extend this class in order to create specific features.
 * @author Ivan Gavrilović
 */



case class DoubleFeature(k:String, v:Double) extends Feature[String, Double](k, v) with NumericFeature
case class IntFeature(k:String, v:Int) extends Feature[String, Int](k, v) with NumericFeature
case class TextFeature(k:String, v:String) extends Feature[String, String](k, v)
case class BooleanFeature(k:String, v:Boolean) extends Feature[String, Boolean](k, v)
case class GenderFeature(k:String, v:Gender) extends Feature[String, Gender](k, v)
case class CategoryFeature(k:String, v:List[VenueCategory]) extends Feature[String, List[VenueCategory]](k, v)
case class PriceFeature(k:String, v:VenuePrice) extends Feature[String, VenuePrice](k, v)
case class CoordinatesFeature(k:String, v:(Double, Double)) extends Feature[String, (Double, Double)](k, v)

abstract class Feature[+K, +V](k:K, v:V){
  val key:K = k
  val value:V = v
  override def toString: String = "Feature: " + key.toString + " = " + value.toString
}

trait NumericFeature