package ofsted.cs1

import enumeratum._

sealed trait ApplicantType extends EnumEntry
object ApplicantType extends Enum[ApplicantType] {
  val values = findValues
  case object Individual extends ApplicantType
  case object Organisation extends ApplicantType
  case object Partnership extends ApplicantType
}
