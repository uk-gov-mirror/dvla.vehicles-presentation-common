package uk.gov.dvla.vehicles.presentation.common.views.constraints

import play.api.data.validation.Constraint
import play.api.data.validation.Constraints.pattern

object Postcode {

  def validPostcode: Constraint[String] = pattern(
    regex =
      """^
        |(?i)(GIR 0AA)|
        |((([A-Z][0-9][0-9]?)|
        |(([A-Z][A-HJ-Y][0-9][0-9]?)|
        |(([A-Z][0-9][A-Z])|
        |([A-Z][A-HJ-Y][0-9]?[A-Z]))))[ ]?[0-9][A-Z]{2})
        |$""".stripMargin.replace("\n", "").r,
    name = "constraint.restricted.validPostcode",
    error = "error.restricted.validPostcode")

  // TODO I think this should move out of the constraint as it is not a constraint, it re-formats for viewing. It could live in a model but we don't yet have a model for postcode
  def formatPostcode(postcode: String): String = {
    val SpaceCharDelimiter = " "
    val A99AA = "([A-Z][0-9]{2}[A-Z]{2})".r
    val A099AA = "([A-Z][0][0-9]{2}[A-Z]{2})".r
    val A999AA = "([A-Z][0-9]{3}[A-Z]{2})".r
    val A9A9AA = "([A-Z][0-9][A-Z][0-9][A-Z]{2})".r
    val AA99AA = "([A-Z]{2}[0-9]{2}[A-Z]{2})".r
    val AA099AA = "([A-Z]{2}[0][0-9]{2}[A-Z]{2})".r
    val AA999AA = "([A-Z]{2}[0-9]{3}[A-Z]{2})".r
    val AA9A9AA = "([A-Z]{2}[0-9][A-Z][0-9][A-Z]{2})".r

    postcode.toUpperCase.replace(SpaceCharDelimiter, "") match {
      case A99AA(p) => p.substring(0, 2) + SpaceCharDelimiter + p.substring(2, 5)
      case A099AA(p) => p.substring(0, 1) + p.substring(2, 3) + SpaceCharDelimiter + p.substring(3, 6)
      case A999AA(p) => p.substring(0, 3) + SpaceCharDelimiter + p.substring(3, 6)
      case A9A9AA(p) => p.substring(0, 3) + SpaceCharDelimiter + p.substring(3, 6)
      case AA99AA(p) => p.substring(0, 3) + SpaceCharDelimiter + p.substring(3, 6)
      case AA099AA(p) => p.substring(0, 2) + p.substring(3, 4) + SpaceCharDelimiter + p.substring(4, 7)
      case AA999AA(p) => p.substring(0, 4) + SpaceCharDelimiter + p.substring(4, 7)
      case AA9A9AA(p) => p.substring(0, 4) + SpaceCharDelimiter + p.substring(4, 7)
      case _ => postcode
    }
  }
}
