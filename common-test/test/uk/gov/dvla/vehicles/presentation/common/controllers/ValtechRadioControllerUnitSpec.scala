package uk.gov.dvla.vehicles.presentation.common.controllers

import uk.gov.dvla.vehicles.presentation.common.views
import uk.gov.dvla.vehicles.presentation.common.helpers

import uk.gov.dvla.vehicles.presentation.common.helpers.{WithApplication, CookieFactoryForUnitSpecs, UnitSpec}
import play.api.test.FakeRequest
import play.api.test.Helpers.{OK, contentAsString, defaultAwaitTimeout}
import views.ValtechRadioView.{KeeperType_Business, KeeperType_Private}

final class ValtechRadioControllerUnitSpec extends UnitSpec {

  "present" should {
    "display the page" in new WithApplication {
      whenReady(present) {
        r =>
          r.header.status should equal(OK)
      }
    }

    "display correct radio button pre-selected when cookie contains private keeper" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.valtechRadio())
      val result = valtechRadioController.present(request)
      val content = contentAsString(result)
      content should include(expectedRadioButtonSelected(KeeperType_Private))
    }

    "display correct radio button pre-selected when cookie contains business keeper" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.valtechRadio(keeperType = KeeperType_Business))
      val result = valtechRadioController.present(request)
      val content = contentAsString(result)
      content should include(expectedRadioButtonSelected(KeeperType_Business))
    }
  }

  private def expectedRadioButtonSelected(radioButton: String) = s"""value="$radioButton" checked"""

  private val valtechRadioController = injector.getInstance(classOf[ValtechRadioController])

  private lazy val present = {
    val request = FakeRequest()
    valtechRadioController.present(request)
  }

}