package ofsted.cs1

import enumeratum._

sealed trait VoluntaryAdoptionType extends EnumEntry
object VoluntaryAdoptionType extends Enum[VoluntaryAdoptionType] {
  val values = findValues
  case object Domestic extends VoluntaryAdoptionType
  case object InterCountry extends VoluntaryAdoptionType
  case object Support extends VoluntaryAdoptionType  
}
