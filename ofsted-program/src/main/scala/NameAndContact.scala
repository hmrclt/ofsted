package ofsted

import ltbs.uniform._
import org.atnos.eff._
import cats.implicits._
import java.time.{ LocalDate => Date }

case class NameAndContact (
  name: Name,
  gender: Gender,
  dob: Date,
  placeofBirth: (Town, Country),
  postalAddress: Address,
  telephone: String,
  mobile: String,
  emailAddress: Option[String],
  applicantType: String
)

object NameAndContact {

  type Stack = Fx.fx5[UniformAsk[String,?], UniformSelect[Gender,?], UniformAsk[Date, ?], UniformAsk[Address, ?], UniformAsk[Option[String], ?]]

  def askName[R : _uniform[String,?]]: Eff[R, Name] = (
    uask[R, String]("forename"),
    uask[R, String]("surname")
  ).tupled

  def uniform[R : _uniform[String,?] : _uniformSelect[Gender,?] : _uniform[Date,?] : _uniform[Address,?] : _uniform[Option[String],?]]: Eff[R, NameAndContact] = (
    askName,
    uaskOneOf[R, Gender]("gender", Gender.values.toSet),
    uask[R, Date]("dateOfBirth"),
    (
      uask[R, String]("townOfBirth"),
      uask[R, String]("countryOfBirth")
    ).tupled,
    uask[R, Address]("postalAddress"),
    uask[R, String]("telephone"),
    uask[R, String]("mobile"),
    uask[R, Option[String]]("emailAddress"),
    uask[R, String]("applicantType")
    ).mapN(NameAndContact.apply)
}
