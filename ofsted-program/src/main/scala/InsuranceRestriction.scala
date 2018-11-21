package ofsted

import ltbs.uniform._
import org.atnos.eff._
import cats.implicits._

case class InsuranceRestriction (
  reason: String,
  healthRelated: Boolean
)

object InsuranceRestriction {

  type Stack = Fx2[UniformAsk[String,?], UniformAsk[Boolean,?]]

  def uniform[R : _uniform[String,?] : _uniform[Boolean,?]]: Eff[R, InsuranceRestriction] = (
    uask[R, String]("reason"),
    uask[R, Boolean]("healthRelated")
  ).mapN(InsuranceRestriction.apply)

}
