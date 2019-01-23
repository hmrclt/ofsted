package ofsted.cs1

import enumeratum._

sealed trait AdoptionSupportType extends EnumEntry
object AdoptionSupportType extends Enum[AdoptionSupportType] {
  val values = findValues
  case object Adults extends AdoptionSupportType
  case object Children extends AdoptionSupportType
}
