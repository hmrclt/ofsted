package ofsted

import enumeratum._
import ltbs.uniform._
import org.atnos.eff._
import cats.implicits._

sealed trait HealthCondition extends EnumEntry

object HealthCondition extends Enum[HealthCondition] {

  val values = findValues

  case object PhysicalAbilityLiftChild extends HealthCondition
  case object ImpairConsciousness      extends HealthCondition
  case object Hearing                  extends HealthCondition
  case object Eyesight                 extends HealthCondition
  case object EmotionalIssues          extends HealthCondition
  case object SeverePain               extends HealthCondition
  case object Drowsiness               extends HealthCondition
  case object BlackoutsEpilepsy        extends HealthCondition
  case object HeartProblems            extends HealthCondition
  case object Diabetes                 extends HealthCondition
  case object AsthmaBreathing          extends HealthCondition
  case object MuscleJointProblems      extends HealthCondition
  case object AlcoholDrugDependency    extends HealthCondition
  case object InfectiousDiseases       extends HealthCondition

  type Stack = Fx2[UniformAsk[String,?], UniformAsk[Set[HealthCondition],?]]

  def uniform[R : _uniform[String,?] : _uniform[Set[HealthCondition],?]]: Eff[R, Map[HealthCondition, String]] =
    uask[R, Set[HealthCondition]]("healthconditions") flatMap { 
      _.map { hc => uask[R, String](s"healthcondition-${hc.toString}").map{d => (hc,d)} }.toList.sequence.map(_.toMap)
    }
}
