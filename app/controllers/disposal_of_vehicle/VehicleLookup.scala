package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import mappings.common.{ReferenceNumber, RegistrationNumber}
import ReferenceNumber._
import RegistrationNumber._
import mappings.disposal_of_vehicle.VehicleLookup._
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import com.google.inject.Inject
import services.vehicle_lookup.VehicleLookupService
import utils.helpers.FormExtensions._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import play.api.data.FormError
import play.api.mvc.SimpleResult
import common.{ClientSideSessionFactory, EncryptedCookieImplicits}
import EncryptedCookieImplicits.RequestAdapter
import EncryptedCookieImplicits.SimpleResultAdapter
import EncryptedCookieImplicits.FormAdapter
import services.brute_force_prevention.BruteForceService

final class VehicleLookup @Inject()(bruteForceService: BruteForceService, vehicleLookupService: VehicleLookupService)(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  val vehicleLookupForm = Form(
    mapping(
      ReferenceNumberId -> referenceNumber,
      RegistrationNumberId -> registrationNumber
    )(VehicleLookupFormModel.apply)(VehicleLookupFormModel.unapply)
  )

  def present = Action {
    implicit request =>
      request.getEncryptedCookie[TraderDetailsModel] match {
        case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, vehicleLookupForm.fill()))
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def submit = Action.async {
    implicit request =>
      vehicleLookupForm.bindFromRequest.fold(
        formWithErrors =>
          Future {
            request.getEncryptedCookie[TraderDetailsModel] match {
              case Some(dealerDetails) => val formWithReplacedErrors = formWithErrors.
                replaceError(RegistrationNumberId, FormError(key = RegistrationNumberId, message = "error.restricted.validVRNOnly", args = Seq.empty)).
                replaceError(ReferenceNumberId, FormError(key = ReferenceNumberId, message = "error.validDocumentReferenceNumber", args = Seq.empty)).
                distinctErrors
                BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithReplacedErrors))
              case None => Redirect(routes.SetUpTradeDetails.present())
            }
          },
        f => {
          val modelWithoutSpaces = f.copy(registrationNumber = f.registrationNumber.replace(" ", "")) // DE7 Strip spaces from input as it is not allowed in the micro-service.
          lookupVehicle(modelWithoutSpaces)
        }
      )
  }

  def back = Action {
    implicit request =>
      request.getEncryptedCookie[TraderDetailsModel] match {
        case Some(dealerDetails) =>
          if (dealerDetails.traderAddress.uprn.isDefined) Redirect(routes.BusinessChooseYourAddress.present())
          else Redirect(routes.EnterAddressManually.present())
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  private def lookupVehicle(model: VehicleLookupFormModel)(implicit request: Request[_]): Future[SimpleResult] =
    bruteForceService.vrmLookupPermitted(model.registrationNumber).map {
      permitted => permitted
    } flatMap { permitted =>
      if (permitted) {
        vehicleLookupService.invoke(buildMicroServiceRequest(model)).map {
          case (responseStatus: Int, response: Option[VehicleDetailsResponse]) =>
            Logger.debug(s"VehicleLookup Web service call successful - response = $response")
            checkResponseConstruction(responseStatus, response).
              withEncryptedCookie(model)
        }.recover {
          case exception: Throwable => throwToMicroServiceError(exception)
        }
      }
      else {
        Future {
          Logger.warn(s"BruteForceService locked out vrm: ${model.registrationNumber}")
          Redirect(routes.MicroServiceError.present())
        }
      }
    } recover {
      case exception: Throwable => {
        Logger.error("Failed to talk to BruteForceService so for safety we won't let anyone through")
        Redirect(routes.MicroServiceError.present())
      }
    }

  private def checkResponseConstruction(responseStatus: Int, response: Option[VehicleDetailsResponse])(implicit request: Request[_]) = {
    responseStatus match {
      case OK => okResponseConstruction(response)
      case _ => Redirect(routes.VehicleLookupFailure.present())
    }
  }

  private def okResponseConstruction(vehicleDetailsResponse: Option[VehicleDetailsResponse])(implicit request: Request[_]) = {
    vehicleDetailsResponse match {
      case Some(response) => responseCodePresent(response)
      case _ => Redirect(routes.MicroServiceError.present()) // TODO write test to achieve code coverage.
    }
  }

  private def responseCodePresent(response: VehicleDetailsResponse)(implicit request: Request[_]) = {
    response.responseCode match {
      case Some(responseCode) =>
        Redirect(routes.VehicleLookupFailure.present()).
          withEncryptedCookie(key = VehicleLookupResponseCodeCacheKey, value = responseCode)
      case None => noResponseCodePresent(response.vehicleDetailsDto)
    }
  }

  private def noResponseCodePresent(vehicleDetailsDto: Option[VehicleDetailsDto])(implicit request: Request[_]) = {
    vehicleDetailsDto match {
      case Some(dto) =>
        Redirect(routes.Dispose.present()).
          withEncryptedCookie(VehicleDetailsModel.fromDto(dto))
      case None => Redirect(routes.MicroServiceError.present())
    }
  }

  private def buildMicroServiceRequest(formModel: VehicleLookupFormModel): VehicleDetailsRequest = {
    VehicleDetailsRequest(referenceNumber = formModel.referenceNumber, registrationNumber = formModel.registrationNumber)
  }

  private def throwToMicroServiceError(exception: Throwable) = {
    Logger.debug(s"Web service call failed. Exception: $exception")
    BadRequest("The remote server didn't like the request.")
    Redirect(routes.MicroServiceError.present())
  }
}
