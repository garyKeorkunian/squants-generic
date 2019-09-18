/*                                                                      *\
** Squants                                                              **
**                                                                      **
** (c) 2013-2019, Gary Keorkunian                                       **
\*                                                                      */

package org.squants

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Success, Try}

class DimensionlessDoubleSpec extends AnyFlatSpec with Matchers {

  behavior of "Dimensionless and its Units of Measure"

  it should "create values using UOM factories" in {
    Percent(1).toPercent should be(1)
    Each(1).toEach should be(1)
    Dozen(1).toDozen should be(1)
    Score(1).toScore should be(1)
    Gross(1).toGross should be(1)
  }

  // 2.0 - Needed to add [Double] type argument for string constructor
  it should "create values from properly formatted Strings" in {
    Dimensionless[Double]("10.22 %").get should be(Percent(10.22))
    Dimensionless[Double]("10.22 ea").get should be(Each(10.22))
    Dimensionless[Double]("10.22 dz").get should be(Dozen(10.22))
    Dimensionless[Double]("10.22 score").get should be(Score(10.22))
    Dimensionless[Double]("10.22 gr").get should be(Gross(10.22))
    Dimensionless[Double]("10.45 zz").failed.get should be(QuantityParseException("Unable to parse Dimensionless", "10.45 zz"))
    Dimensionless[Double]("zz ea").failed.get should be(QuantityParseException("Unable to parse numeric", "zz"))
  }

    // 2.0 - Extra check
  it should "create a SquantsDouble.Dimensionless when type is declared without supplying Numeric parameter" in {
    import org.squants.SquantsDouble.Dimensionless
    val typedVal: Try[Dimensionless] = Dimensionless("10.22 %")
    typedVal.get should be(Percent(10.22))
  }

  it should "properly convert to all supported Units of Measure" in {
    val x = Gross(1)
    x.toPercent should be(14400)
    x.toEach should be(144)
    x.toDozen should be(12)
    x.toScore should be(144d / 20)
    x.toGross should be(1)
  }

  it should "return properly formatted strings for all supported Units of Measure" in {
    Percent(1).toString(Percent) should be("1.0 %")
    Each(1).toString(Each) should be("1.0 ea")
    Dozen(1).toString(Dozen) should be("1.0 dz")
    Score(1).toString(Score) should be("1.0 score")
    Gross(1).toString(Gross) should be("1.0 gr")
  }

  it should "return another Dimensionless when multiplied by a Dimensionless" in {
    Each(2) * Dozen(1) should be(Dozen(2))
    Dozen(5) * Percent(10) should be(Each(6))
  }

//  it should "return another Dimensionless when added to a Double" in {
//    Each(10) + 10.22 should be(Each(20.22))
//  }
//
//  it should "return a Frequency when divided by Time" in {
//    Each(60) / Seconds(1) should be(Hertz(60))
//  }
//
//  it should " return a Time when divided by Frequency" in {
//    Each(60) / Hertz(60) should be(Seconds(1))
//  }

  behavior of "CountsConversions"

  it should "provide aliases for single unit values" in {
    import DimensionlessConversions._

    percent should be(Percent(1))
    each should be(Each(1))
    dozen should be(Dozen(1))
    score should be(Score(1))
    gross should be(Gross(1))
    hundred should be(Each(1e2))
    thousand should be(Each(1e3))
    million should be(Each(1e6))
  }

  it should "provide implicit conversion from Double" in {
    import DimensionlessConversions._

    val coefficient = 10d
    coefficient.percent should be(Percent(coefficient))
    coefficient.each should be(Each(coefficient))
    coefficient.ea should be(Each(coefficient))
    coefficient.dozen should be(Dozen(coefficient))
    coefficient.dz should be(Dozen(coefficient))
    coefficient.score should be(Score(coefficient))
    coefficient.gross should be(Gross(coefficient))
    coefficient.gr should be(Gross(coefficient))
    coefficient.hundred should be(Each(coefficient * 1e2))
    coefficient.thousand should be(Each(coefficient * 1e3))
    coefficient.million should be(Each(coefficient * 1e6))
  }

  it should "provide an implicit conversion to Double" in {
    import DimensionlessConversions._

    10 + 5.each should be(15d)
    100 - 1.dozen should be(88d)
    100 * 15.0.percent should be(15)  // 2.0 Needed to make that percent a Double
    12000 / 1.dozen should be(1000d)
  }

//  it should "provide Numeric support" in {
//    import DimensionlessConversions.DimensionlessNumeric
//
//    // The `times` operation is allowed for Dimensionless quantities
//    DimensionlessNumeric.times(Each(10), Dozen(3)) should be(Dozen(30))
//  }
}

class DimensionlessGenSpec extends AnyFlatSpec with Matchers {

  behavior of "DimensionlessGenSpec Code"

  it should "create an Int based Quantity" in {
    val x = Each[Int](10)
    x.toEach should be(10)
    x.toEach shouldBe an[Int]
  }

  it should "create a Long based Quantity" in {
    val x = Each[Long](10L)
    x.toEach should be(10L)
    x.toEach shouldBe a[Long]
  }

  it should "create a Float based Quantity" in {
    val x = Each[Float](10.22F)
    x.toEach should be(10.22F)
    x.toEach shouldBe a[Float]
  }

  it should "create a Double based Quantity" in {
    val x = Each(10.22)
    x.toEach should be(10.22)
    x.toEach shouldBe a[Double]
  }

  it should "create a BigDecimal based Quantity as previously" in {
    val x = Each(BigDecimal(10.22))
    x.toEach should be(BigDecimal(10.22))
    x.toEach shouldBe a[BigDecimal]
  }

  it should "create a BigDecimal based Quantity from parsing a String" in {
    val x = Dimensionless[BigDecimal]("10.22 ea")
    x should be(Success(Each(BigDecimal(10.22))))
  }

  it should "create a Quantity from parsing a Tuple" in {
    Dimensionless((BigDecimal(10.22), "ea")) should be(Success(Each(BigDecimal(10.22))))
    Dimensionless((10.22, "ea")) should be(Success(Each(10.22)))
    Dimensionless((10.22F, "ea")) should be(Success(Each(10.22F)))
    Dimensionless((10L, "ea")) should be(Success(Each(10L)))
    Dimensionless((10, "ea")) should be(Success(Each(10)))
  }

  it should "create a SquantsGeneric.Dimensionless when type is declared without supplying Numeric parameter" in {
    import org.squants.SquantsGeneric.Dimensionless
    val typedVal: Try[Dimensionless[BigDecimal]] = Dimensionless("10.22 %")
    typedVal.get should be(Percent(BigDecimal(10.22)))
  }

  it should "add two Quantities that are based on the same numeric type" in {
    val x = Each(10) + Each(10)
    x.toEach should be(20)
  }

  it should "add two Quantities of different numeric types" in {
    val lngPlusInt = Each(10L) + Each(10)
    lngPlusInt.toEach should be(20L)

    val fltPlusInt = Each(10F) + Each(10)
    fltPlusInt.toEach should be(20F)
    val fltPlusLng = Each(10F) + Each(10L)
    fltPlusLng.toEach should be(20F)

    val dblPlusInt = Each(10.22) + Each(10)
    dblPlusInt.toEach should be(20.22)
    val dblPlusLng = Each(10.22) + Each(10L)
    dblPlusLng.toEach should be(20.22)
    val dblPlusFlt = Each(10.22) + Each(10F)
    dblPlusFlt.toEach should be(20.22)

    val bdPlusInt = Each(BigDecimal(10.22)) + Each(10)
    bdPlusInt.toEach should be(BigDecimal(20.22))
    val bdPlusLng = Each(BigDecimal(10.22)) + Each(10L)
    bdPlusLng.toEach should be(BigDecimal(20.22))
    val bdPlusFlt = Each(BigDecimal(10.22)) + Each(10F)
    bdPlusFlt.toEach should be(BigDecimal(20.22))
    val bdPlusDbl = Each(BigDecimal(10.22)) + Each(10.22)
    bdPlusDbl.toEach should be(BigDecimal(20.44))
  }

  it should "scale when multiplied by a value of the same numeric type" in {
    val x = Each(10.22)
    val dx = x * 2d
    dx.toEach should be(20.44)
  }

  it should "scale when multiplied by a value of a different numeric type" in {
    val x = Each(10.22)
    val dx = x * 10L
    dx.toEach should be(102.2)
  }

}