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
    trait Dimension // e.g, Mass, Length, Time, etc
    trait UnitOfMeasure[D <: Dimension] // e.g., Kilogram, Meter, Second, etc
    abstract class Quantity[D <: Dimension, N: SquantsNumeric] {
      def value: N
      def unit: UnitOfMeasure[D]
    } // 10 Kilograms, 20 Meters, 30 Seconds
```

* Enough of the Quantity code has been implemented to prove out the concept

* The Dimensionless type has been implemented as a first vetting example
  * DimensionlessSpec passes as is - a promising sign
  
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
    
    



  
  