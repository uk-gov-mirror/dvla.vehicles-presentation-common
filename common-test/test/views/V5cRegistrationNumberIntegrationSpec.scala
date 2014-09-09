package views

import helpers.UiSpec
import helpers.webbrowser.TestHarness
import pages.{ErrorPanel, V5cRegistrationNumberPage}

class V5cRegistrationNumberIntegrationSpec extends UiSpec with TestHarness {

  "ValtechInputText integration" should {
    "display the page" in new WebBrowser {
      go to V5cRegistrationNumberPage
      page.title should equal(V5cRegistrationNumberPage.title)
    }

    "displays success page when correct data is entered" in new WebBrowser {
      V5cRegistrationNumberPage.navigate()
      page.title should equal("Success") // Check the new title of the success page
    }

    "reject submit when field is blank" in new WebBrowser {
      V5cRegistrationNumberPage.navigate(v5cRegistrationNumber = "")
      ErrorPanel.numberOfErrors should equal(2)
    }
  }
}