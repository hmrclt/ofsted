package ofsted.cs1
import ofsted.Address

case class Establishment (
  name: String,
  address: Address,
  telephone: String,
  email: String,
  soleUse: Option[String]
)
