package com.tsukaby.bayes.classifier

/**
 * A basic wrapper reflecting a classification.  It will store both features and resulting classification.
 *
 */
case class Classification[T, K <: AnyRef](
  /**
   * The classified features.
   */
  features: Traversable[T],

  /**
   * The category as which the features was classified.
   */
  category: K,

  /**
   * The probability that the features belongs to the given category.
   */
  probability: Float = 0.0f

)
