package ofsted.cs1

import enumeratum._

sealed trait OrganisationSector extends EnumEntry
object OrganisationSector extends Enum[OrganisationSector] {
  val values = findValues
  case object LocalAuthority extends OrganisationSector
  case object HealthAuthority extends OrganisationSector
  case object Voluntary extends OrganisationSector
  case object Private extends OrganisationSector
}
