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

import ltbs.uniform.datapipeline.{Messages => _, _}
import InferParser._
import ltbs.uniform.widgets.govuk._
import cs1._

import java.io.File
import java.time.{LocalDate => Date}

@Singleton
class Cs1Controller @Inject() (val controllerComponents: ControllerComponents) extends BaseController with PlayInterpreter with I18nSupport {

  import Cs1Journey._
  def run(key: String) = Action.async { implicit request =>

    def inferForm[A](implicit parser: DataParser[A], html: HtmlForm[A]) = inferWebMonadForm[A](views.html.chrome.apply)

    def dummyForm[A]: WebMonadForm[A] = ???
    
    runWeb(
      program = program[FxAppend[Stack, PlayStack]]
        .useForm(inferForm[ServiceType]				       )
        .useForm(inferForm[ApplicantType]			       )
        .useForm(inferForm[Boolean]				       )
        .useForm(inferForm[Option[String]]			       )
        .useForm(inferForm[Option[RegisteredEstablishmentOrAgency]]     )
        .useForm(inferForm[Set[Act]]				       )
        .useForm(inferForm[String]				       )
        .useForm(inferForm[Option[RefusedApplication]]		       )
        .useForm(inferForm[Date]					       )
        .useForm(inferForm[OrganisationSector]			       )
        .useForm(inferForm[OrganisationType]			       )
        .useForm(inferForm[Address]				       )
        .useForm(inferForm[Option[HoldingCompany]]		       )
        .useForm(inferForm[Option[Individual]]			       )
        .useForm(inferForm[Individual]                                  )
        .useForm(dummyForm[Site])
        .useForm(inferForm[Establishment])
        .useForm(dummyForm[Option[Option[File]]])
        .useForm(dummyForm[Signature])
        .useForm(inferForm[Option[Staff]])
        .useForm(dummyForm[Option[Site]])
        .useForm(dummyForm[File])
        .useForm(inferForm[(Int,Int)])
        .useForm(inferForm[Int])
        .useForm(inferForm[ChildrensHomeType])
        .useForm(inferForm[Set[VoluntaryAdoptionType]])
        .useForm(inferForm[IndependentFosteringType])
        .useForm(inferForm[Set[AdoptionSupportType]])
        ,
      key,
      request,
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
