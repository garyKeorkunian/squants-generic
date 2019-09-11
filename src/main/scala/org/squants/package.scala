package org

import org.squants.SquantsNumeric._

package object squants {

  object NumericRules {

    object UseDouble {
      implicit object DoubleIsSquantsNumeric extends DoubleIsSquantsNumeric
    }

    object UseDeclaredType {
      implicit object IntIsSquantsNumeric extends IntIsSquantsNumeric
      implicit object LongIsSquantsNumeric extends LongIsSquantsNumeric
      implicit object FloatIsSquantsNumeric extends FloatIsSquantsNumeric
      implicit object DoubleIsSquantsNumeric extends DoubleIsSquantsNumeric
      implicit object BigDecimalIsSquantsNumeric extends BigDecimalIsSquantsNumeric
    }
  }
}
