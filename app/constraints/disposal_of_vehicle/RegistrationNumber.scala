package constraints.disposal_of_vehicle

import play.api.data.validation.{ValidationError, Invalid, Valid, Constraint}

object RegistrationNumber {
  def validRegistrationNumber: Constraint[String] = Constraint("constraint.restrictedvalidVRN") { input =>
    val whitelist =
      """^
        |([A-Za-z]{3}[0-9]{1,4})|
        |([A-Za-z][0-9]{1,3}[A-Za-z]{3})|
        |([A-Za-z]{3}[0-9]{1,3}[A-Za-z])|
        |([A-Za-z]{2}[0-9]{2}[A-Za-z]{3})|
        |([A-Za-z]{1,3}[0-9]{1,3})|
        |([0-9]{1,4}[A-Za-z]{1,3})|
        |([A-Za-z]{1,2}[0-9]{1,4})$""".stripMargin.replace("\n", "").r

    if(whitelist.pattern.matcher(input.replace(" ","")).matches) Valid
    else Invalid(ValidationError("error.restricted.validVRNOnly"))
  }
}
