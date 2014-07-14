import java.io.{FileOutputStream, OutputStream}
import java.net.URLClassLoader
import de.johoop.jacoco4sbt.JacocoPlugin._
import net.litola.SassPlugin
import org.apache.commons.io.IOUtils
import org.scalastyle.sbt.ScalastylePlugin
import play.Project._
import sbt.Keys._
import sbt._
import templemore.sbt.cucumber.CucumberPlugin
import scala.concurrent.Future

object Resolvers {
  val nexus = "http://rep002-01.skyscape.preview-dvla.co.uk:8081/nexus/content/repositories"

  val projectResolvers = Seq(
    "spray repo" at "http://repo.spray.io/",
    "local nexus snapshots" at s"$nexus/snapshots",
    "local nexus releases" at s"$nexus/releases"
  )

  val publisher = publishTo <<= version { v: String =>
    if (v.trim.endsWith("SNAPSHOT"))
      Some("snapshots" at s"$nexus/snapshots")
    else
      Some("releases" at s"$nexus/releases")
  }
}

object ApplicationBuild extends Build {
  import Resolvers._

  val appName         = "vehicles-online"

  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    cache,
    filters,
    "org.seleniumhq.selenium" % "selenium-java" % "2.42.2" % "test" withSources() withJavadoc(),
    "com.github.detro" % "phantomjsdriver" % "1.2.0" % "test" withSources() withJavadoc(),
    "info.cukes" % "cucumber-scala_2.10" % "1.1.7" % "test" withSources() withJavadoc(),
    "info.cukes" % "cucumber-java" % "1.1.7" % "test" withSources() withJavadoc(),
    "info.cukes" % "cucumber-picocontainer" % "1.1.7" % "test" withSources() withJavadoc(),
    "org.specs2" %% "specs2" % "2.3.10" % "test" withSources() withJavadoc(),
    "org.mockito" % "mockito-all" % "1.9.5" % "test" withSources() withJavadoc(),
    "com.github.tomakehurst" % "wiremock" % "1.46" % "test" withSources() withJavadoc() exclude("log4j", "log4j"),
    "org.slf4j" % "log4j-over-slf4j" % "1.7.7" % "test" withSources() withJavadoc(),
    "org.scalatest" % "scalatest_2.10" % "2.2.0" % "test" withSources() withJavadoc(),
    "com.google.inject" % "guice" % "4.0-beta4" withSources() withJavadoc(),
    "com.tzavellas" % "sse-guice" % "0.7.1" withSources() withJavadoc(), // Scala DSL for Guice
    "commons-codec" % "commons-codec" % "1.9" withSources() withJavadoc(),
    "org.apache.httpcomponents" % "httpclient" % "4.3.4" withSources() withJavadoc()
  )

  val cukes = CucumberPlugin.cucumberSettings ++
    Seq (
      CucumberPlugin.cucumberFeaturesLocation := "./test/acceptance/disposal_of_vehicle/",
      CucumberPlugin.cucumberStepsBasePackage := "helpers.steps",
      CucumberPlugin.cucumberJunitReport := false,
      CucumberPlugin.cucumberHtmlReport := false,
      CucumberPlugin.cucumberPrettyReport := false,
      CucumberPlugin.cucumberJsonReport := false,
      CucumberPlugin.cucumberStrict := true,
      CucumberPlugin.cucumberMonochrome := false
    )

  val jsModulesToOptimise = Seq("custom.js")

  /**
   * The Play Framework requires all config for RequireJS to be defined through a single file. The location of this file
   * is specified through the `requireJsShim` SBT setting.
   *
   * TypeSafe advocate setting the `requireJsShim` path to the application's `data-main` module. This approach assumes
   * there is just one, and will not scale when multiple `data-main` modules are used across the site.
   *
   * Currently this is not an issue: this application only uses one `data-main` module: custom.js
   *
   * If this becomes an issue, the solution is to define the universal set of all configuration options in a central
   * config file. This file can either be appended to the `/assets/javascripts/require.js` file (to reduce introducing
   * a new resource), or more simply, the subset of relevant options can be repeated across each `data-main` module.
   */
  val jsConfig = "custom.js"

  val myOrganization = Seq(organization := "dvla", organizationName := "Driver & Vehicle Licensing Agency")

  val compilerOptions = Seq(scalacOptions := Seq("-deprecation", "-unchecked", "-feature", "-Xlint", "-language:reflectiveCalls", "-Xmax-classfile-name", "128"))

  val myScalaVersion = Seq(scalaVersion := "2.10.3")

  val scalaCheck = org.scalastyle.sbt.ScalastylePlugin.Settings

  val requireJsSettings = Seq(requireJs := jsModulesToOptimise, requireJsShim := jsConfig)

  val myTestOptions =
  if (System.getProperty("include") != null ) {
    Seq(testOptions in Test += Tests.Argument("include", System.getProperty("include")))
  } else if (System.getProperty("exclude") != null ) {
    Seq(testOptions in Test += Tests.Argument("exclude", System.getProperty("exclude")))
  } else Seq.empty[Def.Setting[_]]

  // If tests are annotated with @LiveTest then they are excluded when running sbt test
  val excludeTest = testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-l", "helpers.tags.LiveTest")

  val myJavaOptions = Seq(javaOptions in Test += System.getProperty("waitSeconds"))

  val myConcurrentRestrictions = Seq(concurrentRestrictions in Global := Seq(Tags.limit(Tags.CPU, 4), Tags.limit(Tags.Network, 10), Tags.limit(Tags.Test, 4)))

  val fork = Seq(sbt.Keys.fork in Test := false)

  val jcoco = Seq(parallelExecution in jacoco.Config := false)

  val appSettings: Seq[Def.Setting[_]] = myOrganization ++ SassPlugin.sassSettings ++ myScalaVersion ++ compilerOptions ++ myConcurrentRestrictions ++
    myTestOptions ++ excludeTest ++ myJavaOptions ++ fork ++ jcoco ++ scalaCheck ++ requireJsSettings ++ cukes

  lazy val OsAddressLookup = Project("OsAddressLookup", file("vehicles-services/a"))
    .settings(libraryDependencies ++= Seq("dvla" %% "os-address-lookup" % "0.1"))
    .settings(resolvers ++= projectResolvers)
    .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  lazy val VehiclesLookup = Project("VehiclesLookup", file("vehicles-services/b"))
    .settings(libraryDependencies ++= Seq("dvla" %% "vehicles-lookup" % "0.1"))
    .settings(resolvers ++= projectResolvers)
    .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  lazy val VehiclesDisposeFulfil = Project("VehiclesDisposeFulfil", file("vehicles-services/c"))
    .settings(libraryDependencies ++= Seq("dvla" %% "vehicles-dispose-fulfil" % "0.1"))
    .settings(resolvers ++= projectResolvers)
    .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)

  lazy val vehiclesOnline = ScopeFilter(inProjects(ThisProject), inConfigurations(Runtime))
  lazy val scopeOsAddressLookup = ScopeFilter(inProjects(LocalProject(OsAddressLookup.id)), inConfigurations(Runtime))
  lazy val scopeVehiclesLookup = ScopeFilter(inProjects(LocalProject(VehiclesLookup.id)), inConfigurations(Runtime))
  lazy val scopeVehiclesDisposeFulfil = ScopeFilter(
    inProjects(LocalProject(VehiclesDisposeFulfil.id)), inConfigurations(Runtime)
  )

  lazy val sandbox = taskKey[Unit]("Runs the sandbox'")
  lazy val sandboxTask = sandbox := {
    val cpVehiclesOnline = fullClasspath.all(vehiclesOnline).value.flatten
    val cpOsAddressLookup = fullClasspath.all(scopeOsAddressLookup).value.flatten
    val cpVehiclesLookup = fullClasspath.all(scopeVehiclesLookup).value.flatten
    val cpeVehiclesDisposeFulfil = fullClasspath.all(scopeVehiclesDisposeFulfil).value.flatten

    cpVehiclesOnline.map(_.data.toURI.toURL).toArray.foreach(println)

    sys.props += "config.resource" -> "application.conf"
    val osalClassLoader = new URLClassLoader(
      cpOsAddressLookup.map(_.data.toURI.toURL).toArray,
      this.getClass.getClassLoader.getParent.getParent
    )

    IOUtils.write("""
                    |ordnancesurvey.requesttimeout = "9999"
                    |ordnancesurvey.apiversion = "testing"
                    |ordnancesurvey.beta06.username = "testUser"
                    |ordnancesurvey.beta06.password = "testPass"
                    |ordnancesurvey.beta06.baseurl = "https://localhost/ord-serv:1234"
                    |ordnancesurvey.preproduction.apikey = "someApiKey"
                    |ordnancesurvey.preproduction.baseurl = "http://baseUrl"
                  """.stripMargin, new FileOutputStream(new File(classDirectory.all(scopeOsAddressLookup).value.head, "os-address-lookup.conf")))

    val t = new Thread() {
      override def run() {
        Thread.currentThread().setContextClassLoader(osalClassLoader)

        import scala.reflect.runtime.universe.runtimeMirror
        import scala.reflect.runtime.universe.newTermName
        lazy val mirror = runtimeMirror(osalClassLoader)
        val bootSymbol = mirror.staticModule("dvla.microservice.Boot").asModule
        val boot = mirror.reflectModule(bootSymbol).instance
        val mainMethodSymbol = bootSymbol.typeSignature.member(newTermName("main")).asMethod
        val bootMirror = mirror.reflect(boot)
        bootMirror.reflectMethod(mainMethodSymbol).apply(Array[String]())
      }
    }

    t.start()

    t.join()

  }

  val main = play.Project(
    appName,
    appVersion,
    appDependencies,
    settings = play.Project.playScalaSettings ++ jacoco.settings ++ ScalastylePlugin.Settings
  ).settings(appSettings: _*)
   .settings(net.virtualvoid.sbt.graph.Plugin.graphSettings: _*)
   .settings(resolvers ++= projectResolvers)
   .settings(publisher)
   .settings(sandboxTask)
}