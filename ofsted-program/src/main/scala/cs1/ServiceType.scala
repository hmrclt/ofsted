package ofsted.cs1

import enumeratum._

sealed trait ServiceType extends EnumEntry
object ServiceType extends Enum[ServiceType] {

  val values = findValues

  case object ChildrenHome extends ServiceType
  case object AdoptionSupportingAgency extends ServiceType
  case object IndependentFosteringAgency extends ServiceType
  case object ResidentialFamilyCentre extends ServiceType
  case object VoluntaryAdoption extends ServiceType
  case object ResidentialHolidayScheme extends ServiceType
}
