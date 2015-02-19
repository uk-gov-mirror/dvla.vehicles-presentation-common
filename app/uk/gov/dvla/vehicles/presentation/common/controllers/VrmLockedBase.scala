package uk.gov.dvla.vehicles.presentation.common.controllers

import com.google.inject.Inject
import play.api.Logger
import play.api.mvc.{Action, Controller, Request, Result}
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.ClientSideSessionFactory
import uk.gov.dvla.vehicles.presentation.common.clientsidesession.CookieImplicits.RichCookies
import uk.gov.dvla.vehicles.presentation.common.model.BruteForcePreventionModel

abstract class VrmLockedBase @Inject()()(implicit protected val clientSideSessionFactory: ClientSideSessionFactory
                               ) extends Controller {

  protected def presentResult(model: BruteForcePreventionModel)
                             (implicit request: Request[_]): Result

  protected def missingBruteForcePreventionCookie(implicit request: Request[_]): Result

  protected def tryAgainResult(implicit request: Request[_]): Result

  protected def exitResult(implicit request: Request[_]): Result

  def present = Action { implicit request =>
    request.cookies.getModel[BruteForcePreventionModel] match {
      case Some(viewModel) =>
        Logger.debug(s"VrmLocked - Displaying the vrm locked error page")
        presentResult(viewModel)
      case None =>
        Logger.debug("VrmLocked - Can't find cookie for BruteForcePreventionViewModel")
        missingBruteForcePreventionCookie
    }
  }

  def tryAgain = Action { implicit request =>
    tryAgainResult
  }

  def exit = Action { implicit request =>
    exitResult
  }
}
