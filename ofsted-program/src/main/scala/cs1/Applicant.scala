package ofsted.cs1

import java.time.{LocalDate => Date}
import ofsted._

sealed trait Applicant
case class Individual (
  title: String,
  firstName: String,
  surname: String,
  dateOfBirth: Date,
  position: String,
  management: Boolean,
  contactChildren: Boolean
) extends Applicant

sealed trait OrgOrPartnership extends Applicant {
  def refusedApplication: Option[RefusedApplication]
  def name: String
  def address: Address
}

case class Organisation (
  refusedApplication: Option[RefusedApplication], //
  organisationSector: OrganisationSector, //
  organisationType: OrganisationType, //
  name: String, //
  address: Address, //
  email: String, //
  telephone: String, //
  creationDate: Date, //
  charityNumber: Option[String], //
  companyNumber: Option[String], //
  commsOptOut: Boolean, //
  holdingCompany: Option[HoldingCompany], //
  manager: Individual, // 
  members: Option[Individual], // list 
  membersDisqualified: Option[Individual] // list
) extends OrgOrPartnership

case class Partnership (
  refusedApplication: Option[RefusedApplication],
  name: String,
  address: Address,
  members: Option[Individual]
) extends OrgOrPartnership
