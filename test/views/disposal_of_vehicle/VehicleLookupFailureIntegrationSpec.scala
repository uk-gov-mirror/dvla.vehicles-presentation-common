package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.disposal_of_vehicle.VehicleLookupFailurePage._
import pages.disposal_of_vehicle._
import mappings.disposal_of_vehicle.VehicleLookup._
import services.fakes.brute_force_protection.FakeBruteForcePreventionWebServiceImpl.MaxAttempts

final class VehicleLookupFailureIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {

    "display the page" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to VehicleLookupFailurePage

      page.title should equal(VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WebBrowser {
      go to VehicleLookupFailurePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.vehicleLookupFormModel()

      go to VehicleLookupFailurePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only dealerDetails cache is populated" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.dealerDetails()

      go to VehicleLookupFailurePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "remove redundant cookies when displayed" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to VehicleLookupFailurePage

      webDriver.manage().getCookieNamed(VehicleLookupResponseCodeCacheKey) should equal(null)
    }

    "not display warnAboutLockout messages when 1 attempt has been made" in new WebBrowser {
      go to BeforeYouStartPage

      CookieFactoryForUISpecs.
        dealerDetails().
        bruteForcePreventionViewModel(attempts = 1, maxAttempts = MaxAttempts).
        vehicleLookupFormModel().
        vehicleLookupResponseCode(responseCode = "vehicle_lookup_document_reference_mismatch")

      go to VehicleLookupFailurePage

      page.source should include("For each vehicle registration mark, only three attempts can be made to retrieve the vehicle details.")
      page.source should not include "After a third unsuccessful attempt the system prevents further attempts to access the the vehicles records for 10 minutes. This is to safeguard vehicle records. Other vehicles can be processed using this service during this period."
    }

    "display warnAboutLockout messages when 2 attempts have been made" in new WebBrowser {
      go to BeforeYouStartPage

      CookieFactoryForUISpecs.
        dealerDetails().
        bruteForcePreventionViewModel(attempts = 2, maxAttempts = MaxAttempts).
        vehicleLookupFormModel().
        vehicleLookupResponseCode(responseCode = "vehicle_lookup_document_reference_mismatch")

      go to VehicleLookupFailurePage

      page.source should include("For each vehicle registration mark, only three attempts can be made to retrieve the vehicle details.")
      page.source should include("After a third unsuccessful attempt the system prevents further attempts to access the the vehicles records for 10 minutes. This is to safeguard vehicle records. Other vehicles can be processed using this service during this period.")
    }
  }

  "vehicleLookup button" should {
    "redirect to vehiclelookup when button clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VehicleLookupFailurePage

      click on vehicleLookup

      page.title should equal(VehicleLookupPage.title)
    }
  }

  "beforeYouStart button" should {
    "redirect to beforeyoustart" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VehicleLookupFailurePage

      click on beforeYouStart

      page.title should equal(BeforeYouStartPage.title)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      dealerDetails().
      bruteForcePreventionViewModel().
      vehicleLookupFormModel().
      vehicleLookupResponseCode()
}
