package controllers.disposal_of_vehicle

import play.api.data.{Form}
import play.api.data.Forms._
import play.api.Logger
import play.api.mvc._
import mappings.disposal_of_vehicle.Dispose._
import mappings.common.Mileage._
import mappings.common.DayMonthYear.dayMonthYear
import mappings.common.Consent._
import constraints.common
import common.DayMonthYear._
import controllers.disposal_of_vehicle.Helpers._
import models.domain.disposal_of_vehicle._
import com.google.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.FormExtensions._
import models.domain.disposal_of_vehicle.DisposeFormModel
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import org.joda.time.format.{ISODateTimeFormat}
import services.dispose_service.DisposeService
import services.DateService
import models.domain.disposal_of_vehicle.DisposeFormModel
import play.api.data.FormError
import scala.Some
import play.api.mvc.SimpleResult
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.domain.disposal_of_vehicle.DisposeModel
import models.domain.disposal_of_vehicle.DisposeViewModel
import mappings.disposal_of_vehicle.Dispose.dateOfDisposalYearsIntoThePast
import scala.language.postfixOps

class Dispose @Inject()(webService: DisposeService, dateService: DateService) extends Controller {
  val disposeForm = Form(
    mapping(
      mileageId -> mileage(),
      dateOfDisposalId -> dayMonthYear.verifying(validDate(),
        after(earliest = dateService.today - dateOfDisposalYearsIntoThePast years),
        notInFuture(dateService)),
      consentId -> consent,
      lossOfRegistrationConsentId -> consent
    )(DisposeFormModel.apply)(DisposeFormModel.unapply)
  )

  private val yearsDropdown: Seq[(String, String)] = dateService.today.yearRangeToDropdown(yearsIntoThePast = dateOfDisposalYearsIntoThePast)

  def present = Action { implicit request => {
      fetchDealerDetailsFromCache match {
        case (Some(dealerDetails)) =>
          Logger.debug("found dealer details")
          // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
          fetchVehicleDetailsFromCache match {
            case (Some(vehicleDetails)) => Ok(views.html.disposal_of_vehicle.dispose(populateModelFromCachedData(dealerDetails, vehicleDetails), disposeForm, yearsDropdown))
            case _ => Redirect(routes.VehicleLookup.present)
          }
        case _ => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  def submit = Action.async { implicit request =>
    Logger.debug("Submitted dispose form...")
    disposeForm.bindFromRequest.fold(
      formWithErrors =>
        Future {
          (fetchDealerDetailsFromCache, fetchVehicleDetailsFromCache) match {
            case (Some(dealerDetails), Some(vehicleDetails)) =>
              val disposeViewModel = populateModelFromCachedData(dealerDetails, vehicleDetails)
              // When the user doesn't select a value from the drop-down then the mapping will fail to match on an Int before
              // it gets to the constraints, so we need to replace the error type with one that will give a relevant message.
              val formWithReplacedErrors = formWithErrors.
                replaceError("dateOfDisposal.day", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError("dateOfDisposal.month", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError("dateOfDisposal.year", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError("dateOfDisposal", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError(consentId, "error.required", FormError(key = consentId, message = "disposal_dispose.consent.notgiven", args = Seq.empty)).
                replaceError(lossOfRegistrationConsentId, "error.required", FormError(key = lossOfRegistrationConsentId, message = "disposal_dispose.loss_of_registration.consent.notgiven", args = Seq.empty)).
                distinctErrors
              BadRequest(views.html.disposal_of_vehicle.dispose(disposeViewModel, formWithReplacedErrors, yearsDropdown))
            case _ =>
              Logger.debug("could not find expected data in cache on dispose submit - now redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
          }
        },
      f => {
        storeDisposeFormModelInCache(f)
        Logger.debug(s"Dispose form submitted - mileage = ${f.mileage}, disposalDate = ${f.dateOfDisposal}")
        disposeAction(webService, f)
      }
    )
  }

  private def populateModelFromCachedData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeViewModel = {
    val model = DisposeViewModel(
      registrationNumber = vehicleDetails.registrationNumber,
      vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
    Logger.debug(s"Dispose page read the following data from cache: $model")
    model
  }

  private def disposeAction(webService: DisposeService, f: DisposeFormModel): Future[SimpleResult] = {
    fetchVehicleLookupDetailsFromCache match {
      case Some(vehicleLookupFormModel) =>
        val disposeModel = DisposeModel(referenceNumber = vehicleLookupFormModel.referenceNumber,registrationNumber = vehicleLookupFormModel.registrationNumber, 
          dateOfDisposal = f.dateOfDisposal, mileage = f.mileage)
        storeDisposeModelInCache(disposeModel)
        webService.invoke(buildDisposeMicroServiceRequest(disposeModel)).map {
          resp => Logger.debug(s"Dispose micro service call successful - response = $resp")
          storeDisposeTransactionIdInCache(resp.transactionId)
          if (resp.success) {
            storeDisposeRegistrationNumberInCache(resp.registrationNumber)
            Redirect(routes.DisposeSuccess.present)
          }
          else {
            handleMicroServiceFailure(resp)
          }
        }.recover {
          case e: Throwable =>
            Logger.warn(s"Dispose micro service call failed. Exception: $e")
            Redirect(routes.MicroServiceError.present)
        }
      case _ => Future {
        Logger.error("could not find dealer details in cache on Dispose submit")
        Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  private def handleMicroServiceFailure(resp: DisposeResponse) = {
    val disposeEndpointDown = "ms.dispose.response.endpointdown"
    resp.responseCode match {
      case Some(responseCode) if responseCode == disposeEndpointDown =>
        Logger.warn("Dispose soap endpoint down redirecting to error page...")
        Redirect(routes.MicroServiceError.present)
      case Some(responseCode) => ??? // TODO define what we do in this situation
      case None => Redirect(routes.DisposeFailure.present)
    }
  }

  private def buildDisposeMicroServiceRequest(disposeModel: DisposeModel):DisposeRequest = {
    val dateTime = disposeModel.dateOfDisposal.toDateTime.get
    val formatter = ISODateTimeFormat.dateTime()
    val isoDateTimeString = formatter.print(dateTime)
    DisposeRequest(referenceNumber = disposeModel.referenceNumber, registrationNumber = disposeModel.registrationNumber, dateOfDisposal = isoDateTimeString, mileage = disposeModel.mileage)
  }
}