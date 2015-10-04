package com.tsukaby.bayes.classifier

import scala.collection.mutable

/**
 * Abstract base extended by any concrete classifier.  It implements the basic
 * functionality for storing categories or features and can be used to calculate
 * basic probabilities – both category and feature probabilities. The classify
 * function has to be implemented by the concrete classifier class.
 *
 */
abstract class Classifier[T, K <: AnyRef] extends IFeatureProbability[T, K] {
  /**
   * The initial memory capacity or how many classifications are memorized.
   */
  private var memoryCapacity: Int = 1000
  /**
   * A dictionary mapping features to their number of occurrences in each
   * known category.
   */
  private val featureCountPerCategory: mutable.Map[K, mutable.Map[T, Int]] = mutable.Map()
  /**
   * A dictionary mapping features to their number of occurrences.
   */
  private val totalFeatureCount: mutable.Map[T, Int] = mutable.Map()
  /**
   * A dictionary mapping categories to their number of occurrences.
   */
  private val totalCategoryCount: mutable.Map[K, Int] = mutable.Map()
  /**
   * The classifier's memory. It will forget old classifications as soon as
   * they become too old.
   */
  private val memoryQueue: mutable.Queue[Classification[T, K]] = mutable.Queue()

  def features: Set[T] = totalFeatureCount.keySet.toSet

  def categories: Set[K] = totalCategoryCount.keySet.toSet

  def categoriesTotal: Int = totalCategoryCount.values.sum

  /**
   * Retrieves the memory's capacity.
   *
   * @return The memory's capacity.
   */
  def getMemoryCapacity: Int = {
    memoryCapacity
  }

  /**
   * Sets the memory's capacity.  If the new value is less than the old
   * value, the memory will be truncated accordingly.
   *
   * @param memoryCapacity The new memory capacity.
   */
  def setMemoryCapacity(memoryCapacity: Int) {
    var i: Int = memoryCapacity
    while (i > memoryCapacity && memoryQueue.nonEmpty) {
      memoryQueue.dequeue()
      i -= 1
    }
    this.memoryCapacity = memoryCapacity
  }

  /**
   * Increments the count of a given feature in the given category.  This is
   * equal to telling the classifier, that this feature has occurred in this
   * category.
   *
   * @param feature The feature, which count to increase.
   * @param category The category the feature occurred in.
   */
  def incrementFeature(feature: T, category: K) {
    val features: mutable.Map[T, Int] = featureCountPerCategory.getOrElse(category, {
      val newMap = mutable.Map[T, Int]()
      featureCountPerCategory.put(category, newMap)
      newMap
    })
    features.update(feature, features.getOrElse(feature, 0) + 1)

    totalFeatureCount.update(feature, totalFeatureCount.getOrElse(feature, 0) + 1)
  }

  /**
   * Increments the count of a given category.  This is equal to telling the
   * classifier, that this category has occurred once more.
   *
   * @param category The category, which count to increase.
   */
  def incrementCategory(category: K) {
    totalCategoryCount.update(category, totalCategoryCount.getOrElse(category, 0) + 1)
  }

  /**
   * Decrements the count of a given feature in the given category.  This is
   * equal to telling the classifier that this feature was classified once in
   * the category.
   *
   * @param feature The feature to decrement the count for.
   * @param category The category.
   */
  def decrementFeature(feature: T, category: K) {

    for {
      features <- featureCountPerCategory.get(category)
      count <- features.get(feature)
    } {
      if (count == 1) {
        features.remove(feature)
        if (features.size == 0) {
          featureCountPerCategory.remove(category)
        }
      } else {
        features.put(feature, count - 1)
      }

      for {
        totalCount <- totalFeatureCount.get(feature)
      } {
        if (totalCount == 1) {
          totalFeatureCount.remove(feature)
        } else {
          totalFeatureCount.put(feature, totalCount - 1)
        }
      }
    }
  }

  /**
   * Decrements the count of a given category.  This is equal to telling the
   * classifier, that this category has occurred once less.
   *
   * @param category The category, which count to increase.
   */
  def decrementCategory(category: K) {
    for {
      count <- totalCategoryCount.get(category)
    } {
      if (count == 1) {
        totalCategoryCount.remove(category)
      } else {
        totalCategoryCount.put(category, count -1)
      }
    }
  }

  /**
   * Retrieves the number of occurrences of the given feature in the given
   * category.
   *
   * @param feature The feature, which count to retrieve.
   * @param category The category, which the feature occurred in.
   * @return The number of occurrences of the feature in the category.
   */
  def featureCount(feature: T, category: K): Int = {
    featureCountPerCategory.get(category).flatMap(_.get(feature)).getOrElse(0)
  }

  /**
   * Retrieves the number of occurrences of the given category.
   *
   * @param category The category, which count should be retrieved.
   * @return The number of occurrences.
   */
  def categoryCount(category: K): Int = totalCategoryCount.getOrElse(category, 0)

  /**
   * {@inheritDoc}
   */
  override def featureProbability(feature: T, category: K): Float = {
    categoryCount(category) match {
      case 0 => 0
      case count => featureCount(feature, category).toFloat / count.toFloat
    }
  }

  /**
   * Retrieves the weighed average <code>P(feature|category)</code> with
   * the given weight and an assumed probability of <code>0.5</code> and the
   * given object to use for probability calculation.
   *
   * @see com.tsukaby.bayes.classifier.Classifier#featureWeighedAverage(Object, Object, IFeatureProbability, float, float)
   *
   * @param feature The feature, which probability to calculate.
   * @param category The category.
   * @param calculator The calculating object.
   * @param weight The feature weight.
   * @return The weighed average probability.
   */
  def featureWeighedAverage(
    feature: T,
    category: K,
    calculator: Option[IFeatureProbability[T, K]] = None,
    weight: Float = 1.0f): Float = {
    featureWeighedAverage(feature, category, calculator, weight, 0.5f)
  }

  /**
   * Retrieves the weighed average <code>P(feature|category)</code> with
   * the given weight, the given assumed probability and the given object to
   * use for probability calculation.
   *
   * @param feature The feature, which probability to calculate.
   * @param category The category.
   * @param calculator The calculating object.
   * @param weight The feature weight.
   * @param assumedProbability The assumed probability.
   * @return The weighed average probability.
   */
  def featureWeighedAverage(
    feature: T,
    category: K,
    calculator: Option[IFeatureProbability[T, K]],
    weight: Float,
    assumedProbability: Float): Float = {
    val basicProbability: Float = calculator.getOrElse(this).featureProbability(feature, category)
    val totals: Int = totalFeatureCount.getOrElse(feature, 0)
    (weight * assumedProbability + totals * basicProbability) / (weight + totals)
  }

  /**
   * Train the classifier by telling it that the given features resulted in
   * the given category.
   *
   * @param category The category the features belong to.
   * @param features The features that resulted in the given category.
   */
  def learn(category: K, features: Traversable[T]) {
    learn(Classification[T, K](features, category))
  }

  /**
   * Train the classifier by telling it that the given features resulted in
   * the given category.
   *
   * @param classification The classification to learn.
   */
  def learn(classification: Classification[T, K]) {
    for (feature <- classification.features) incrementFeature(feature, classification.category)
    incrementCategory(classification.category)
    memoryQueue.enqueue(classification)
    if (memoryQueue.size > memoryCapacity) {
      val toForget: Classification[T, K] = memoryQueue.dequeue()
      for (feature <- toForget.features) decrementFeature(feature, toForget.category)
      decrementCategory(toForget.category)
    }
  }

  /**
   * The classify method.  It will retrieve the most likely category for the
   * features given and depends on the concrete classifier implementation.
   *
   * @param features The features to classify.
   * @return The category most likely.
   */
  def classify(features: Traversable[T]): Option[Classification[T, K]]
}
