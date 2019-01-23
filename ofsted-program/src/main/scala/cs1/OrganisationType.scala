package ofsted.cs1

import enumeratum._

sealed trait OrganisationType extends EnumEntry
object OrganisationType extends Enum[OrganisationType] {
  val values = findValues
  case object Company extends OrganisationType
  case object StatutoryBody extends OrganisationType
  case object Committee extends OrganisationType
  case object Other extends OrganisationType
}
