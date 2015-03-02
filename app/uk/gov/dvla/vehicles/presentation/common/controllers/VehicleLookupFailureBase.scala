package uk.gov.dvla.vehicles.presentation.common.controllers

import play.api.Logger
import play.api.libs.json.Reads
import play.api.mvc.{Result, Action, Controller, DiscardingCookie, Request}
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
  protected def foundSubmitCookieDataResult()(implicit request: Request[_]): Result
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
        println(s"present - YAY all the cookie data found")
        println(s"present - bruteForcePreventionResponse = $bruteForcePreventionResponse")
        println(s"present - vehicleLookUpFormModelDetails = $vehicleLookUpFormModelDetails")
        println(s"present - vehicleLookupResponseCode = $vehicleLookupResponseCode")
        val responseCode = vehicleLookupResponseCode.split("-").map(_.trim)
        println(s"present - vehicleLookupResponseCookie: ${responseCode.last}")
        println(s"present - discarding cookie $vehicleLookupResponseCodeCacheKey")
        presentResult(vehicleLookUpFormModelDetails, responseCode.last).
          discardingCookies(DiscardingCookie(name = vehicleLookupResponseCodeCacheKey))
      case _ =>
        Logger.debug("VehicleLookupFailure present could not find all the cookie data. A redirection will now occur")
        missingPresentCookieDataResult()
    }
  }

  def submit = Action { implicit request =>
    request.cookies.getModel[FormModel] match {
      case Some(vehicleLookUpFormModelDetails) =>
        Logger.debug("VehicleLookupFailure submit successfully found cookie data")
        foundSubmitCookieDataResult()
      case _ =>
        Logger.debug("VehicleLookupFailure submit could not find all the cookie data. A redirection will now occur")
        missingSubmitCookieDataResult()
    }
  }
}
