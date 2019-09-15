/*                                                                      *\
** Squants                                                              **
**                                                                      **
** (c) 2013-2019, Gary Keorkunian                                       **
\*                                                                      */

package org

import org.squants.SquantsNumeric._
import scala.language.implicitConversions

package object squants {

  implicit object DoubleIsSquantsNumeric extends DoubleIsSquantsNumeric
  implicit def NumericToDouble[A: Numeric](a: A): Double = implicitly[Numeric[A]].toDouble(a)

    object AllNumerics {
      implicit object LongIsSquantsNumeric extends LongIsSquantsNumeric
      implicit object FloatIsSquantsNumeric extends FloatIsSquantsNumeric
      implicit object IntIsSquantsNumeric extends IntIsSquantsNumeric
      implicit object BigDecimalIsSquantsNumeric extends BigDecimalIsSquantsNumeric
    }
}
