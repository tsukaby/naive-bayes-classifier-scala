import com.tsukaby.bayes.classifier.BayesClassifier
import org.specs2.mutable.Specification

class Integration extends Specification {
  "Integration" >> {
    "#all" >> {
      "when gave any data" >> {
        "it returns result of classification" in {
          val bayes = new BayesClassifier[String, String]()
          bayes.learn("technology", "github" :: "git" :: "tech" :: "technology" :: Nil)
          bayes.learn("weather", "sun" :: "rain" :: "cloud" :: "weather" :: "snow" :: Nil)
          bayes.learn("government", "ballot" :: "winner" :: "party" :: "money" :: "candidate" :: Nil)

          val unknownText1 = "I use git".split(" ")
          val unknownText2 = "Today's weather is snow".split(" ")
          val unknownText3 = "I will vote for that party".split(" ")

          bayes.classify(unknownText1).map(_.category) must beSome("technology")
          bayes.classify(unknownText2).map(_.category) must beSome("weather")
          bayes.classify(unknownText3).map(_.category) must beSome("government")
        }
      }
    }
  }
}
