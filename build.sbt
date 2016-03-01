name := "spark-stemming"

version := "0.1.0"

organization := "com.github.master"

spName := "master/spark-stemming"

scalaVersion := "2.10.5"

crossScalaVersions := Seq("2.10.5", "2.11.7")

sparkVersion := "1.6.0"

spName := "com.github.master/spark-stemming"

spShortDescription := "Spark MLlib wrapper around Snowball stemming"

spDescription := """Snowball is a small string processing language
                    | designed for creating stemming algorithms for
                    | use in Information Retrieval. This package allows
                    | to use it as a part of Spark ML Pipeline API.""".stripMargin

licenses := Seq("BSD 2-Clause" -> url("https://opensource.org/licenses/BSD-2-Clause"))

sparkComponents ++= Seq("mllib", "sql")

credentials += Credentials(Path.userHome / ".sbt" / "sparkpkg-credentials")

libraryDependencies += "com.novocode" % "junit-interface" % "0.11" % Test
libraryDependencies += "org.scalatest" %% "scalatest" % "2.1.5" % Test
