package uk.gov.dvla.vehicles.presentation.common.controllers

import play.api.libs.json.Reads
import play.api.Logger
import play.api.mvc.{Action, Controller, DiscardingCookie, Request, Result}
import uk.gov.dvla.vehicles.presentation.common
import common.clientsidesession.CacheKey
import common.clientsidesession.ClientSideSessionFactory
import common.clientsidesession.CookieImplicits.RichCookies
import common.model.BruteForcePreventionModel
import common.model.CacheKeyPrefix

abstract class VehicleLookupFailureBase[FormModel <: VehicleLookupFormModelBase]
  (implicit clientSideSessionFactory: ClientSideSessionFactory,
   fromJson: Reads[FormModel],
   cacheKey: CacheKey[FormModel],
   cacheKeyPrefix: CacheKeyPrefix
  ) extends Controller {

  protected def presentResult(model: FormModel, responseCode: String)(implicit request: Request[_]): Result
  protected def missingPresentCookieDataResult()(implicit request: Request[_]): Result
  protected def submitResult()(implicit request: Request[_]): Result
  protected def missingSubmitCookieDataResult()(implicit request: Request[_]): Result
  protected val vehicleLookupResponseCodeCacheKey: String

  def present = Action { implicit request =>
    implicit val bruteForceCacheKey = BruteForcePreventionModel.key

    (request.cookies.getModel[BruteForcePreventionModel],
      request.cookies.getModel[FormModel],
      request.cookies.getString(vehicleLookupResponseCodeCacheKey)
      ) match {
      case (Some(bruteForcePreventionResponse),
            Some(vehicleLookUpFormModelDetails),
            Some(vehicleLookupResponseCode)) =>
        val responseCode = vehicleLookupResponseCode.split("-").map(_.trim)
        presentResult(vehicleLookUpFormModelDetails, responseCode.last).
          discardingCookies(DiscardingCookie(name = vehicleLookupResponseCodeCacheKey))
      case _ =>
        Logger.debug(s"VehicleLookupFailure present could not find all the cookie data. " +
          s"A redirection will now occur with tracking id: ${request.cookies.trackingId()}")
        missingPresentCookieDataResult()
    }
  }

  def submit = Action { implicit request =>
    request.cookies.getModel[FormModel] match {
      case Some(vehicleLookUpFormModelDetails) =>
        Logger.debug(s"VehicleLookupFailure submit successfully found " +
          s"cookie data with tracking id: ${request.cookies.trackingId()}")
        submitResult()
      case _ =>
        Logger.debug(s"VehicleLookupFailure submit could not find all the cookie data. " +
          s"A redirection will now occur with tracking id: ${request.cookies.trackingId()}")
        missingSubmitCookieDataResult()
    }
  }
}
