package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}

class BusinessChooseYourAddressIntegrationSpec extends Specification with Tags {
  "business_choose_your_address Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser)
      browser.goTo(BusinessChooseYourAddressPage.url)

      // Assert
      titleMustEqual(BusinessChooseYourAddressPage.title)
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser)
      BusinessChooseYourAddressPage.happyPath(browser)

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(BusinessChooseYourAddressPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display validation error messages when addressSelected is not in the list" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser)
      BusinessChooseYourAddressPage.sadPath(browser)

      //Assert
      checkNumberOfValidationErrors(2)
    }

    "display previous page when back link is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser)
      browser.click("#backButton")

      //Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}