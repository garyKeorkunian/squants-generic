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

  // TODO - This operator is conflicting with Quantity.+(Quantity), despite the different types
//  def +[A: SquantsNumeric](that: A): Dimensionless[N] = Each(toEach + that).in(unit)

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
  val units: Set[U] = Set(Each, Percent, Dozen, Score, Gross)

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

object DimensionlessConversions {
  import AllNumerics._

  lazy val percent: Dimensionless[Int] = Percent(1)
  lazy val each: Dimensionless[Int] = Each(1)
  lazy val dozen: Dimensionless[Int] = Dozen(1)
  lazy val score: Dimensionless[Int] = Score(1)
  lazy val gross: Dimensionless[Int] = Gross(1)
  lazy val hundred: Dimensionless[Long] = Each(100L)
  lazy val thousand: Dimensionless[Long] = Each(1000L)
  lazy val million: Dimensionless[Long] = Each(1000000L)

  implicit class DimensionlessConversions[N](n: N)(implicit sqNum: SquantsNumeric[N]) {
    import sqNum.mkSquantsNumericOps
    def percent: Dimensionless[N] = Percent(n)
    def each: Dimensionless[N] = Each(n)
    def ea: Dimensionless[N] = Each(n)
    def dozen: Dimensionless[N] = Dozen(n)
    def dz: Dimensionless[N] = Dozen(n)
    def score: Dimensionless[N] = Score(n)
    def gross: Dimensionless[N] = Gross(n)
    def gr: Dimensionless[N] = Gross(n)
    def hundred: Dimensionless[N] = Each(n * 100L)
    def thousand: Dimensionless[N] = Each(n * 1000L)
    def million: Dimensionless[N] = Each(n * 1000000L)
  }


  // TODO - Restore this functionality

//  /**
//   * Provides an implicit conversion from Dimensionless to Double, allowing a Dimensionless value
//   * to be used anywhere a Double (or similar primitive) is required
//   *
//   * @param d Dimensionless
//   * @return
//   */
//    import scala.language.implicitConversions
//    implicit def dimensionlessToDouble(d: Dimensionless): Double = d.toEach
//
//  implicit object DimensionlessNumeric extends AbstractQuantityNumeric[Dimensionless](Dimensionless.primaryUnit) {
//    /**
//     * Dimensionless quantities support the times operation.
//     * This method overrides the default [[squants.AbstractQuantityNumeric.times]] which throws an exception
//     *
//     * @param x Dimensionless
//     * @param y Dimensionless
//     * @return
//     */
//    override def times(x: Dimensionless, y: Dimensionless) = x * y
//  }
}

