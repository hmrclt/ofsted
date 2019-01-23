package ofsted.cs1

import ofsted.Address
import java.time.{LocalDate => Date}

case class HoldingCompany (
  name: String, //
  address: Address, //
  email: String, //
  telephone: String, //
  creationDate: Date, //
  charityNumber: Option[String], //
  companyNumber: Option[String], //
  subsidiaries: Option[Subsidiary] //
)
