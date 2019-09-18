/*                                                                      *\
** Squants                                                              **
**                                                                      **
** (c) 2013-2019, Gary Keorkunian                                       **
\*                                                                      */

package org

import org.squants.SquantsNumeric._

package object squants {

  implicit object DoubleIsSquantsNumeric extends DoubleIsSquantsNumeric
  implicit object LongIsSquantsNumeric extends LongIsSquantsNumeric
  implicit object FloatIsSquantsNumeric extends FloatIsSquantsNumeric
  implicit object IntIsSquantsNumeric extends IntIsSquantsNumeric
  implicit object BigDecimalIsSquantsNumeric extends BigDecimalIsSquantsNumeric

  val Dimensionless = DimensionlessGen

  object SquantsDouble {
    type Dimensionless = DimensionlessGen[Double]
  }

  object SquantsGeneric {

    type Dimensionless[N] = DimensionlessGen[N]
  }
}
