package ofsted.cs1

import ofsted.Address

case class RegisteredEstablishmentOrAgency (
  name: String,
  address: Address,
  ofstedRegNo: Option[String]
)
