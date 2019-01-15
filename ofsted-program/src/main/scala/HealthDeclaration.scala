package ofsted

import ltbs.uniform._
import org.atnos.eff._
import java.time.{ LocalDate => Date }
import cats.implicits._

case class HealthDeclaration (
  nameAndContact: NameAndContact,
  ageCategories: AgeCategories,
  provisionName: String,
  provisionAddress: Address,
  provisionTelephoneNo: String,
  surgeryDetails: String,
  takingMedication: Option[String],
  healthCondition: Map[HealthCondition, String],
  insuranceRestrictions: Option[InsuranceRestriction],
  disabilityAllowance: Option[String],
  otherLast5years: Option[String],
  hospitalisedLast5years: Option[String],
  smoker: Boolean,
  drinkUnits: AlcoholUnits,
  previewMedicalReport: Boolean
)

object HealthDeclaration {

  type Stack = Fx.fx10[UniformAsk[String,?], UniformAsk[Gender,?], UniformAsk[Set[HealthCondition],?], UniformAsk[Date,?], UniformAsk[Address,?], UniformAsk[Option[String],?], UniformAsk[Int,?], UniformAsk[Boolean,?],  UniformAsk[BirthPlace,?], UniformAsk[Name,?]]

  def uniform[R : _uniform[String,?] : _uniform[Gender,?] : _uniform[Set[HealthCondition],?] : _uniform[Date,?] : _uniform[Address,?] : _uniform[Option[String],?] : _uniform[Int,?] : _uniform[Boolean,?] : _uniform[BirthPlace,?] : _uniform[Name,?]]: Eff[R, HealthDeclaration] = (
    NameAndContact.uniform,
    (
      uask[R, Int]("under1"),
      uask[R, Int]("over1")
    ).tupled,
    uask[R, String]("provisionName"),
    uask[R, Address]("provisionAddress"),
    uask[R, String]("provisionTelephoneNo"),
    uask[R, String]("surgeryDetails"),
    uask[R, Option[String]]("takingMedication"),
    HealthCondition.uniform,
    InsuranceRestriction.uniform when uask[R, Boolean]("insuranceRestrictions"),
    uask[R, Option[String]]("disabilityAllowance"),
    uask[R, Option[String]]("otherLast5years"),
    uask[R, Option[String]]("hospitalisedLast5years"),
    uask[R, Boolean]("smoker"),
    uask[R, AlcoholUnits]("drinkUnits"),
    uask[R, Boolean]("previewMedicalReport")
  ).mapN(HealthDeclaration.apply)

}
