package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import mappings.common.MultiLineAddress
import mappings.common.PostCode
import mappings.common.MultiLineAddress._
import mappings.common.PostCode._
import models.domain.disposal_of_vehicle.EnterAddressManuallyModel
import constraints.MultilineAddress.requiredAddress
import play.api.data.Forms._


object EnterAddressManually extends Controller {
  val form = Form(
    mapping(
      MultiLineAddress.id -> address.verifying(requiredAddress),
      PostCode.key -> postcode()
    )(EnterAddressManuallyModel.apply)(EnterAddressManuallyModel.unapply)
  )

  def present = Action {
    implicit request => Ok("hello")
  }

  def submit = Action {
    implicit request => ???
  }
}