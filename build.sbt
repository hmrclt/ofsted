import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val uniformVersion = "0.2.3"

lazy val root = project.in(file("."))
  .aggregate(
//    ofstedProgramJS,
    ofstedProgramJVM,
//    `ofsted-prototype`,
    `ofsted-play`
  )
  .settings(
    publishLocal := {},
    publish := {},
    publishArtifact := false
  )
  .settings(commonSettings)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.7",
  addCompilerPlugin("org.spire-math" %% "kind-projector" % "0.9.3"),
  scalacOptions ++= Seq(
//    "-Xfatal-warnings",                  // Fail the compilation if there are any warnings.
    "-deprecation",                      // Emit warning and location for usages of deprecated APIs.
    "-encoding", "utf-8",                // Specify character encoding used by source files.
    "-explaintypes",                     // Explain type errors in more detail.
    "-feature",                          // Emit warning and location for usages of features that should be imported explicitly.
    "-unchecked",                        // Enable additional warnings where generated code depends on assumptions.
    "-Xcheckinit",                       // Wrap field accessors to throw an exception on uninitialized access.
    "-Xfuture",                          // Turn on future language features.
    "-Xlint:adapted-args",               // Warn if an argument list is modified to match the receiver.
    "-Xlint:by-name-right-associative",  // By-name parameter of right associative operator.
//    "-Xlint:constant",                   // Evaluation of a constant arithmetic expression results in an error.
    "-Xlint:delayedinit-select",         // Selecting member of DelayedInit.
    "-Xlint:doc-detached",               // A Scaladoc comment appears to be detached from its element.
    "-Xlint:inaccessible",               // Warn about inaccessible types in method signatures.
    "-Xlint:infer-any",                  // Warn when a type argument is inferred to be `Any`.
    "-Xlint:missing-interpolator",       // A string literal appears to be missing an interpolator id.
    "-Xlint:nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Xlint:nullary-unit",               // Warn when nullary methods return Unit.
    "-Xlint:option-implicit",            // Option.apply used implicit view.
    "-Xlint:package-object-classes",     // Class or object defined in package object.
    "-Xlint:poly-implicit-overload",     // Parameterized overloaded implicit methods are not visible as view bounds.
    "-Xlint:private-shadow",             // A private field (or class parameter) shadows a superclass field.
    "-Xlint:stars-align",                // Pattern sequence wildcard must align with sequence component.
    "-Xlint:type-parameter-shadow",      // A local type parameter shadows a type already in scope.
    "-Xlint:unsound-match",              // Pattern match may not be typesafe.
    "-Yno-adapted-args",                 // Do not adapt an argument list (either by inserting () or creating a tuple) to match the receiver.
    "-Ypartial-unification",             // Enable partial unification in type constructor inference
    "-Ywarn-dead-code",                  // Warn when dead code is identified.
//    "-Ywarn-extra-implicit",             // Warn when more than one implicit parameter section is defined.
    "-Ywarn-inaccessible",               // Warn about inaccessible types in method signatures.
    "-Ywarn-infer-any",                  // Warn when a type argument is inferred to be `Any`.
    "-Ywarn-nullary-override",           // Warn when non-nullary `def f()' overrides nullary `def f'.
    "-Ywarn-nullary-unit",               // Warn when nullary methods return Unit.
    "-Ywarn-numeric-widen",              // Warn when numerics are widened.
    "-Ywarn-unused",
//    "-Ywarn-unused:implicits",           // Warn if an implicit parameter is unused.
//    "-Ywarn-unused:imports",             // Warn if an import selector is not referenced.
//   "-Ywarn-unused:locals",              // Warn if a local definition is unused.
//    "-Ywarn-unused:params",              // Warn if a value parameter is unused.
//    "-Ywarn-unused:patvars",             // Warn if a variable bound in a pattern is unused.
//    "-Ywarn-unused:privates",            // Warn if a private member is unused.
    "-Ywarn-value-discard"               // Warn when non-Unit expression results are unused.
  ),
  scalacOptions in (Compile, console) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),
  libraryDependencies ++= Seq(
    "org.atnos" %%% "eff" % "5.2.0",
    "org.scalatest" %%% "scalatest" % "3.0.5" % "test"
  )
)

lazy val `ofsted-program` = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      "com.beachape" %%% "enumeratum" % "1.5.13",
      "com.luketebbs.uniform" %%% "core" % uniformVersion, 
      "com.luketebbs.uniform" %%% "interpreter-logictable" % "0.2.3-SNAPSHOT" % "test"
    )
  )

lazy val ofstedProgramJS = `ofsted-program`.js
lazy val ofstedProgramJVM = `ofsted-program`.jvm

lazy val `ofsted-prototype` = project
  .settings(commonSettings)
  .dependsOn(ofstedProgramJS)
  .settings(
    scalaJSUseMainModuleInitializer := true,
    libraryDependencies ++= Seq(
      "org.querki" %%% "jquery-facade" % "1.2",
      "com.luketebbs.uniform" %%% "interpreter-js" % uniformVersion
    )
  )
  .enablePlugins(ScalaJSPlugin)

lazy val `ofsted-play` = project
  .settings(commonSettings)
  .dependsOn(ofstedProgramJVM)
  .enablePlugins(PlayScala)
  .settings(
    libraryDependencies ++= Seq(
      filters,
      guice,
      "com.luketebbs.uniform" %%% "interpreter-play26" % uniformVersion
    )
  )
