package com.tsukaby.bayes.classifier

/**
 * A basic wrapper reflecting a classification.  It will store both features and resulting classification.
 *
 */
case class Classification[T, K <: AnyRef](
  features: Traversable[T],
  category: K,
  probability: Float = 0.0f
)
