package ofsted

import enumeratum._

sealed trait Gender extends EnumEntry

object Gender extends Enum[Gender] {

  val values = findValues

  case object Male extends Gender
  case object Female extends Gender

}

