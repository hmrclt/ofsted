package ofsted.cs1

import java.time.{LocalDate => Date}

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
