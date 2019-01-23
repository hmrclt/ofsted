package ofsted.cs1

case class Signature(
  title: String,
  forename: String,
  surname: String,
  userStatus: String,
  onBehalfOf: Signature.OnBehalfOf
)

object Signature {

  sealed trait OnBehalfOf
  object OnBehalfOf {
    case object Company extends OnBehalfOf
    case object Partnership extends OnBehalfOf
    case object StatutoryBody extends OnBehalfOf
    case object Committee extends OnBehalfOf

    val Other = z.Other    
    object z { 
      final case class Other(details: String) extends OnBehalfOf
    }

  }
}
