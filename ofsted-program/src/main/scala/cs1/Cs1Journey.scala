package ofsted.cs1

import ltbs.uniform._
import org.atnos.eff._
import cats.implicits._
import enumeratum._
import ofsted._
import java.time.{LocalDate => Date}
//import java.io.File

case class File(file: String)

sealed trait ServiceType extends EnumEntry
object ServiceType extends Enum[ServiceType] {

  val values = findValues

  case object ChildrenHome extends ServiceType
  case object AdoptionSupportingAgency extends ServiceType
  case object IndependentFosteringAgency extends ServiceType
  case object ResidentialFamilyCentre extends ServiceType
  case object VoluntaryAdoption extends ServiceType
  case object ResidentialHolidayScheme extends ServiceType
}

sealed trait ApplicantType extends EnumEntry
object ApplicantType extends Enum[ApplicantType] {
  val values = findValues
  case object Individual extends ApplicantType
  case object Organisation extends ApplicantType
  case object Partnership extends ApplicantType
}

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

sealed trait Applicant
case class Individual (
  title: String,
  firstName: String,
  surname: String,
  dateOfBirth: Date,
  position: String,
  management: Boolean,
  contactChildren: Boolean
) extends Applicant

sealed trait OrgOrPartnership extends Applicant {
  def refusedApplication: Option[RefusedApplication]
  def name: String
  def address: Address
}

sealed trait OrganisationType extends EnumEntry
object OrganisationType extends Enum[OrganisationType] {
  val values = findValues
  case object Company extends OrganisationType
  case object StatutoryBody extends OrganisationType
  case object Committee extends OrganisationType
  case object Other extends OrganisationType
}

sealed trait OrganisationSector extends EnumEntry
object OrganisationSector extends Enum[OrganisationSector] {
  val values = findValues
  case object LocalAuthority extends OrganisationSector
  case object HealthAuthority extends OrganisationSector
  case object Voluntary extends OrganisationSector
  case object Private extends OrganisationSector
}

case class Subsidiary (
  name: String, //
  address: Address, //
  email: String, //
  telephone: String, //
  ofstedRegNo: String //
)

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

case class Organisation (
  refusedApplication: Option[RefusedApplication], //
  organisationSector: OrganisationSector, //
  organisationType: OrganisationType, //
  name: String, //
  address: Address, //
  email: String, //
  telephone: String, //
  creationDate: Date, //
  charityNumber: Option[String], //
  companyNumber: Option[String], //
  commsOptOut: Boolean, //
  holdingCompany: Option[HoldingCompany], //
  manager: Individual, // 
  members: Option[Individual], // list 
  membersDisqualified: Option[Individual] // list
) extends OrgOrPartnership

case class Partnership (
  refusedApplication: Option[RefusedApplication],
  name: String,
  address: Address,
  members: Option[Individual]
) extends OrgOrPartnership

case class RegisteredEstablishmentOrAgency (
  name: String,
  address: Address,
  ofstedRegNo: Option[String]
)

case class Establishment (
  name: String,
  address: Address,
  telephone: String,
  email: String,
  soleUse: Option[String]
)

case class Site (
  address: Address,
  telephone: String,
  fax: String,  
  email: String,
  description: String,  
  soleUse: Option[String],
  pendingWork: Option[(String, Date)]
)

case class Cs1Form(
  serviceType : ServiceType,
  applicantType : ApplicantType,
  isEducationalEstablishment: Boolean,
  dfeNumber: Option[String],
  rea: Option[RegisteredEstablishmentOrAgency],
  financialViability: Option[String],
  interestsRea: Option[RegisteredEstablishmentOrAgency],
  applicableActs: Set[Act],
  moreDetails: Option[String],
  targetOpDate: Date,
  applicant: Applicant,
  establishment: Establishment,
  otherPremises: Option[Site],
  travelArrangements: Option[String],
  planningPermit: Option[Option[File]],
  okWithoutPermitChange: Option[Boolean],
  accessReqDisabilityAct2005: Option[String],
  premisesSecurity: String,
  staffPosts: Option[Staff],
  staffPostsTimes: (Int,Int,Boolean),
  service: Service,
  establishmentChargesRange: String,
  managerTitle: String,
  managerForename: String,
  managerSurname: String,
  managerOtherAgency: Option[String],
  checksAdoptionRegulations2005: Boolean,
  proceduresAndPolicies: File,
  noPreviousApplicationSince30Sep2010: Boolean,
  signature: Signature,
  researchOrgComsOptOut: Boolean
)

case class RefusedApplication(
  refAppName: String,
  refAppOfstedRegNo: String,
  refAppOrgID: String,
  refAppDetails: String
)

case class Staff(
  staffPosition: String,
  staffDuties: String
)

sealed trait ChildrensHomeType extends EnumEntry
object ChildrensHomeType extends Enum[ChildrensHomeType] {
  val values = findValues
  case object SecureChildrensHome extends ChildrensHomeType
  case object RefugeUnderSection51OfTheChildrenAct1989 extends ChildrensHomeType
  case object ResidentialSpecialSchool extends ChildrensHomeType
  case object BoardingSchool extends ChildrensHomeType
  case object ChildrensHome extends ChildrensHomeType
}

case class MaxChildrenPerCategory(
  emotional: Int,
  physical: Int,
  learning: Int,
  mental: Int,
  drug: Int,
  alcohol: Int,
  eyesight: Int
)

sealed trait Service
case class ChildrenHome(
  establishmentType: ChildrensHomeType,
  regPlacesNo: Int,
  homeSingleSex: Boolean,
  maxChildrenPerCategory: MaxChildrenPerCategory,
  care4Adults: Option[String],
  ageRange: (Int, Int),
  childrenSelectionCriteria: Option[String],
  facilitiesServiceDetails: String,
  protectingHealthDetails: String,
  fireEmergencyPrecautions: String,
  religiousObservence: String,
  contactChildRelatives: String,
  stepsHomeLocation: String,
  childrenConcernsComplaints: String,
  childrenEducation: String,
  placementReviewing: String,
  indepPersonReg43CH2015: Boolean,
  establishmentCareQualCom: Boolean,
  shortBreaksService: Boolean
) extends Service

sealed trait AdoptionSupportType extends EnumEntry
object AdoptionSupportType extends Enum[AdoptionSupportType] {
  val values = findValues
  case object Adults extends AdoptionSupportType
  case object Children extends AdoptionSupportType
}

case class AdoptionSupportingAgencyServices(
  counseling: Boolean,
  info: Boolean,
  birthRecords: Boolean,
  intermediary: Boolean,
  contactFormerFamily: Boolean,
  therapeuticNeeds: Boolean,
  relationAdoptiveParents: Boolean,
  disruptionMediation: Boolean,
  intermediaryServices: Boolean,
  parentTraining: Boolean
)

case class AdoptionSupportingAgency(
  adoptionSupportType: Set[AdoptionSupportType],
  adoptionSupportingAgencyServices : AdoptionSupportingAgencyServices
) extends Service

sealed trait IndependentFosteringType extends EnumEntry
object IndependentFosteringType extends Enum[IndependentFosteringType] {
  val values = findValues
  case object Option1 extends IndependentFosteringType
  case object Option2 extends IndependentFosteringType
}

case class IndependentFosteringAgency(
      serviceType: IndependentFosteringType,
      parentChildrenAct1989: Boolean,
      panel: Boolean,
      panelVarious: Boolean,
      panelJoint: Boolean
) extends Service

case class ResidentialFamilyCentre(
      familiesAccomodated: Int,
      emergencyAdmissions: Boolean,
      courtReferrals: Boolean,
      refuge: Boolean,
      careForAdultsWithoutChildren: Boolean,
      admitChildrenWithCarers: Boolean,
      otherService: Boolean
) extends Service

sealed trait VoluntaryAdoptionType extends EnumEntry
object VoluntaryAdoptionType extends Enum[VoluntaryAdoptionType] {
  val values = findValues
  case object Domestic extends VoluntaryAdoptionType
  case object InterCountry extends VoluntaryAdoptionType
  case object Support extends VoluntaryAdoptionType  
}

case class VoluntaryAdoption(
  serviceType: Set[VoluntaryAdoptionType],
  branches: Option[String],
  panel: Boolean,
  panelMoreBranches: Boolean, // This should only appear if adoptionBranches is populated (i.e. Answer==Yes)
  birthRecordsCounselling: Boolean,
  intermediaryServices: Boolean,
  supportProvided: Set[AdoptionSupportType]

) extends Service

case class ResidentialHolidayScheme(
      ageRange: (Int, Int),
      singleSex: Boolean,
      selectionCriteria: Option[String],
      facilitiesServicesDetails: String,
      missingChildrenProcedure: String,
      concernsComplaints: String
) extends Service

case class Signature(
  title: String,
  forename: String,
  surname: String,
  userStatus: String,
  onBehalfOf: Signature.OnBehalfOf
)

object Signature {
  case class OnBehalfOf(text: String)
//   sealed trait OnBehalfOf
//   object OnBehalfOf {
//     case object Company extends OnBehalfOf
//     case object Partnership extends OnBehalfOf
//     case object StatutoryBody extends OnBehalfOf
//     case object Committee extends OnBehalfOf
//     final case class Other(details: String) extends OnBehalfOf    
//   }
}

object Cs1Journey {

  type Stack = Fx.append[Fx.append[
    Fx.fx12[
      UniformAsk[ServiceType,?],
      UniformAsk[ApplicantType,?],
      UniformAsk[Boolean,?],
      UniformAsk[Option[String],?],
      UniformAsk[Option[RegisteredEstablishmentOrAgency],?],
      UniformAsk[Set[Act],?],
      UniformAsk[String,?],
      UniformAsk[Option[RefusedApplication],?],
      UniformAsk[Date,?],
      UniformAsk[OrganisationSector,?],
      UniformAsk[OrganisationType,?],
      UniformAsk[Address,?]
    ],
    Fx.fx11[
      UniformAsk[HoldingCompany,?],
      UniformAsk[Option[Individual],?],
      UniformAsk[Individual,?],
      UniformAsk[Site,?],      
      UniformAsk[Establishment,?],
      UniformAsk[Option[Option[File]],?],
      UniformAsk[Signature,?],
      UniformAsk[Option[Staff],?],
      UniformAsk[File,?],
      UniformAsk[(Int,Int,Boolean),?],
      UniformAsk[Int,?]      
    ]],
    Fx.fx7[
      UniformAsk[ChildrensHomeType,?],
      UniformAsk[Set[VoluntaryAdoptionType],?],
      UniformAsk[IndependentFosteringType,?],
      UniformAsk[Set[AdoptionSupportType],?],
      UniformAsk[(Int,Int),?],
      UniformAsk[AdoptionSupportingAgencyServices, ?],
      UniformAsk[MaxChildrenPerCategory, ?]
    ]
  ]

  def program[R
      : _uniform[ServiceType,?]
      : _uniform[ApplicantType,?]
      : _uniform[Boolean,?]
      : _uniform[Option[String],?]
      : _uniform[Option[RegisteredEstablishmentOrAgency],?]
      : _uniform[Set[Act],?]
      : _uniform[String,?]
      : _uniform[Option[RefusedApplication],?]
      : _uniform[Date,?]
      : _uniform[OrganisationSector,?]
      : _uniform[OrganisationType,?]
      : _uniform[Address,?]
      : _uniform[HoldingCompany,?]
      : _uniform[Option[Individual],?]
      : _uniform[Individual,?]
      : _uniform[Site,?]
      : _uniform[Establishment,?]
      : _uniform[Option[Option[File]],?]
      : _uniform[Signature,?]
      : _uniform[Option[Staff],?]
      : _uniform[File,?]
      : _uniform[(Int,Int),?]    
      : _uniform[(Int,Int,Boolean),?]
      : _uniform[Int,?]
      : _uniform[ChildrensHomeType,?]
      : _uniform[Set[VoluntaryAdoptionType],?]
      : _uniform[IndependentFosteringType,?]
      : _uniform[Set[AdoptionSupportType],?]
      : _uniform[AdoptionSupportingAgencyServices,?]
      : _uniform[MaxChildrenPerCategory, ?]    
  ]: Eff[R, Cs1Form] = {

    // the 'or' boolean monoid - we need this for 'emptyUnless'
    implicit val mbool = new cats.Monoid[Boolean] {
      def empty = false
      def combine(a: Boolean, b: Boolean) = a || b
    }

    def applicantProgram(applicantType: ApplicantType): Eff[R, Applicant] = {
      applicantType match {
        case ApplicantType.Individual =>
          uask[R,Individual]("individual").map{x => x : Applicant}
        case ApplicantType.Organisation => (
          uask[R,Option[RefusedApplication]]("refusedApplication"),
          uask[R,OrganisationSector]("orgSector"),
          uask[R,OrganisationType]("orgType"),
          uask[R,String]("organisationName"),
          uask[R,Address]("address"),
          uask[R,String]("email"),
          uask[R,String]("telephone"),
          uask[R,Date]("creationDate"),
          uask[R,Option[String]]("charityNumber"),
          uask[R,Option[String]]("companyNumber"),
          uask[R,Boolean]("commsOptOut"),
          uask[R,HoldingCompany]("holdingCompany") when uask[R,Boolean]("holdingCompanyOption"),
          uask[R,Individual]("manager"), //
          uask[R,Option[Individual]]("organisationMembers"), // list
          uask[R,Option[Individual]]("membersDisqualified") // list
        ).mapN(Organisation.apply)
        case ApplicantType.Partnership => (
          uask[R,Option[RefusedApplication]]("refusedApplication"),
          uask[R,String]("partnershipName"),
          uask[R,Address]("address"),
          uask[R,Option[Individual]]("partnershipMembers")
        ).mapN(Partnership.apply)
      }
    }

    def serviceProgram(serviceType: ServiceType): Eff[R, Service] = serviceType match {
      case ServiceType.ChildrenHome => for {
        establishmentType          <- uask[R, ChildrensHomeType]("establishmentType")
        regPlacesNo                <- uask[R, Int]("regPlacesNo")
        homeSingleSex              <- uask[R, Boolean]("homeSingleSex")
        maxChildrenPerCategory     <- uask[R, MaxChildrenPerCategory]("maxChildrenPerCategory")
        care4Adults                <- uask[R, Option[String]]("care4Adults")
        ageRange                   <- uask[R, (Int, Int)]("ageRange")
        childrenSelectionCriteria  <- uask[R, Option[String]]("childrenSelectionCriteria")
        facilitiesServiceDetails   <- uask[R, String]("facilitiesServiceDetails")
        protectingHealthDetails    <- uask[R, String]("protectingHealthDetails")
        fireEmergencyPrecautions   <- uask[R, String]("fireEmergencyPrecautions")
        religiousObservence        <- uask[R, String]("religiousObservence")
        contactChildRelatives      <- uask[R, String]("contactChildRelatives")
        stepsHomeLocation          <- uask[R, String]("stepsHomeLocation")
        childrenConcernsComplaints <- uask[R, String]("childrenConcernsComplaints")
        childrenEducation          <- uask[R, String]("childrenEducation")
        placementReviewing         <- uask[R, String]("placementReviewing")
        indepPersonReg43CH2015     <- uask[R, Boolean]("indepPersonReg43CH2015")
        establishmentCareQualCom   <- uask[R, Boolean]("establishmentCareQualCom")
        shortBreaksService         <- uask[R, Boolean]("shortBreaksService")
      } yield ChildrenHome(
        establishmentType, regPlacesNo, homeSingleSex, maxChildrenPerCategory, care4Adults, ageRange,
        childrenSelectionCriteria, facilitiesServiceDetails, protectingHealthDetails,
        fireEmergencyPrecautions, religiousObservence, contactChildRelatives,
        stepsHomeLocation, childrenConcernsComplaints, childrenEducation,
        placementReviewing, indepPersonReg43CH2015, establishmentCareQualCom,
        shortBreaksService
      )

      case ServiceType.AdoptionSupportingAgency => (
        uask[R, Set[AdoptionSupportType]]("adoptionSupportType"),
        uask[R, AdoptionSupportingAgencyServices]("adoptionSupportingAgencyServices")
      ).mapN(AdoptionSupportingAgency)

      case ServiceType.IndependentFosteringAgency => (
        uask[R, IndependentFosteringType]("independentFosteringType"),
        uask[R, Boolean]("parentChildrenAct1989"),
        uask[R, Boolean]("panel"),
        uask[R, Boolean]("panelVarious"),
        uask[R, Boolean]("panelJoint")
      ).mapN(IndependentFosteringAgency)

      case ServiceType.ResidentialFamilyCentre => (
        uask[R, Int]("familiesAccomodated"),
        uask[R, Boolean]("emergencyAdmissions"),
        uask[R, Boolean]("courtReferrals"),
        uask[R, Boolean]("refuge"),
        uask[R, Boolean]("careForAdultsWithoutChildren"),
        uask[R, Boolean]("admitChildrenWithCarers"),
        uask[R, Boolean]("otherService")
      ).mapN(ResidentialFamilyCentre)

      case ServiceType.VoluntaryAdoption => (
        uask[R, Set[VoluntaryAdoptionType]]("voluntaryAdoptionType"),
        uask[R, Option[String]]("branches"),
        uask[R, Boolean]("panel"),
        uask[R, Boolean]("panelMoreBranches"),
        uask[R, Boolean]("birthRecordsCounselling"),
        uask[R, Boolean]("intermediaryServices"),
        uask[R, Set[AdoptionSupportType]]("supportProvidedDetail") emptyUnless uask[R, Boolean]("supportProvided")
      ).mapN(VoluntaryAdoption)

      case ServiceType.ResidentialHolidayScheme => (
        uask[R, (Int, Int)]("ageRange"),
        uask[R, Boolean]("singleSex"),
        uask[R, Option[String]]("selectionCriteria"),
        uask[R, String]("facilitiesServicesDetails"),
        uask[R, String]("missingChildrenProcedure"),
        uask[R, String]("concernsComplaints")        
      ).mapN(ResidentialHolidayScheme)
    }

    for {
      serviceType                <- uask[R, ServiceType]("serviceType")
      applicantType              <- uask[R, ApplicantType]("applicantType")
      isEducationalEstablishment <- uask[R, Boolean]("isEducationalEstablishment")
      dfeNumber                  <- uask[R, Option[String]]("defNumber") emptyUnless isEducationalEstablishment
      rea                        <- uask[R, Option[RegisteredEstablishmentOrAgency]]("rea")
      financialViability         <- uask[R, Option[String]]("financialViability")
      interestsRea               <- uask[R, Option[RegisteredEstablishmentOrAgency]]("interestsRea")
      applicableActs             <- uask[R, Set[Act]]("applicableActs")             
      moreDetails                <- uask[R, String]("moreDetails") when (applicableActs.nonEmpty)
      targetOpDate               <- uask[R, Date]("targetOpDate")
      applicant                  <- applicantProgram(applicantType)
      establishment              <- uask[R, Establishment]("establishment")
      otherPremises              <- uask[R, Site]("otherPremises") when (uask[R, Boolean]("haveOtherPremises")) // Option[Site] not compiling for some reason
      travelArrangements         <- uask[R, String]("travelArrangements") when (otherPremises.toList.size >1)
      planningPermit             <- if (
        List(
          ServiceType.AdoptionSupportingAgency, ServiceType.IndependentFosteringAgency,
          ServiceType.VoluntaryAdoption, ServiceType.ChildrenHome, ServiceType.ResidentialFamilyCentre
        ) contains serviceType
      ) (uask[R, Option[Option[File]]]("planningPermit")) else (Eff.pure[R,Option[Option[File]]](None))
      okWithoutPermitChange      <- uask[R, Boolean]("okWithoutPermitChange") when (
                                      serviceType == ServiceType.ResidentialHolidayScheme)
      accessReqDisabilityAct2005 <- uask[R, Option[String]]("accessReqDisabilityAct2005")
      premisesSecurity           <- uask[R, String]("premisesSecurity")
      staffPosts                 <- uask[R, Option[Staff]]("staffPosts") // list
      staffPostsTimes            <- uask[R, (Int,Int,Boolean)]("staffPostsTimes") emptyUnless staffPosts.nonEmpty
      service                      <- serviceProgram(serviceType)
      establishmentChargesRange     <- uask[R, String]("establishmentChargesRange")
      managerTitle                  <- uask[R, String]("managerTitle")
      managerForename               <- uask[R, String]("managerForename")
      managerSurname                <- uask[R, String]("managerSurname")
      managerOtherAgency            <- uask[R, Option[String]]("managerOtherAgency")
      checksAdoptionRegulations2005 <- uask[R, Boolean]("checksAdoptionRegulations2005")
      proceduresAndPolicies         <- uask[R, File]("proceduresAndPolicies")

      noPreviousApplicationSince30Sep2010 <- uask[R, Boolean]("noPreviousApplicationSince30Sep2010")
      signature                           <- uask[R, Signature]("signature")
      researchOrgComsOptOut               <- uask[R, Boolean]("researchOrgComsOptOut")
    } yield Cs1Form(
      serviceType,
      applicantType,
      isEducationalEstablishment,
      dfeNumber,
      rea,
      financialViability,
      interestsRea,
      applicableActs,
      moreDetails,
      targetOpDate,
      applicant,
      establishment,
      otherPremises,
      travelArrangements,
      planningPermit,
      okWithoutPermitChange,
      accessReqDisabilityAct2005,
      premisesSecurity,
      staffPosts,
      staffPostsTimes,      
      service,
      establishmentChargesRange,
      managerTitle,
      managerForename,
      managerSurname,
      managerOtherAgency,
      checksAdoptionRegulations2005,
      proceduresAndPolicies,
      noPreviousApplicationSince30Sep2010,
      signature,
      researchOrgComsOptOut
    )
  }
}
