package ofsted.cs1

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

case class AdoptionSupportingAgency(
  adoptionSupportType: Set[AdoptionSupportType],
  adoptionSupportingAgencyServices : AdoptionSupportingAgencyServices
) extends Service

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
