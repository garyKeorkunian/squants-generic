/*                                                                      *\
** Squants                                                              **
**                                                                      **
** (c) 2013-2019, Gary Keorkunian                                       **
\*                                                                      */

package org.squants

final class Dimensionless[N: SquantsNumeric] private (val value: N, val unit: UnitOfMeasure[Dimensionless.type])
  extends Quantity[Dimensionless.type, N] {

  import sqNum.mkSquantsNumericOps

  def *[A: SquantsNumeric](that: Dimensionless[A]): Dimensionless[N] = map(_ * that.toEach)

  // this is an exception to the general rule in that the numeric type of the right side is kept
  // this exception exists because it is consistent with the right side dimension/unit being kept as well.
  def *[D <: Dimension, A: SquantsNumeric](that: D#Q[A]): D#Q[A] = that * toEach

  def toPercent: N = to(Percent)
  def toEach: N = to(Each)
  def toDozen: N = to(Dozen)
  def toScore: N = to(Score)
  def toGross: N = to(Gross)
}

object Dimensionless extends Dimension {
  override type U = DimensionlessUnit
  override type Q[N] = Dimensionless[N]

  val name: String = "Dimensionless"
  val primaryUnit: U = Each
  val siUnit: U with SiUnit = Each
  val units: Set[U] = Set(Each, Dozen, Score, Gross)

  def apply[N: SquantsNumeric](n: N, unit: DimensionlessUnit) = new Dimensionless(n, unit)
}

trait DimensionlessUnit extends UnitOfMeasure[Dimensionless.type] with SimpleConverter {
  override def apply[N: SquantsNumeric](value: N): Dimensionless[N] = Dimensionless(value, this)
}

object Each extends DimensionlessUnit with PrimaryUnit with SiUnit {
  val symbol = "ea"
}

object Percent extends DimensionlessUnit {
  val conversionFactor = 1e-2
  val symbol = "%"
}

object Dozen extends DimensionlessUnit {
  val conversionFactor = 12D
  val symbol = "dz"
}

object Score extends DimensionlessUnit {
  val conversionFactor = 20D
  val symbol = "score"
}

object Gross extends DimensionlessUnit {
  val conversionFactor = 144D
  val symbol = "gr"
}

