package ofsted

import org.atnos.eff._
import org.scalatest._
import ltbs.uniform.interpreters.logictable._
import org.atnos.eff.syntax.all._
import HealthCondition._

class HealthConditionSpec extends FlatSpec with Matchers {

  type CStack = FxAppend[Stack, LogicTableStack]

  "HealthCondition's uniform journey" should "understand some simple input" in {

    val (executionResult,_) = uniform[CStack].
      giveSelectionExamples(List(Set(AsthmaBreathing: HealthCondition))).
      giveExamples(List("wheezy")).
      runEither.runWriter.runList.run.head

    executionResult should be (Right(Map(AsthmaBreathing -> "wheezy")))
  }

  it should "work with the extremely healthy" in {

    val (executionResult,_) = uniform[CStack].
      giveSelectionExamples(List(Set.empty[HealthCondition])).
      giveExamples(List.empty[String]).
      runEither.runWriter.runList.run.head

    executionResult should be (Right(Map()))
  }

  it should "work with the incredibly ill" in {
    val prefix = "healthcondition-"

    val (executionResult,_) = uniform[CStack].
      giveSelectionExamples(List(HealthCondition.values.toSet)).
      giveExamples{
        case s: String if s.startsWith(prefix) => List(s"Really bad ${s.drop(prefix.length)}")
      }.
      runEither.runWriter.runList.run.head

    executionResult should be {
      val all = HealthCondition.values.map{ x =>
        (x, s"Really bad ${x.toString}")
      }
      Right(Map(all:_*))
    }
  }

}
