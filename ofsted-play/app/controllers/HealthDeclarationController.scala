package controllers

import ltbs.uniform.interpreters.playframework._
import cats.kernel.Monoid
import javax.inject._
import play.api._
import play.api.data._, Forms._
import play.api.mvc._
import play.twirl.api.Html

import org.atnos.eff._
import cats.data._
import cats.implicits._

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global
import cats.data.Validated

import play.api.i18n._
import ofsted._

import ltbs.uniform.datapipeline.{Messages => _, _}
import InferParser._
import ltbs.uniform.widgets.govuk._

@Singleton
class HealthDeclarationController @Inject() (val controllerComponents: ControllerComponents) extends BaseController with PlayInterpreter with I18nSupport {

  import HealthDeclaration._

  def run(key: String) = Action.async { implicit request =>

    def inferForm[A](implicit parser: DataParser[A], html: HtmlForm[A]) = inferWebMonadForm[A](views.html.chrome.apply)

    runWeb(
      program = uniform[FxAppend[Stack, PlayStack]]
        .useForm(inferForm[String])
        .useForm(inferForm[Gender])
        .useForm(inferForm[Set[HealthCondition]])
        .useForm(inferForm[java.time.LocalDate])
        .useForm(inferForm[Address])
        .useForm(inferForm[Option[String]])
        .useForm(inferForm[Int])
        .useForm(inferForm[Boolean])
        .useForm(inferForm[Name])
        .useForm(inferForm[BirthPlace]),
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
