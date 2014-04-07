package functional

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import pages.disposal_of_vehicle._
import helpers.webbrowser._
import helpers.disposal_of_vehicle.Helper._

class SetUpTradeDetailsSpec extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {

  feature("US158: Disposal to Trade: Business Name - Error Messages") {
    info("As a Motor Trader")
    info("I want to receive an appropriate error message when I enter a disallowed Business Name")
    info("so that I can move to the next step in the transaction")
    info("")

    scenario("No business name entered") {
      new WebBrowser {
        Given("the motor trader has entered no Business Name")
        go to BeforeYouStartPage
        click on BeforeYouStartPage.startNow
        // Note: leave the dealerName field blank to cause errors.
        SetupTradeDetailsPage.dealerPostcode enter postcodeValid

        When("the Business Name does not comply with formatting rules")
        click on SetupTradeDetailsPage.lookup

        Then("a single appropriate message is displayed")
        page.text should include("Must be between two and 30 characters and not contain invalid characters")

        And("the motor trader remains on the current step in the")
        page.title should equal("Provide your trader details")
      }
    }

    scenario("Valid data entered") {
      new WebBrowser {
        Given("the motor trader has entered data")
        go to BeforeYouStartPage
        click on BeforeYouStartPage.startNow
        SetupTradeDetailsPage.dealerName enter traderBusinessNameValid
        SetupTradeDetailsPage.dealerPostcode enter postcodeValid

        When("the data complies with formatting rules")
        click on SetupTradeDetailsPage.lookup

        Then("the Business Name is retained")
        page.text should include(traderBusinessNameValid)

        And("the motor trader progresses to the next step in the transaction")
        page.title should equal("Select your trade address")
      }
    }
  }

  feature("Disposal to Trade: Postcode - Error Messages") {
    info("As a Motor Trader")
    info("I want to receive an appropriate error message when I enter a disallowed postcode")
    info("so that I can correct it and move to the next step in the transaction")
    info("")

    scenario("No postcode entered") {
      new WebBrowser {
        Given("the motor trader has entered no postcode")
        go to BeforeYouStartPage
        click on BeforeYouStartPage.startNow
        SetupTradeDetailsPage.dealerName enter traderBusinessNameValid
        // Note: leave the postcode field blank to cause errors.

        When("the postcode does not comply with formatting rules")
        click on SetupTradeDetailsPage.lookup

        Then("a single appropriate message is displayed")
        page.text should include("Must be between 5 and 8 characters and in the format PR2 8AE")

        And("the motor trader remains on the current step in the")
        page.title should equal("Provide your trader details")
      }
    }
  }

}
