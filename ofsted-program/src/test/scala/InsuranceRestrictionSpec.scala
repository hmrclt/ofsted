package ofsted

import org.atnos.eff._
import org.scalatest._
import ltbs.uniform.interpreters.logictable._
import org.atnos.eff.syntax.all._
import InsuranceRestriction._

class InsuranceRestrictionSpec extends FlatSpec with Matchers {
  type CStack = FxAppend[Stack, LogicTableStack]

  "InsuranceRestriction's uniform journey" should "work with simple cases" in {
    val executionResult = uniform[CStack].
      giveExamples(List("test")).
      giveExamples(List(true,false)).
      runEither.runWriter.runList.run.map(_._1)

    executionResult should be (
      List(
        Right(InsuranceRestriction("test",true)),
        Right(InsuranceRestriction("test",false))
      )
    )
  }
}
