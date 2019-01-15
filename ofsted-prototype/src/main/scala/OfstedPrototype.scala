package ofsted.prototype

import cats._, implicits._
import org.atnos.eff._
import org.atnos.eff.syntax.all._
import org.querki.jquery._
import ltbs.uniform.prototype._
import ltbs.uniform.widgets.govuk._

import JsInterpreter._
import JsImplementations._
import scala.scalajs.js.annotation.JSExportTopLevel
import cats.Monoid
import ltbs.uniform.datapipeline._
import InferParser._

import ofsted._
import cs1._
import java.io.File

object OfstedPrototype {

  implicit val messages = new MessagesProvider {
    reload()
  }

  @JSExportTopLevel("enableMessages")
  def enableMessages(): Unit = {
    messages.enabled = true
    messages.updateMessages()
  }

  @JSExportTopLevel("disableMessages")
  def disableMessages(): Unit = {
    messages.enabled = false
    messages.updateMessages()
  }

  @JSExportTopLevel("reloadMessages")
  def reloadMessages(): Unit = {
    messages.reload()
  }

  @JSExportTopLevel("saveAndContinue")
  def saveAndContinue(pageId: String): Unit = {
    journey(Submit(pageId))
    println(s"state: $state")
  }

  @JSExportTopLevel("backLink")
  def backLink(): Unit = {
    val (last::others) = breadcrumbs
    breadcrumbs = others
    journey(Back(last))
    ()
  }

  def main(args: Array[String]): Unit = {
    journey(Back(""))
    ()
  }

  var state: DB = implicitly[Monoid[DB]].empty
  var breadcrumbs: List[String] = Nil

  implicit val cmsMessages: Messages = Messages.fromPlayFormat{"""
address.heading=Address
applicableActs.heading=Has your organisation ever been registered or licensed for, or been the owner of or a partner in, any service registered or licensed under any of the following?
applicantType.heading=Are you applying as an individual, partnership or organisation?
charityNumber.heading=Do you know the registered charity number?
commsOptOut.heading=I would like to opt out of receiving communications electronically
companyNumber.heading=Do you know the registered company number?
creationDate.heading=Date that it came into being 
defNumber.heading=Please, provide the Department for Education number (if available)
email.heading=Email address
financialViability.heading=Are you/is your organisation financially viable, do you have the finance available to achieve the aims and objectives of the statement of purpose, and can you supply a business plan and details of cash-flow or predicted cash-flow?
financialViability.inner.hint=Please provide details
holdingCompany.heading=Is the organisation a subsidiary of a holding company?
individual.heading=!!!!!
interestsRea.heading=Do you/does your organisation have any current financial or work interests in one or more other establishments or agencies registered with OFSTED?
isEducationalEstablishment.heading=Are you applying to register an educational establishment (or planned education establishment) as a children’s home?
manager.heading=Please, provide the details of the responsible individual for the organisation
membersDisqualified.heading=!!!!!
moreDetails.heading=You need to provide more details
orgSector.heading=Organisation sector:
orgType.heading=Type of organisation:
rea.heading=What are the details of the registered establishment or agency?
refusedApplication.heading=Has your organisation or partnership ever had an application refused or registration cancelled?
refusedApplication.heading.hint=Only answer "Yes" if it happened under any of these acts: \
                                - Registered Homes Act 1984 \
                                - Registered Homes (Amendment) Act 1991 \
                                - Children Act 1989 \
                                - Childcare Act 2006 \
                                - Nurses Agencies Act 1957 \
                                - Care Standards Act 2000 \
                                - Health and Social Care Act 2008
serviceType.heading=What type of service are you applying to register?
ChildrenHome.option=Childrens home
targetOpDate.heading=What is the target opening date?
telephone.heading=Phone number
telephone.heading.hint=Without spaces e.g. 07123456789
title.heading=Title
firstName.heading=Forename
surname.heading=Surname
position.heading=Position
management.heading=Will this person have any management responsibility?
contactChildren.heading=Will this person have significant contact with children?
RegHomes1991.option=Registered Homes (Amendment) Act 1991
CareStdAct2000.option=Care Standards Act 2000
RegHomes1984.option=Registered Homes Act 1984
HealthSocialCareAct2008.option=Health and Social Care Act 2008
name.heading=Name
address.heading=Address
address.line1.heading=Address Line 1
address.line2.heading=Address Line 2
address.line3.heading=Address Line 3
address.line4.heading=Address Line 4
address.postcode.heading=Postcode
ofstedRegNo.heading=Ofsted registration number
ofstedRegNo.heading=Ofsted registration number
refusedApplication.inner.refAppName.heading=Name (as stated in the application)
refusedApplication.inner.refAppOfstedRegNo.heading=Ofsted registration number
refusedApplication.inner.refAppOrgID.heading=OrganisationID
refusedApplication.inner.refAppDetails.heading=Please, tell us what happened:
refusedApplication.inner.refAppDetails.heading.hint=For example: \
                                                    - details of any registration held, including information about the type of service provided \
                                                    - the dates of registration \
                                                    - reasons why the registration ceased, if applicable \
                                                    - any other detail that you think is relevant
TRUE.option=Yes
FALSE.option=No
there.is.a.problem=There is a problem
required=This is a required field
day=Day
month=Month
year=Year
LocalAuthority=Local authority
HealthAuthority=Health authority
StatutoryBody=Statutory body
organisationName.heading=What is the name of your organisation?
partnershipName.heading=What is the name of your partnership?
email.heading=Email Address
telephone.heading=Telephone number
creationDate.heading=Date of creation
charityNumber.heading=Do you know the charity number?
companyNumber.heading=Do you know the company number?
subsidiaries.heading=Are there any other subsidiaries?
    """}
  @JSExportTopLevel("back")
  def back(page: String) = journey(Back(page))

  def dummyForm[A]: Form[A] = ???

  def journey(action: Action) = {

    import Cs1Journey._

    val output: ((Either[Page, Cs1Form],DB), List[String]) =
      program[FxAppend[Stack, JsStack]]
        .useForm(inferJsForm[ServiceType])
        .useForm(inferJsForm[ApplicantType])
        .useForm(inferJsForm[Boolean])
        .useForm(inferJsForm[Option[String]])
        .useForm(inferJsForm[Option[RegisteredEstablishmentOrAgency]])
        .useForm(inferJsForm[Set[Act]])    
        .useForm(inferJsForm[String])
        .useForm(inferJsForm[Option[RefusedApplication]])
        .useForm(inferJsForm[java.time.LocalDate])
        .useForm(inferJsForm[OrganisationSector])
        .useForm(inferJsForm[OrganisationType])
        .useForm(inferJsForm[Address])
        .useForm(inferJsForm[Option[HoldingCompany]])
        .useForm(inferJsForm[Option[Individual]])
        .useForm(inferJsForm[Individual])    
        .useForm(dummyForm[Site])
        .useForm(inferJsForm[Establishment])
        .useForm(dummyForm[Option[Option[File]]])
        .useForm(dummyForm[Signature])
        .useForm(inferJsForm[Option[Staff]])
        .useForm(dummyForm[Option[Site]])
        .useForm(dummyForm[File])
        .useForm(inferJsForm[(Int,Int)])
        .useForm(inferJsForm[Int])
        .useForm(inferJsForm[ChildrensHomeType])
        .useForm(inferJsForm[Set[VoluntaryAdoptionType]])
        .useForm(inferJsForm[IndependentFosteringType])
        .useForm(inferJsForm[Set[AdoptionSupportType]])
        .runReader(action)
        .runEither
        .runState(state)
        .runState(List.empty[String])    
        .runEval
        .run

    val ((result,newState),newBreadcrumbs) = output
    breadcrumbs = newBreadcrumbs    
    state = newState

    result match {
      case Left(page) => setPage(page)
      case Right(fin) => scala.scalajs.js.Dynamic.global.alert(fin.toString)
    }
  }

  def updateDataTargets(): Unit = {
    val i = $("""[data-target] > input[type="radio"]""")
    i.change{ e: org.scalajs.dom.Element =>
      
      val radioValue=$(e).value
      val dataTarget=$(e).parent("[data-target]").attr("data-target")
	$(".conditional-" + dataTarget).removeClass("govuk-radios__conditional")
	$(".conditional-" + dataTarget).addClass("govuk-radios__conditional--hidden")
	$("#conditional-" + dataTarget + "-" + radioValue).removeClass("govuk-radios__conditional--hidden")
	$("#conditional-" + dataTarget + "-" + radioValue).addClass("govuk-radios__conditional")	      
    }
    ()
  }

  def setPage(page: Page): Unit = {
    page.title.map { title =>
      breadcrumbs = title :: breadcrumbs
      $("#title").html(messages.span(s"heading.$title"))
      $("#continue-button").replaceWith(
        s"""|<button class="govuk-button" type="submit" id="continue-button"
            |  onclick="saveAndContinue('$title')">
            |    Save and continue
            |</button>""".stripMargin)
    }

    val backlink = { page.breadcrumbs.headOption match {
      case Some(back) =>
        s"""<a href="#" onclick="back('$back');" class="govuk-back-link">${messages.getMessage(List(s"back-to-$back","back"))}</a>"""
      case None => ""
    }}

    page.body.map { x => $("#mainBody").html(backlink ++ x) }    

    page.errors.flatTree match {
      case Nil => $("#error-summary-display").css("display", "none")
      case err =>
        $("#error-summary-display").css("display", "block")
        $("#error-summary-list").html {
          err.map{ msg =>
            s"""|<li role="tooltip">
                |  <a href="#packQty.higher" id="packQty.higher-error-summary" data-focuses="packQty.higher">
                |    $msg
                |  </a>
                |</li>""".stripMargin
          }.mkString("")
        }
    }
    updateDataTargets()
  }

}
