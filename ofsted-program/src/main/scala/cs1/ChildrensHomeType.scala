package ofsted.cs1

import enumeratum._

sealed trait ChildrensHomeType extends EnumEntry
object ChildrensHomeType extends Enum[ChildrensHomeType] {
  val values = findValues
  case object SecureChildrensHome extends ChildrensHomeType
  case object RefugeUnderSection51OfTheChildrenAct1989 extends ChildrensHomeType
  case object ResidentialSpecialSchool extends ChildrensHomeType
  case object BoardingSchool extends ChildrensHomeType
  case object ChildrensHome extends ChildrensHomeType
}
