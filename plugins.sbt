resolvers += Classpaths.sbtPluginReleases

addSbtPlugin("com.sqality.scct" % "sbt-scct" % "0.3")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.1")

// Plugin for publishing scoverage results to coveralls
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0.BETA1")

// Plugin for code formatting:
addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

// Plugin for checking code style:
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.4.0")