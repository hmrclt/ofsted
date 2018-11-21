package ofsted

import org.atnos.eff._
import org.scalatest._
import ltbs.uniform.interpreters.logictable._
import org.atnos.eff.syntax.all._
import HealthDeclaration._

class HealthDeclarationSpec extends FlatSpec with Matchers {
  type CStack = FxAppend[Stack, LogicTableStack]

  "HealthDeclaration's uniform journey" should "work with simple cases" in {
    1 should be (1)
  }
}
