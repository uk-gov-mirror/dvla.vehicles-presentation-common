package uk.gov.dvla.vehicles.presentation.common.controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}
import uk.gov.dvla.vehicles.presentation.common.webserviceclients.healthstats.HealthStats

class HealthCheck @Inject()(healthStats: HealthStats) extends Controller {

  def respond = Action { request =>
    healthStats.healthy match {
      case Some(notHealthy) => InternalServerError(notHealthy.details)
      case _ => Ok("")
    }
  }
}
