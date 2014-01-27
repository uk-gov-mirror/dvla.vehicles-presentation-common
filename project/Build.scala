import sbt._
import sbt.Keys._
import play.Project._
import net.litola.SassPlugin
import de.johoop.jacoco4sbt.JacocoPlugin._

object ApplicationBuild extends Build {
  val appName         = "vehicles-online"

  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    jdbc,
    cache,
    "org.specs2" %% "specs2" % "2.3.6" % "test" withSources() withJavadoc(),
    "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc(),
    "com.dwp.carers" %% "carerscommon" % "0.81" ,
    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "org.seleniumhq.selenium" % "selenium-java" % "2.35.0" % "test"
  )

  var sO: Seq[Def.Setting[_]] = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls"))

  var sV: Seq[Def.Setting[_]] = Seq(scalaVersion := "2.10.3")

  var sR: Seq[Def.Setting[_]] = Seq(resolvers += "Carers repo" at "http://build.3cbeta.co.uk:8080/artifactory/repo/")

  var sTest: Seq[Def.Setting[_]] = Seq()

  if (System.getProperty("include") != null ) {

    sTest = Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  }

  if (System.getProperty("exclude") != null ) {
    sTest = Seq(testOptions in Test += Tests.Argument("exclude", System.getProperty("exclude")))
  }

  var jO: Seq[Def.Setting[_]] = Seq(javaOptions in Test += System.getProperty("waitSeconds"))

  var gS: Seq[Def.Setting[_]] = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 4), Tags.limit(Tags.Network, 10), Tags.limit(Tags.Test, 4)))

  var f: Seq[Def.Setting[_]] = Seq(sbt.Keys.fork in Test := false)

  var jcoco: Seq[Def.Setting[_]] = Seq(parallelExecution in jacoco.Config := false)

  var appSettings: Seq[Def.Setting[_]] =  SassPlugin.sassSettings ++ sV ++ sO ++ sR ++ gS ++ sTest ++ jO ++ f ++ jcoco

  val main = play.Project(appName, appVersion, appDependencies, settings = play.Project.playScalaSettings ++ jacoco.settings).settings(appSettings: _*)
}
