package controllers

import ltbs.uniform.interpreters.playframework._
import cats.kernel.Monoid
import javax.inject._
import play.api._
import play.api.mvc._

import org.atnos.eff._
import cats.implicits._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.i18n._
import ofsted._

import ltbs.uniform.web.{Messages => _, _}, parser._
import ltbs.uniform.ErrorTree
import play.twirl.api.Html
import InferParser._
import ltbs.uniform.widgets.govuk._
import cs1._

import java.io.File
import java.time.{LocalDate => Date}

@Singleton
class Cs1Controller @Inject() (val controllerComponents: ControllerComponents) extends BaseController with PlayInterpreter with I18nSupport {

  def messages(request: Request[AnyContent]): ltbs.uniform.web.Messages = convertMessages(messagesApi.preferred(request))

  def renderForm(key: String, errors: ErrorTree, form: Html, breadcrumbs: List[String], request: Request[AnyContent], messagesIn: ltbs.uniform.web.Messages): Html = {
    views.html.chrome(key, errors, form, breadcrumbs)(messagesIn, request)
  }

  import Cs1Journey._
  def run(implicit key: String) = Action.async { implicit request =>

    def dummyForm[A]: PlayForm[A] = new PlayForm[A] {
      def decode(out: Encoded): Either[ErrorTree,A] = ???
      def encode(in: A): Encoded = ???
      def receiveInput(data: Request[AnyContent]): Encoded = ???
      def render(key: String, existing: Option[Encoded], data: Request[AnyContent], errors: ErrorTree): Html = ???
    }
    
    runWeb(
      program = program[FxAppend[Stack, PlayStack]]
        .useForm(PlayForm.automatic[ServiceType])
        .useForm(PlayForm.automatic[ApplicantType])
        .useForm(PlayForm.automatic[Boolean])
        .useForm(PlayForm.automatic[Option[String]])
        .useForm(PlayForm.automatic[Option[RegisteredEstablishmentOrAgency]])
        .useForm(PlayForm.automatic[Set[Act]])
        .useForm(PlayForm.automatic[String])
        .useForm(PlayForm.automatic[Option[RefusedApplication]])
        .useForm(PlayForm.automatic[Date])
        .useForm(PlayForm.automatic[OrganisationSector])
        .useForm(PlayForm.automatic[OrganisationType])
        .useForm(PlayForm.automatic[Address])
        .useForm(PlayForm.automatic[Option[HoldingCompany]])
        .useForm(PlayForm.automatic[Option[Individual]])
        .useForm(PlayForm.automatic[Individual])
        .useForm(dummyForm[Site])
        .useForm(PlayForm.automatic[Establishment])
        .useForm(dummyForm[Option[Option[File]]])
        .useForm(dummyForm[Signature])
        .useForm(PlayForm.automatic[Option[Staff]])
        .useForm(dummyForm[Option[Site]])
        .useForm(dummyForm[File])
        .useForm(PlayForm.automatic[(Int,Int)])
        .useForm(PlayForm.automatic[Int])
        .useForm(PlayForm.automatic[ChildrensHomeType])
        .useForm(PlayForm.automatic[Set[VoluntaryAdoptionType]])
        .useForm(PlayForm.automatic[IndependentFosteringType])
        .useForm(PlayForm.automatic[Set[AdoptionSupportType]])
        ,
      persistence
    )(
      a => Future.successful(Ok(a.toString))
    )
  }

  val persistence = new Persistence {
    private var data: DB = Monoid[DB].empty
    def dataGet: Future[DB] = Future.successful(data)
    def dataPut(dataIn: DB): Future[Unit] =
      Future(data = dataIn).map{_ => ()}
  }

}
