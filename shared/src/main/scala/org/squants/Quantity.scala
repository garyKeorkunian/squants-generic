package org.squants

abstract class Quantity[D <: Dimension, N: SquantsNumeric]
  extends Serializable with Ordered[Quantity[D, N]] {

  type Q[A] = D#Q[A] // A like quantity with a Generic numeric
  type QN = Q[N] // A like quantity with a like numeric

  private val sqNum = implicitly[SquantsNumeric[N]]
  import sqNum.mkSquantsNumericOps

  def value: N
  def unit: UnitOfMeasure[D]

  def map[A: SquantsNumeric](f: N => A): Q[A] = unit.apply[A](f(value))

  def to(otherUnit: UnitOfMeasure[D]): N = otherUnit match {
    case u if u == this.unit => value
    case _ => otherUnit.convertTo(this.unit.convertFrom(value))
  }

  def in(otherUnit: UnitOfMeasure[D]): QN = otherUnit match {
    case u if u == this.unit => this.asInstanceOf[QN]
    case _ => otherUnit(otherUnit.convertTo(this.unit.convertFrom(value)))
  }

  def plus[A: SquantsNumeric](that: Quantity[D, A]): QN = map(_ + that.to(unit))
  def +[A: SquantsNumeric](that: Quantity[D, A]): QN = map(_ + that.to(unit))

  def minus[A: SquantsNumeric](that: Quantity[D, A]): QN = map(_ - that.to(unit))
  def -[A: SquantsNumeric](that: Quantity[D, A]): QN = map(_ - that.to(unit))

  def times[A: SquantsNumeric](that: A): QN = map(_ * that)
  def *[A: SquantsNumeric](that: A): QN = map(_ * that)

  def divide[A: SquantsNumeric](that: A): QN = map(_ / that)
  def /[A: SquantsNumeric](that: A): QN = map(_ / that)

  // TODO Need a way to compare when the numeric types are different, but could be equivalent
  override def compare(that: Quantity[D, N]): Int = sqNum.compare(value, that.to(unit))

  override def equals(that: Any): Boolean = that match {
    case x: Quantity[D, _] => x.to(unit) == value
    case _ => false
  }

  override val hashCode: Int = toString.hashCode

  override def toString: String = toString(unit)

  def toString(u: UnitOfMeasure[D]): String = s"${to(u).toString} ${u.symbol}"

}
