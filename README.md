# naive-bayes-classifier-scala
Naive bayes classifier library. Implemented by Scala.

This is porting from [ptnplanet/Java-Naive-Bayes-Classifier](https://github.com/ptnplanet/Java-Naive-Bayes-Classifier)

[![Build Status](https://travis-ci.org/tsukaby/naive-bayes-classifier-scala.svg?branch=master)](https://travis-ci.org/tsukaby/naive-bayes-classifier-scala)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.tsukaby/naive-bayes-classifier-scala_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.tsukaby/naive-bayes-classifier-scala_2.11)
[![Scaladoc](http://javadoc-badge.appspot.com/com.tsukaby/naive-bayes-classifier-scala_2.11.svg?label=scaladoc)](http://javadoc-badge.appspot.com/com.tsukaby/naive-bayes-classifier-scala_2.11)

## Getting Started

    libraryDependencies ++= Seq(
      "com.tsukaby" %% "naive-bayes-classifier-scala" % "0.1.0"
    )

This library has been published in the [Maven central](http://search.maven.org/#browse|-351387659).

## How to use

```
// Create instance
val bayes = new BayesClassifier[String, String]()

// Learning
bayes.learn("technology", "github" :: "git" :: "tech" :: "technology" :: Nil)
bayes.learn("weather", "sun" :: "rain" :: "cloud" :: "weather" :: "snow" :: Nil)
bayes.learn("government", "ballot" :: "winner" :: "party" :: "money" :: "candidate" :: Nil)

val unknownText1 = "I use git".split(" ")
val unknownText2 = "Today's weather is snow".split(" ")
val unknownText3 = "I will vote for that party".split(" ")

// Classify
println(bayes.classify(unknownText1).map(_.category).getOrElse("")) // technology
println(bayes.classify(unknownText2).map(_.category).getOrElse("")) // weather
println(bayes.classify(unknownText3).map(_.category).getOrElse("")) // government
```

## Contribution

I'm seeking your PR!!!
I'm easy.

For example.

- Bug fix
- Refactoring the code.
- Add new features and others.
- Fix my odd English texts.

## License

```
Copyright 2015 - 2015 tsukaby.com
MIT License
```
