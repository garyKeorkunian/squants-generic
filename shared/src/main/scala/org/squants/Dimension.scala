/*                                                                      *\
** Squants                                                              **
**                                                                      **
** (c) 2013-2019, Gary Keorkunian                                       **
\*                                                                      */

package org.squants

import scala.util.{ Failure, Success, Try }

import scala.language.higherKinds

trait Dimension {

  // The numerically generic type for all Quantities of an implemented Dimension
  type Q[N] <: Quantity[this.type, N]

  /**
   * The name
   *
   * @return
   */
  def name: String

  /**
   * Set of available units
   *
   * @return
   */
  def units: Set[UnitOfMeasure[this.type]]

  /**
   * The unit with a conversions factor of 1.
   * The conversionFactor for other units should be set relative to this unit.
   *
   * @return
   */
  def primaryUnit: UnitOfMeasure[this.type]

  /**
   * The International System of Units (SI) Base Unit
   *
   * @return
   */
  def siUnit: UnitOfMeasure[this.type] with SiUnit

  /**
   * Maps a string representation of a unit symbol into the matching UnitOfMeasure object
   *
   * @param symbol String
   * @return
   */
  def symbolToUnit(symbol: String): Option[UnitOfMeasure[this.type]] = units.find(u ⇒ u.symbol == symbol)

  /**
   * Tries to map a string or tuple value to Quantity of this Dimension
   *
   * @param value the source string (ie, "10 kW") or tuple (ie, (10, "kW"))
   * @return Try[Q[N]
   */
  protected def parse[N: SquantsNumeric](value: String): Try[Q[N]] = parseString(value)
  protected def parse[N: SquantsNumeric](value: (N, String)): Try[Q[N]] = parseTuple(value._1, value._2)

  private def parseString[N: SquantsNumeric](s: String): Try[Q[N]] = {
    val sqNum = implicitly[SquantsNumeric[N]]
    s match {
      case QuantityString(value, symbol) ⇒
        sqNum.fromString(value) match {
          case Success(value) ⇒ Success(symbolToUnit(symbol).get(value))
          case Failure(error) ⇒ Failure(QuantityParseException(s"Unable to parse numeric", value, Some(error)))
        }
      case x ⇒ Failure(QuantityParseException(s"Unable to parse $name", s))
    }
  }

  private lazy val QuantityString = ("([\\S]+) *(" + units.map { u: UnitOfMeasure[this.type] ⇒ u.symbol }.reduceLeft(_ + "|" + _) + ")$").r

  private def parseTuple[N: SquantsNumeric](value: N, symbol: String): Try[Q[N]] = {
    symbolToUnit(symbol) match {
      case Some(unit) ⇒ Success(unit(value))
      case None       ⇒ Failure(QuantityParseException(s"Unable to identify $name unit $symbol", (value, symbol).toString()))
    }
  }
}

case class QuantityParseException(message: String, expression: String, cause: Option[Throwable] = None) extends Exception

/**
 * SI Base Quantity
 */
trait BaseDimension {
  self: Dimension ⇒
  /**
   * SI Base Unit for this Quantity
   *
   * @return
   */
  def siUnit: SiBaseUnit

  /**
   * SI Dimension Symbol
   *
   * @return
   */
  def dimensionSymbol: String
}
