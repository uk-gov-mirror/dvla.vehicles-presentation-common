package uk.gov.dvla.vehicles.presentation.common.composition

import uk.gov.dvla.vehicles.presentation.common.helpers.webbrowser.GlobalCreator


object TestGlobal extends GlobalLike with TestComposition

trait ChangeKeeperGlobalCreator extends GlobalCreator {
  override def global = TestGlobal
}
