package ofsted.cs1

import enumeratum._

sealed trait Act extends EnumEntry
object Act extends Enum[Act] {
  val values = findValues
  case object RegHomes1984 extends Act
  case object RegHomes1991 extends Act
  case object ChildrenAct1989 extends Act
  case object ChildcareAct2006 extends Act
  case object NursesAgenciesAct1957 extends Act
  case object CareStdAct2000 extends Act
  case object HealthSocialCareAct2008 extends Act
}
