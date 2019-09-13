/*                                                                      *\
** Squants                                                              **
**                                                                      **
** (c) 2013-2019, Gary Keorkunian                                       **
\*                                                                      */

//package org.squants
//
//class SquantsNumericSpec {
//
//
//  import org.squants.NumericRules.UseDeclaredType._
//
//  val x: Int = 10
//  val y: Double = 10.22
//
//  val z = addNumerics(y, x)
//
//  z
//
//  def addNumerics[A: SquantsNumeric, B: SquantsNumeric](a: A, b: B): A = {
//    val sqNumA = implicitly[SquantsNumeric[A]]
//    import sqNumA.mkSquantsNumericOps
//    a + b
//  }
//
//}
