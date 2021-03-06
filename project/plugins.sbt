resolvers += Resolver.url("HMRC Sbt Plugin Releases", url("https://dl.bintray.com/hmrc/sbt-plugin-releases"))(Resolver.ivyStylePatterns)

resolvers += "HMRC Releases" at "https://dl.bintray.com/hmrc/releases"

addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.19")

addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.1")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "1.0.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-auto-build" % "1.15.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-git-versioning" % "1.17.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-artifactory" % "0.18.0")

addSbtPlugin("uk.gov.hmrc" % "sbt-distributables" % "1.3.0")
