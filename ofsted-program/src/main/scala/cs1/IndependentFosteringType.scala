package ofsted.cs1

import enumeratum._

sealed trait IndependentFosteringType extends EnumEntry
object IndependentFosteringType extends Enum[IndependentFosteringType] {
  val values = findValues
  case object Option1 extends IndependentFosteringType
  case object Option2 extends IndependentFosteringType
}
