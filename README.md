# Squants Generic

This repo is an attempt to model a version of Squants that uses a generic numeric instead of Double.

## Goals

* Create a model that supports generic numerics (Squants 2.x)
  * Refactor Quantity.value to be generically typed
  * Support generic conversion factors
  * Support operations across different types of numerics

* Minimize the impact to user code, but there will be some migration required
  * Generic numerics should be inferred by existing user code
  
```scala
    val p = Kilowatts(12.34) // should result in Power[Double], which is the effective type in Squants 1.x
```
  * Should this to default all numerics to Double for backwards compatability?
```scala
    val p = Kilowatts(12) // should be this be Power[Int] or Power[Double]?
```
        We should be able to define this by which implicits we include in the default squants package object
        User can override by typing for specific numerics other than Double.  That'll leave some warts. 
    
  * However, explicit type declarations will require the addition of the Numeric type
  
```scala
    val p: Power = Load(12.34)
```

must be changed to

```scala
    val p: Power[Double] = Load(12.34)
```

  * User define Quantities will need to be refactored

* Provide squants-spire companion project 
  * SquantsNumeric Type Classes for Spire types
  
 
## Current State

* The based class model has been refactored
  * SquantsNumeric Typeclasses have been implemented
    * supports all numeric operations required by Squants
    * provides support for conversions between numeric types and, therefore, cross type operations
  * Dimension is now the root type of the model - No Longer Quantity
  * UnitOfMeasure is now typed on Dimension
  * Quantity is now typed on Dimension and N: SquantsNumeric
  * Enough of the Quantity code has been implemented to prove out the concept
  
  * The Dimensionless type has been implemented as a first vetting example
    * Most DimensionlessSpec tests pass as is - a promising sign - however, I am finding things some things that will need to be more explicit in user code
    
  
```scala
    // Abbreviated Model

    // Numerics
    trait SquantsNumeric[A] // Defines required numerical operations
    trait IntIsSquantsNumeric extends SquantsNumeric[Int]
    trait LongIsSquantsNumeric extends SquantsNumeric[Long]
    trait FloatIsSquantsNumeric extends SquantsNumeric[Float]
    trait DoubleIsSquantsNumeric extends SquantsNumeric[Double]
    trait BigDecimalIsSquantsNumeric extends SquantsNumeric[BigDecimal]
  
    // Quantity Domain
    trait Dimension 
    trait UnitOfMeasure[D <: Dimension] 
    abstract class Quantity[D <: Dimension, N: SquantsNumeric] {
      def value: N
      def unit: UnitOfMeasure[D]
    } 

    // Example Dimension
    object Mass extends Dimension

    trait MassUnit extends UnitOfMeasure[Mass.type]

    object Kilogram extends MassUnit
    object Gram extends MassUnit

    final class Mass[N: SquantsNumeric](val value: N, val unit: UnitOfMeasure[Mass.type])
      extends Quantity[Mass.type, N]

```

The following is an example of using Units to create Quantities

Creating overloadeds version of the UnitOfMeaure.apply allows for backwards compatibility with 1.x and
explicit numeric typing when a user is ready to use it.

```scala
import org.squants._

// The following code is 1.x compatible and generates the same result - a quantity with a Double value
val nd = Each(10)        // DimensionlessGen[Double] = 10.0 ea

val ld = Each(10L)       // DimensionlessGen[Double] = 10.0 ea

val fd = Each(10.22f)    // DimensionlessGen[Double] = 10.220000267028809 ea

val dd = Each(10.22)     // DimensionlessGen[Double] = 10.22 ea

val ddd = Each(10.22d)    // DimensionlessGen[Double] = 10.22 ea

// This statement is allowed in 1.x, but the BigDecimal is actually converted to a Double
// In 2.x the BigDecimal is preserved.  
// If a user did this, I suspect this would be a desired in change behavior
val bd = Each(BigDecimal(10.22)) // DimensionlessGen[scala.math.BigDecimal] = 10.22 ea

// This is new for 2.x.  Specifying a type argument results in a quantity with a value of that type
val n = Each[Int](10)   // DimensionlessGen[Int] = 10 ea

val l = Each[Long](10)  // DimensionlessGen[Long] = 10 ea

val f = Each[Float](10.22f) // DimensionlessGen[Float] = 10.22 ea

val d = Each[Double](10.22) // DimensionlessGen[Double] = 10.22 ea   

val a = Each[BigDecimal](10.22)  // DimensionlessGen[BigDecimal] = 10.22 ea

// Of course, the argument must match the type
 
// This code is not backward compitable, because it assumed parsing a Double
val s1 = Dimensionless("10.22")

// In 2.x string parsers must know what type of numeric they are attempting to parse
val s2 = Dimensionless[Double]("10.22")

```

Where explicit Quantity types will be used, you can use *Quantity*Gen (e.g DimensionlessGen) directly,
or import type aliases.

Importing squants.SquantsDouble._ provides type aliases that provide backward compatibility with 1.x

```scala
import org.squants.SquantsDouble._

def d: Dimensionless // DimensionlessGen[Double]

```

Importing squants.SquantsGeneric._ provides type aliases that are generic and require a type parameter

```scala
import org.squants.SquantsGeneric._

def bd: Dimensionless[BigDecimal]
def n: Dimensionless[Int]

```

Additional imports can also be created (e.g. SquantsBigDecimal, SquantsSpireRational, etc.).
And, of course, users can create their own to identify which numeric they want for each dimension.


  
## Next Steps

* Implement Time and Frequency
  * This help define any new patterns required for dimensional conversions
  
* Implement QuantityRange and SVector

* Add range, vector and approximation capabilities to Quantity class

* Implement a handful of other Dimensions and their relationships

Once the model is fully vetted ...

* Create a dev branch on typelevel/squants and migrate code

* Complete conversion of all Dimensions

* Update README and Scala docs

* Create squants-spire project

* Find beta users
  * Code-Test-Fix
  
* Release
    * Separate 1.x release branch to provide maintenance for ??? months
    
    



  
  
