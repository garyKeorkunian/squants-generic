# Squants Generic

This repo is an attempt to model a version of Squants that users a generic numeric instead of Double.

## Goals

* Create a model that supports generic numerics

* Minimize the impact to user code
  * Generic numerics must be able to be inferred
  * Treat existing code as Double numerics where possible
  * Explicit type declarations will require the addition of the Numeric type

* Provide squants-spire companion project 
  * Type classes for [SpireType]isSquantsNumeric
  
 
## Current State

* The based class model has been refactored
  * SquantsNumeric Type classes have been implemented
  * Dimension is now the root type
  * UnitOfMeasure is now typed on Dimension
  * Quantity is now typed on Dimension and N: SquantsNumeric

* Enough of the Quantity code has been implemented to prove out the concept

* The Dimensionless type has been implemented as a first vetting example
  * DimensionlessSpec passes as suggesting user code that just works with existing quantities will work as-is
  
## Next Steps

* Implement Time and Frequency
  * This help define any new patterns required for dimensional conversions
  
* Implement QuantityRange and SVector

* Add range and vector capabilities to Quantity class

* Implement a handful of other Dimensions

Once the model is fully vetted ...

* Create a dev branch on typelevel/squants and migrate code

* Complete conversion of all Dimensions

* Update README and other docs

* Create squants-spire project

* Find beta users
  * Code-Test-Fix
  
* Release



  
  
