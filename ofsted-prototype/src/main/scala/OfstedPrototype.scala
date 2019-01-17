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
import ltbs.uniform.web._, parser._
import InferParser._

import ofsted._
import cs1._

import scala.util.Random.nextInt

object OfstedPrototype {

  @JSExportTopLevel("enableMessages")
  def enableMessages(): Unit = {
    val myPromise = $.ajax(
      s"https://raw.githubusercontent.com/VictorRogerAndres/ofsted/master/SC1_titles.txt?count=${nextInt.toString}")

    myPromise.done((data: Any, textStatus: String, jqXHr: JQueryXHR) => {
      cmsMessages = BestGuessMessages(CmsMessages.fromText{data.toString})
    })
  }

  @JSExportTopLevel("disableMessages")
  def disableMessages(): Unit = {
    cmsMessages = NoopMessages
  }

  @JSExportTopLevel("reloadMessages")
  def reloadMessages(): Unit = {
    val myPromise = $.ajax(
      s"https://raw.githubusercontent.com/VictorRogerAndres/ofsted/master/SC1_titles.txt?count=${nextInt.toString}")

    myPromise.done((data: Any, textStatus: String, jqXHr: JQueryXHR) => {
      cmsMessages = BestGuessMessages(CmsMessages.fromText{data.toString})
    })
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

  var state: DB = implicitly[Monoid[DB]].empty
  var breadcrumbs: List[String] = Nil

  implicit var cmsMessages: Messages = NoopMessages

  def main(args: Array[String]): Unit = {
    val myPromise = $.ajax(
      s"https://raw.githubusercontent.com/VictorRogerAndres/ofsted/master/SC1_titles.txt?count=${nextInt.toString}")

    myPromise.done((data: Any, textStatus: String, jqXHr: JQueryXHR) => {
      cmsMessages = BestGuessMessages(CmsMessages.fromText{data.toString})
      journey(Back(""))
    })
    ()
  }

  @JSExportTopLevel("back")
  def back(page: String) = journey(Back(page))

  def dummyForm[A]: Form[A] = ???

  def journey(implicit action: Action) = {

    import Cs1Journey._

    val (result,(newState,newBreadcrumbs)) =
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
        .useForm(inferJsForm[HoldingCompany])
        .useForm(inferJsForm[Option[Individual]])
        .useForm(inferJsForm[Individual])    
        .useForm(inferJsForm[Site])
        .useForm(inferJsForm[Establishment])
        .useForm(inferJsForm[Option[Option[File]]])
        .useForm(inferJsForm[Signature])
        .useForm(inferJsForm[Option[Staff]])
        .useForm(inferJsForm[File])
        .useForm(inferJsForm[(Int,Int)])
        .useForm(inferJsForm[(Int,Int,Boolean)])    
        .useForm(inferJsForm[Int])
        .useForm(inferJsForm[ChildrensHomeType])
        .useForm(inferJsForm[Set[VoluntaryAdoptionType]])
        .useForm(inferJsForm[IndependentFosteringType])
        .useForm(inferJsForm[Set[AdoptionSupportType]])
        .useForm(inferJsForm[AdoptionSupportingAgencyServices])
        .useForm(inferJsForm[MaxChildrenPerCategory])
        .runEither
        .runState((state, List.empty[String]))
        .run

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
      $("#title").html(s"heading.$title")
      $("#continue-button").replaceWith(
        s"""|<button class="govuk-button" type="submit" id="continue-button"
            |  onclick="saveAndContinue('$title')">
            |    Save and continue
            |</button>""".stripMargin)
    }

    val backlink = { page.breadcrumbs.headOption match {
      case Some(back) =>
        s"""<a href="#" onclick="back('$back');" class="govuk-back-link">Back</a>"""
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
