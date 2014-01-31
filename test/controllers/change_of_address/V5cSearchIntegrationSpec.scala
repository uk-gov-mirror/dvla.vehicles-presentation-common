
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{TestHelper, BrowserMatchers}

class V5cSearchIntegrationSpec extends Specification with Tags {

  "V5cSearch Integration" should {
    "be presented when the login cache is complete" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      // Pass credentials through login page and click submit
      TestHelper.loginPagePopulate(browser)

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")
      browser.goTo("/v5c-search")

      // Assert
      titleMustContain("retrieve a vehicle record")
    }

    "redirect to login when login cache is empty" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/v5c-search")

      // Assert
      titleMustContain("Change of keeper - are you registered")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      TestHelper.loginPagePopulate(browser)

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")

      // Complete V5c search page
       TestHelper.v5cSearchPagePopulate(browser)

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details")
    }
  }
}