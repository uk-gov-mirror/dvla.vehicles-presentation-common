package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{VehicleDetailsModel, DealerDetailsModel, DisposeConfirmationFormModel, DisposeModel}
import mappings.disposal_of_vehicle.DisposeSuccess._
import controllers.disposal_of_vehicle.Helpers._
import play.api.Logger

object DisposeSuccess extends Controller {

  val disposeSuccessForm = Form(
    mapping(
      emailAddressId -> text
    )(DisposeConfirmationFormModel.apply)(DisposeConfirmationFormModel.unapply)
  )

  def present = Action {
    implicit request => {
      (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) =>
          val disposeModel = fetchData(dealerDetails, vehicleDetails)
          Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeSuccessForm, disposeFormModel))
        case _ => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  def submit = Action {
    implicit request => {
      disposeSuccessForm.bindFromRequest.fold(
        formWithErrors => {
          (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache)  match {
            case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) =>
              val disposeModel = fetchData(dealerDetails, vehicleDetails)
              BadRequest(views.html.disposal_of_vehicle.dispose_success(disposeModel, formWithErrors, disposeFormModel))
            case _ => Redirect(routes.SetUpTradeDetails.present)
          }
        },
        f => {Logger.debug(s"Form submitted email address = <<${f.emailAddress}>>"); Ok("success")}
      )
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeModel  = {
    DisposeModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      keeperName = vehicleDetails.keeperName,
      keeperAddress = vehicleDetails.keeperAddress,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
  }
}