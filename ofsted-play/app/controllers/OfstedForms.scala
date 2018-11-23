package controllers

import ltbs.uniform.interpreters.playframework._
import enumeratum._
import play.api.i18n.Messages

import cats.data.Validated
import cats.implicits._
import play.twirl.api.Html
import play.api.data._, Forms._
import play.api.mvc._
import java.time.{LocalDate => Date}
import ofsted._
import scala.util.Try

import upickle.default._

case class OfstedForms()(implicit messages: Messages) {

  val stringForm = new WebMonadForm[String] {
    def decode(out: Encoded): String = out
    def encode(in: String): Encoded = in
    def playForm(key: String, validation: String => Validated[ValidationError,String]): Form[String] = Form(single(key -> nonEmptyText))
    def render(key: String, existing: ValidatedData[String], request: Request[AnyContent]): Html = {

      val form = existing match {
        case Some(Validated.Invalid(e)) => Form(single(key -> nonEmptyText)).withError("", e)
        case _ => Form(single(key -> nonEmptyText))
      }

      implicit val r: Request[AnyContent] = request
      views.html.string(key, form)
    }
  }

  def enumSelectPage[A <: EnumEntry](e: Enum[A]) = {

    new WebMonadSelectPage[A] {
      def decode(out: Encoded): A = e.withName(out)
      def encode(in: A): Encoded = in.entryName

      def renderOne(
        key: String,
        options: Set[A],
        existing: ValidatedData[A],
        request: Request[AnyContent]
      ): Html = {
        implicit val r: Request[AnyContent] = request
        views.html.radios(
          key,
          options.map(encode),
          None,
          Form(single(key -> nonEmptyText.transform(decode, encode)))
        )
      }

      def renderMany(
        key: String,
        options: Set[A],
        existing: ValidatedData[Set[A]],
        request: Request[AnyContent]
      ): Html = {
        implicit val r: Request[AnyContent] = request
        views.html.checkboxes(
          key,
          options.map(encode),
          None,
          playFormMany(key, _.valid)
        )
      }

      def toHtml(in: A): Html = Html(messages(in.entryName))
      def playFormOne(key: String, validation: A => Validated[ValidationError,A]): Form[A] =
        Form(single(key -> nonEmptyText.transform(decode, encode)))

      def playFormMany(key: String,validation: Set[A] => Validated[ValidationError,Set[A]]): Form[Set[A]] = {
        def batchDecode(input: List[Encoded]): Set[A] = input.map(decode).toSet
        def batchEncode(input: Set[A]): List[Encoded] = input.map(encode).toList
        Form(single(key -> list(nonEmptyText).transform(batchDecode, batchEncode)))
      }

    }
  }

  def timePit[A]: WebMonadForm[A] = new WebMonadForm[A] {
    def decode(out: Encoded): A = ???
    def encode(in: A): Encoded = ???
    def playForm(key: String, validation: A => Validated[ValidationError,A]): Form[A] = ???
    def render(key: String, existing: ValidatedData[A], request: Request[AnyContent]): Html = ???
  }

  def dateForm: WebMonadForm[Date] = new WebMonadForm[Date] {

    val dateMap: Mapping[Date] = tuple(
      "day" -> number.verifying("error.start-day.invalid", d => d > 0 && d <= 31),
      "month" -> number.verifying("error.start-month.invalid", d => d > 0 && d <= 12),
      "year" -> number.verifying("error.start-year.invalid", d => d >= 1900 && d < 2100)
    ).verifying("error.date.invalid", x => x match {
                  case (d, m, y) => Try(Date.of(y, m, d)).isSuccess
                })
      .transform({ case (d, m, y) => Date.of(y, m, d) }, d => (d.getDayOfMonth, d.getMonthValue, d.getYear))

    def decode(out: Encoded): Date = Date.parse(out)
    def encode(in: Date): Encoded = in.toString
    def playForm(key: String, validation: Date => Validated[ValidationError,Date]): Form[Date] = Form(dateMap)
    def render(key: String, existing: ValidatedData[Date], request: Request[AnyContent]): Html = {

      val form = existing match {
        case Some(Validated.Invalid(e)) => Form(dateMap).withError("", e)
        case _ => Form(dateMap)
      }

      implicit val r: Request[AnyContent] = request
      views.html.date(key, form)
    }
  }


  def addressForm: WebMonadForm[Address] = new WebMonadForm[Address] {

    implicit val rw: ReadWriter[Address] = macroRW

    val addressMap: Mapping[Address] = mapping(
      "line1" -> nonEmptyText, 
      "line2" -> text,
      "line3" -> text,
      "line4" -> text,
      "postCode" -> nonEmptyText
    )(Address.apply)(Address.unapply)

    def decode(out: Encoded): Address = read[Address](out)
    def encode(in: Address): Encoded = write(in)
    def playForm(key: String, validation: Address => Validated[ValidationError,Address]): Form[Address] = Form(addressMap)
    def render(key: String, existing: ValidatedData[Address], request: Request[AnyContent]): Html = {

      val form = existing match {
        case Some(Validated.Invalid(e)) => Form(addressMap).withError("", e)
        case _ => Form(addressMap)
      }

      implicit val r: Request[AnyContent] = request
      views.html.address(key, form)
    }
  }

  def optStringForm: WebMonadForm[Option[String]] = new WebMonadForm[Option[String]] {
    def decode(out: Encoded): Option[String] = Some(out).filter(_.nonEmpty)
    def encode(in: Option[String]): Encoded = in.getOrElse("")
    def playForm(key: String, validation: Option[String] => Validated[ValidationError,Option[String]]): Form[Option[String]] =
      Form(single(key -> text.transform(decode, encode)))

    def render(key: String, existing: ValidatedData[Option[String]], request: Request[AnyContent]): Html = {

      val form = existing match {
        case Some(Validated.Invalid(e)) => Form(single(key -> text)).withError("", e)
        case _ => Form(single(key -> text))
      }

      implicit val r: Request[AnyContent] = request
      views.html.string(key, form)
    }
  }

  def intForm: WebMonadForm[Int] = new WebMonadForm[Int] {

    def intMapping(key: String) = single(key -> number)

    def decode(out: Encoded): Int = out.toInt
    def encode(in: Int): Encoded = in.toString
    def playForm(key: String, validation: Int => Validated[ValidationError,Int]): Form[Int] = Form(intMapping(key))
    def render(key: String, existing: ValidatedData[Int], request: Request[AnyContent]): Html = {

      val form = existing match {
        case Some(Validated.Invalid(e)) => Form(intMapping(key)).withError("", e)
        case _ => Form(intMapping(key))
      }

      implicit val r: Request[AnyContent] = request
      views.html.int(key, form)
    }
  }
  
  def booleanForm: WebMonadForm[Boolean] = new WebMonadForm[Boolean] {

    def decode(out: Encoded): Boolean = out.toUpperCase == "TRUE"
    def encode(in: Boolean): Encoded = if(in) "TRUE" else "FALSE"

    def playForm(key: String, validation: Boolean => Validated[ValidationError,Boolean]): Form[Boolean] =
      Form(single(key -> nonEmptyText.transform(decode, encode)))

    def render(key: String, existing: ValidatedData[Boolean], request: Request[AnyContent]): Html = {
      implicit val r: Request[AnyContent] = request
      views.html.radios(
        key,
        Set("TRUE", "FALSE"),
        None,
        Form(single(key -> nonEmptyText.transform(decode, encode)))
      )
    }

  }

}
