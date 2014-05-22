package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeApplication, WithApplication}

final class AesEncryptionSpec extends WordSpec with Matchers {
  "encryptCookie" should {
    "return an encrypted string" in new WithApplication(app = appWithCryptpConfig) {
      val aesEncryption = new AesEncryption
      aesEncryption.encrypt(clearText) should not equal clearText
    }

    "return a different encrypted string given same clear text input" in new WithApplication(app = appWithCryptpConfig) {
      val aesEncryption = new AesEncryption
      val cipherText1 = aesEncryption.encrypt(clearText)
      val cipherText2 = aesEncryption.encrypt(clearText)

      withClue("Initialization vectors must be used to ensure output is always random") {
        cipherText1 should not equal cipherText2
      }
    }
  }

  "decryptCookie" should {
    "return a decrypted string" in new WithApplication(app = appWithCryptpConfig) {
      val aesEncryption = new AesEncryption
      val encrypted = aesEncryption.encrypt(clearText)
      aesEncryption.decrypt(encrypted) should equal(clearText)
    }
  }

  private final val clearText = "qwerty" // TODO all the different copies of clearText should be replaced by a single val in an object
  private val appWithCryptpConfig = FakeApplication(
    additionalConfiguration = Map("application.secret256Bit" -> "MnPSvGpiEF5OJRG3xLAnsfmdMTLr6wpmJmZLv2RB9Vo="))

}
