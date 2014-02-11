package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.SetUpTradeDetails
import SetUpTradeDetails._

class SetUpTradeDetailsIntegrationSpec extends Specification with Tags {


  "SetUpTradeDetails Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/disposal-of-vehicle/setup-trade-details")

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade 2")
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser)

      // Assert
      titleMustEqual("Business: Choose your address")
    }

    "display five validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "", traderPostcode = "")

      // Assert
      checkNumberOfValidationErrors(5)
    }

    "display two validation error messages when a valid postcode is entered with no business name" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a valid postcode is entered with a business name less than min length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "m")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display one validation error message when a valid postcode is entered with a business name more than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq")

      // Assert

      checkNumberOfValidationErrors(1)
    }

    "display three validation error messages when a valid business name is entered with no postcode" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "")

      // Assert
      checkNumberOfValidationErrors(3)
    }

    "display two validation error messages when a valid business name is entered with a postcode less than min length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "a")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display two validation error messages when a valid business name is entered with a postcode more than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "SA99 1DDD")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a valid business name is entered with a postcode containing an incorrect format" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "SAR99")

      // Assert
      checkNumberOfValidationErrors(1)
    }
  }
}