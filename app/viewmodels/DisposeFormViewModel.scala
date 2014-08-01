package viewmodels

import mappings.common.Consent.consent
import play.api.data.Mapping
import play.api.libs.json.Json
import uk.gov.dvla.vehicles.presentation.common
import common.mappings.DayMonthYear.dayMonthYear
import common.mappings.Mileage.mileage
import common.views.constraints.DayMonthYear.{after, notInFuture, validDate}
import common.clientsidesession.CacheKey
import common.services.DateService
import common.views.models.DayMonthYear

final case class DisposeFormViewModel(mileage: Option[Int],
                                  dateOfDisposal: DayMonthYear,
                                  consent: String,
                                  lossOfRegistrationConsent: String)

object DisposeFormViewModel {
  implicit val JsonFormat = Json.format[DisposeFormViewModel]
  final val DisposeFormModelCacheKey = "disposeForm"
  implicit val Key = CacheKey[DisposeFormViewModel](value = DisposeFormModelCacheKey)
  final val DisposeOccurredCacheKey = "disposeOccurredCacheKey"
  final val PreventGoingToDisposePageCacheKey = "preventGoingToDisposePage"
  final val DisposeFormTransactionIdCacheKey = "disposeFormTransactionId"
  final val DisposeFormTimestampIdCacheKey = "disposeFormTimestampId"
  final val DisposeFormRegistrationNumberCacheKey = "disposeFormRegistrationNumber"
  final val SurveyRequestTriggerDateCacheKey = "surveyRequestTriggerDate"

  object Form {
    final val MileageId = "mileage"
    final val DateOfDisposalId = "dateOfDisposal"
    final val ConsentId = "consent"
    final val LossOfRegistrationConsentId = "lossOfRegistrationConsent"
    final val DateOfDisposalYearsIntoThePast = 2
    final val TodaysDateOfDisposal = "todaysDateOfDisposal"
    final val BackId = "back"
    final val SubmitId = "submit"

    def mapping(dateService: DateService): Mapping[DisposeFormViewModel] =
      play.api.data.Forms.mapping(
        MileageId -> mileage(),
        DateOfDisposalId -> dayMonthYear.verifying(validDate(),
          after(earliest = (dateService.today - DateOfDisposalYearsIntoThePast).years),
          notInFuture(dateService)),
        ConsentId -> consent,
        LossOfRegistrationConsentId -> consent
      )(DisposeFormViewModel.apply)(DisposeFormViewModel.unapply)
  }
}
