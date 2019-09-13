package org.squants

import scala.language.implicitConversions
import scala.util.Try

trait SquantsNumeric[N] extends Ordering[N] {

  def plus(a: N, b: N): N
  def minus(a: N, b: N): N
  def times(a: N, b: N): N
  def divide(a: N, b: N): N
  def remainder(a: N, b: N): N
  def divideAndRemainder(a: N, b: N): (N, N) = (divide(a, b), remainder(a, b))

  def negate(a: N): N
  def abs(a: N): N

  def zero: N
  def one: N

  def toInt(a: N): Int
  def toLong(a: N): Long
  def toFloat(a: N): Float
  def toDouble(a: N): Double

  def fromDouble(a: Double): N
  def fromString(s: String): Try[N]
  def fromSquantsNumeric[A: SquantsNumeric](a: A): N

  class Ops(lhs: N) {
    def +[R: SquantsNumeric](rhs: R): N = plus(lhs, fromSquantsNumeric(rhs))
    def -[R: SquantsNumeric](rhs: R): N = minus(lhs, fromSquantsNumeric(rhs))
    def *[R: SquantsNumeric](rhs: R): N = times(lhs, fromSquantsNumeric(rhs))
    def /[R: SquantsNumeric](rhs: R): N = divide(lhs, fromSquantsNumeric(rhs))
    def %[R: SquantsNumeric](rhs: R): N = remainder(lhs, fromSquantsNumeric(rhs))
    def /%[R: SquantsNumeric](rhs: R): (N, N) = divideAndRemainder(lhs, fromSquantsNumeric(rhs))
    def unary_-(): N = negate(lhs)
    def >[R: SquantsNumeric](rhs: R): Boolean = gt(lhs, fromSquantsNumeric(rhs))
    def >=[R: SquantsNumeric](rhs: R): Boolean = gteq(lhs, fromSquantsNumeric(rhs))
    def <[R: SquantsNumeric](rhs: R): Boolean = lt(lhs, fromSquantsNumeric(rhs))
    def <=[R: SquantsNumeric](rhs: R): Boolean = lteq(lhs, fromSquantsNumeric(rhs))
  }
  implicit def mkSquantsNumericOps(lhs: N): Ops = new Ops(lhs)
}

object SquantsNumeric {

  trait IntIsSquantsNumeric extends SquantsNumeric[Int] with Ordering.IntOrdering {
    def plus(a: Int, b: Int): Int = a + b
    def minus(a: Int, b: Int): Int = a - b
    def times(a: Int, b: Int): Int = a * b
    def divide(a: Int, b: Int): Int = a / b
    def remainder(a: Int, b: Int): Int = a % b
    def negate(a: Int): Int = -a
    def abs(a: Int): Int = math.abs(a)
    def zero = 0
    def one = 1
    def toInt(a: Int): Int = a
    def toLong(a: Int): Long = a.toLong
    def toFloat(a: Int): Float = a.toFloat
    def toDouble(a: Int): Double = a.toDouble

    def fromDouble(a: Double): Int = a.toInt
    def fromString(s: String): Try[Int] = Try {s.toInt}
    override def fromSquantsNumeric[A](a: A)(implicit squantsNumeric: SquantsNumeric[A]): Int = squantsNumeric.toInt(a)
  }

  trait LongIsSquantsNumeric extends SquantsNumeric[Long] with Ordering.LongOrdering {
    def plus(a: Long, b: Long): Long = a + b
    def minus(a: Long, b: Long): Long = a - b
    def times(a: Long, b: Long): Long = a * b
    def divide(a: Long, b: Long): Long = a / b
    def remainder(a: Long, b: Long): Long = a % b
    def negate(a: Long): Long = -a
    def abs(a: Long): Long = math.abs(a)
    def zero = 0L
    def one = 1L
    def toInt(a: Long): Int = a.toInt
    def toLong(a: Long): Long = a
    def toFloat(a: Long): Float = a.toFloat
    def toDouble(a: Long): Double = a.toDouble

    def fromDouble(a: Double): Long = a.toLong
    def fromString(s: String): Try[Long] = Try {s.toLong}
    override def fromSquantsNumeric[A](a: A)(implicit squantsNumeric: SquantsNumeric[A]): Long = squantsNumeric.toLong(a)
  }

  trait FloatIsSquantsNumeric extends SquantsNumeric[Float] with Ordering.FloatOrdering {
    def plus(a: Float, b: Float): Float = a + b
    def minus(a: Float, b: Float): Float = a - b
    def times(a: Float, b: Float): Float = a * b
    def divide(a: Float, b: Float): Float = a / b
    def remainder(a: Float, b: Float): Float = a % b
    def negate(a: Float): Float = -a
    def abs(a: Float): Float = math.abs(a)
    def zero = 0F
    def one = 1F
    def toInt(a: Float): Int = a.toInt
    def toLong(a: Float): Long = a.toLong
    def toFloat(a: Float): Float = a
    def toDouble(a: Float): Double = a.toDouble

    def fromDouble(a: Double): Float = a.toFloat
    def fromString(s: String): Try[Float] = Try {s.toFloat}
    override def fromSquantsNumeric[A](a: A)(implicit squantsNumeric: SquantsNumeric[A]): Float = squantsNumeric.toFloat(a)
  }

  trait DoubleIsSquantsNumeric extends SquantsNumeric[Double] with Ordering.DoubleOrdering {
    def plus(a: Double, b: Double): Double = a + b
    def minus(a: Double, b: Double): Double = a - b
    def times(a: Double, b: Double): Double = a * b
    def divide(a: Double, b: Double): Double = a / b
    def remainder(a: Double, b: Double): Double = a % b
    def negate(a: Double): Double = -a
    def abs(a: Double): Double = math.abs(a)
    def zero = 0D
    def one = 1D
    def toInt(a: Double): Int = a.toInt
    def toLong(a: Double): Long = a.toLong
    def toFloat(a: Double): Float = a.toFloat
    def toDouble(a: Double): Double = a

    def fromDouble(a: Double): Double = a
    def fromString(s: String): Try[Double] = Try {s.toDouble}
    override def fromSquantsNumeric[A](a: A)(implicit squantsNumeric: SquantsNumeric[A]): Double = squantsNumeric.toDouble(a)

  }

  trait BigDecimalIsSquantsNumeric extends SquantsNumeric[BigDecimal] with Ordering.BigDecimalOrdering {
    def plus(a: BigDecimal, b: BigDecimal): BigDecimal = a + b
    def minus(a: BigDecimal, b: BigDecimal): BigDecimal = a - b
    def times(a: BigDecimal, b: BigDecimal): BigDecimal = a * b
    def divide(a: BigDecimal, b: BigDecimal): BigDecimal = a / b
    def remainder(a: BigDecimal, b: BigDecimal): BigDecimal = a % b
    def negate(a: BigDecimal): BigDecimal = -a
    def abs(a: BigDecimal): BigDecimal = a.abs
    def zero = BigDecimal(0)
    def one = BigDecimal(1)
    def fromDouble(a: Double): BigDecimal = BigDecimal(a)
    def toInt(a: BigDecimal): Int = a.toInt
    def toLong(a: BigDecimal): Long = a.toLong
    def toFloat(a: BigDecimal): Float = a.toFloat
    def toDouble(a: BigDecimal): Double = a.toDouble

    def fromString(s: String): Try[BigDecimal] = Try { BigDecimal(s)}
    override def fromSquantsNumeric[A](a: A)(implicit squantsNumeric: SquantsNumeric[A]): BigDecimal = a match {
      case a: BigDecimal => a
      case _ => BigDecimal(squantsNumeric.toDouble(a))
    }
  }
}

