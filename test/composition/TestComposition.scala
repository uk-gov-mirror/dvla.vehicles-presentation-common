package composition

import com.google.inject.{Injector, Guice}
import filters.EnsureSessionCreatedFilter
import play.api.mvc.EssentialFilter
import play.filters.csrf.CSRFFilter

object TestComposition {
  lazy val testInjector: Injector = Guice.createInjector(new TestModule())

  lazy val filters = Array(new CSRFFilter(), testInjector.getInstance(classOf[EnsureSessionCreatedFilter]))

}