package ofsted.cs1

import ofsted.Address
import java.time.{LocalDate => Date}

case class Site (
  address: Address,
  telephone: String,
  fax: String,  
  email: String,
  description: String,  
  soleUse: Option[String],
  pendingWork: Option[(String, Date)]
)
