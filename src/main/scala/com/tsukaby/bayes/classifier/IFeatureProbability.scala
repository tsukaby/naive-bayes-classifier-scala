package com.tsukaby.bayes.classifier

/**
 * Simple interface defining the method to calculate the feature probability.
 */
trait IFeatureProbability[T, K] {
  def featureProbability(feature: T, category: K): Float
}
