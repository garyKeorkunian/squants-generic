package org.squants

import org.squants.NumericRules.UseDouble.DoubleIsSquantsNumeric

final class Dimensionless[N: SquantsNumeric] private (val value: N, val unit: UnitOfMeasure[Dimensionless.type])
 extends Quantity[Dimensionless.type, N] {

  private val sqNum = implicitly[SquantsNumeric[N]]
  import sqNum.mkSquantsNumericOps

  def *[T: SquantsNumeric](that: Dimensionless[T]) = Each(toEach * sqNum.fromSquantsNumeric(that.toEach)).in(unit)
  def *[T: SquantsNumeric](that: Quantity[_, T]): Quantity[_, T] = that * toEach

  def toPercent: N = to(Percent)
  def toEach: N = to(Each)
  def toDozen: N = to(Dozen)
  def toScore: N = to(Score)
  def toGross: N = to(Gross)
}

object Dimensionless extends Dimension {
  val name: String = "Dimensionless"
  val primaryUnit: UnitOfMeasure[Dimensionless.this.type] = Each
  val siUnit: UnitOfMeasure[Dimensionless.this.type] with SiUnit = Each
  val units: Set[UnitOfMeasure[Dimensionless.this.type]] = Set(Each, Dozen)

  def apply[N: SquantsNumeric](n: N, unit: DimensionlessUnit) = new Dimensionless(n, unit)
  def apply[N: SquantsNumeric](s: String) = parse[N](s)
  def apply[N: SquantsNumeric](t: Tuple2[N, String]) = parse[N](t)
}

trait DimensionlessUnit extends UnitOfMeasure[Dimensionless.type] with SimpleConverter {
  override type ConversionNumeric = Double
  override protected implicit val conversionNumeric: SquantsNumeric[ConversionNumeric] = DoubleIsSquantsNumeric

  def apply[N: SquantsNumeric](n: N) = Dimensionless(n, this)
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


