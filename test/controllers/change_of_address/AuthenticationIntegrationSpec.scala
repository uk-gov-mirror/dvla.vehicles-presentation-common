
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class AuthenticationIntegrationSpec extends Specification with Tags {

  "Authentication Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/authentication")

      // Assert
      titleMustContain("authentication")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      val validPIN = "123456"

      // Arrange
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"

      // Act
      browser.fill("#PIN") `with` validPIN
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - retrieve a vehicle record")
    }

  }

}