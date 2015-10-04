package com.tsukaby.bayes.classifier

import scala.collection.{mutable, SortedSet}

/**
 * A concrete implementation of the abstract Classifier class.  The Bayes
 * classifier implements a naive Bayes approach to classifying a given set of
 * features: classify(feat1,...,featN) = argmax(P(cat)*PROD(P(featI|cat)
 *
 * @see http://en.wikipedia.org/wiki/Naive_Bayes_classifier
 *
 */
class BayesClassifier[T, K <: AnyRef] extends Classifier[T, K] {
  /**
   * Calculates the product of all feature probabilities: PROD(P(featI|cat)
   *
   * @param features The set of features to use.
   * @param category The category to test for.
   * @return The product of all feature probabilities.
   */
  private def featuresProbabilityProduct(features: Traversable[T], category: K): Float = {
    features.foldLeft(1.0f)((x: Float, y: T) => x * featureWeighedAverage(y, category))
  }

  /**
   * Calculates the probability that the features can be classified as the category given.
   *
   * @param features The set of features to use.
   * @param category The category to test for.
   * @return The probability that the features can be classified as the category.
   */
  private def categoryProbability(features: Traversable[T], category: K): Float = {
    (categoryCount(category).toFloat / categoriesTotal.toFloat) * featuresProbabilityProduct(features, category)
  }


  private val ordering = new Ordering[Classification[T, K]] {
    override def compare(o1: Classification[T, K], o2: Classification[T, K]): Int = {
      var toReturn = o1.probability.compare(o2.probability)
      if ((toReturn == 0) && !(o1.category == o2.category)) toReturn = -1
      toReturn
    }
  }

  /**
   * Retrieves a sorted <code>Set</code> of probabilities that the given set
   * of features is classified as the available categories.
   *
   * @param features The set of features to use.
   * @return A sorted <code>Set</code> of category-probability-entries.
   */
  private def categoryProbabilities(features: Traversable[T]): mutable.SortedSet[Classification[T, K]] = {
    val probabilities: mutable.SortedSet[Classification[T, K]] = mutable.TreeSet.empty[Classification[T, K]](ordering)

    for (category <- categories) {
      probabilities.add(Classification[T, K](features, category, categoryProbability(features, category)))
    }
    probabilities
  }

  /**
   * Classifies the given set of features.
   *
   * @return The category the set of features is classified as.
   */
  override def classify(features: Traversable[T]): Option[Classification[T, K]] = {
    val probabilites: SortedSet[Classification[T, K]] = categoryProbabilities(features)
    probabilites.lastOption
  }

  /**
   * Classifies the given set of features. and return the full details of the
   * classification.
   *
   * @return The set of categories the set of features is classified as.
   */
  def classifyDetailed(features: Traversable[T]): Traversable[Classification[T, K]] = {
    categoryProbabilities(features)
  }
}
