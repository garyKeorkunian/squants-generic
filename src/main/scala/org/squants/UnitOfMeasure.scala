package org.squants

trait UnitOfMeasure[D <: Dimension] extends Serializable {

  def symbol: String

  def apply[N: SquantsNumeric](n: N): Quantity[D, N]

  protected def converterTo[N]: (N, SquantsNumeric[N]) => N
  protected def converterFrom[N]: (N, SquantsNumeric[N]) => N

  /**
   * Converts a numeric to this UnitOfMeasure from the PrimaryUnit
   *
   * @param n The numeric value to be converted
   * @tparam N The type of numeric - must have SquantsNumeric[N] in scope
   * @return The converted value
   */
  final def convertTo[N: SquantsNumeric](n: N): N = converterTo(n, implicitly[SquantsNumeric[N]])

  /**
   * Converts a numeric from this UnitOfMeasure to the PrimaryUnit
   *
   * @param n The numeric value to be converted
   * @tparam N The type of numeric - must have SquantsNumeric[N] in scope
   * @return The converted value
   */
  final def convertFrom[N: SquantsNumeric](n: N): N = converterFrom(n, implicitly[SquantsNumeric[N]])

}

/**
 * A Simple Converter that uses a single conversion factor.
 *
 * Works well for any conversions between units that share a zero.
 * Does not work for scale based units with different zeros, e.g. Temperature
 *
 * The ConversionNumeric can be any type with a SquantsNumeric[ConversionNumeric] in scope
 *
 * At conversion time, the conversionFactor will be mapped to a value matching the type of
 * the value being converted.
 *
 */
trait SimpleConverter { self: UnitOfMeasure[_] =>

  type ConversionNumeric
  protected implicit def conversionNumeric: SquantsNumeric[ConversionNumeric]
  def conversionFactor: ConversionNumeric

  protected def converterTo[N]: (N, SquantsNumeric[N]) => N = { (n, sqNum) =>
    sqNum.divide(n, sqNum.fromSquantsNumeric(conversionFactor))
  }

  protected def converterFrom[N]: (N, SquantsNumeric[N]) => N = { (n, sqNum) =>
    sqNum.times(n, sqNum.fromSquantsNumeric(conversionFactor))
  }

}

/**
 * Identifies the PrimaryUnit from which all other Unit's conversionFactors a relative.
 *
 * The conversionFactor is finalized at 1 and the converter methods are overridden as identify-like functions
 */
trait PrimaryUnit extends SimpleConverter { self: UnitOfMeasure[_] =>
  override final val conversionFactor: ConversionNumeric = conversionNumeric.one
  protected override final def converterTo[N]: (N, SquantsNumeric[N]) => N = { (n, _) => n }
  protected override final def converterFrom[N]: (N, SquantsNumeric[N]) => N = { (n, _) => n }
}

// Market Traits
trait SiUnit { self: UnitOfMeasure[_] => }
trait SiBaseUnit { self: UnitOfMeasure[_] => }

