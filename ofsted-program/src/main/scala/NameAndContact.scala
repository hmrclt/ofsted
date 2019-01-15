package ofsted

import ltbs.uniform._
import org.atnos.eff._
import cats.implicits._
import java.time.{ LocalDate => Date }

case class Name(
  forename: String,
  surname: String,
  gender: Gender
)

case class BirthPlace(
  town: String,
  country: String
)

case class NameAndContact (
  name: Name,
  gender: Gender,
  dob: Date,
  placeofBirth: BirthPlace,
  postalAddress: Address,
  telephone: String,
  mobile: String,
  emailAddress: Option[String],
  applicantType: String
)

object NameAndContact {

  def uniform[R : _uniform[String,?] : _uniform[Gender,?] : _uniform[Date,?] : _uniform[Address,?] : _uniform[Option[String],?] : _uniform[Name,?] : _uniform[BirthPlace,?]]: Eff[R, NameAndContact] = (
    uask[R, Name]("name"),
    uask[R, Gender]("gender"),
    uask[R, Date]("dateOfBirth"),
    uask[R, BirthPlace]("placeOfBirth"),
    uask[R, Address]("postalAddress"),
    uask[R, String]("telephone"),
    uask[R, String]("mobile"),
    uask[R, Option[String]]("emailAddress"),
    uask[R, String]("applicantType")
  ).mapN(NameAndContact.apply)
}
