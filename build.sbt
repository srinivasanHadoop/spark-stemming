name := "spark-stemmer"

version := "0.1.0"

organization := "com.github.master"

scalaVersion := "2.10.5"

crossScalaVersions := Seq("2.10.5", "2.11.7")

sparkVersion := "1.6.0"

spName := "com.github.master/spark-stemmer"

spShortDescription := "Spark MLlib wrapper around Snowball stemming"

spDescription := """Snowball is a small string processing language
                    | designed for creating stemming algorithms for
                    | use in Information Retrieval. This package allows
                    | to use it as a part of Spark ML Pipeline API.""".stripMargin

spAppendScalaVersion := true

licenses := Seq("BSD 2-Clause" -> url("https://opensource.org/licenses/BSD-2-Clause"))

sparkComponents ++= Seq("mllib", "sql")

credentials += Credentials(Path.userHome / ".sbt" / "sparkpkg-credentials")

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.5" % Test
libraryDependencies ++= libraryDependencies.value
  .filter { _.organization == "org.apache.spark" }
  .map { dep => dep.organization %% dep.name % dep.revision % Test }

//"com.holdenkarau" %% "spark-testing-base" % "1.6.0_0.3.1"
