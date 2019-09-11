package org.squants

abstract class Quantity[D <: Dimension, N: SquantsNumeric] extends Serializable with Ordered[Quantity[D, N]] {

  private val sqNum = implicitly[SquantsNumeric[N]]
  import sqNum.mkSquantsNumericOps

  type Q = this.type

  def value: N
  def unit: UnitOfMeasure[D]

  def to(otherUnit: UnitOfMeasure[D]): N = otherUnit match {
    case u if u == this.unit => value
    case _ => otherUnit.convertTo(this.unit.convertFrom(value))
  }

  def in(otherUnit: UnitOfMeasure[D]): Q = otherUnit match {
    case u if u == this.unit => this
    case _ => otherUnit[N](otherUnit.convertTo(this.unit.convertFrom(value)))
  }

  def plus[T: SquantsNumeric](that: Quantity[D, T]): Q =
    unit(value + sqNum.fromSquantsNumeric(that.to(unit)))
  def +[T: SquantsNumeric](that: Quantity[D, T]): Q = plus(that)

  def minus[T: SquantsNumeric](that: Quantity[D, T]): Q =
    unit(value - sqNum.fromSquantsNumeric(that.to(unit)))
  def -[T: SquantsNumeric](that: Quantity[D, T]): Q = minus(that)

  def times[T: SquantsNumeric](that: T): Q =
    unit(value * sqNum.fromSquantsNumeric(that))
  def *[T: SquantsNumeric](that: T): Q = times(that)

  def divide[T: SquantsNumeric](that: T): Q =
    unit(value / sqNum.fromSquantsNumeric(that))
  def /[T: SquantsNumeric](that: T): Q = divide(that)

  // TODO Need a way to compare when the numeric types are different, but could be equivalent
  def compare(that: Q): Int = sqNum.compare(value, that.to(unit))

  override def equals(that: Any): Boolean = that match {
    case x: Q => x.to(unit) == value
    case _ => false // TODO Need a way to compare when the numeric types are different, but could be equivalent
  }

  override val hashCode: Int = toString.hashCode

  override def toString: String = toString(unit)

  def toString(u: UnitOfMeasure[D]): String = s"${to(u).toString} ${u.symbol}"

  def map[T: SquantsNumeric](f: N => T): Quantity[D, T] = unit(f(value))

}
