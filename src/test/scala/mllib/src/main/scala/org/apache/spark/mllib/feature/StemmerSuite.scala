package org.apache.spark.mllib.feature

import org.scalatest.FunSuite

class StemmerSuite extends FunSuite with LocalSparkContext {
  test("Stemming of English words") {
    val data = sqlContext.createDataFrame(Seq(
      ("All", 1),
      ("mimsy", 2),
      ("were", 3),
      ("the", 4),
      ("borogoves", 5)
    )).toDF("word", "id")

    val stemmed = new Stemmer()
      .setInputCol("word")
      .setOutputCol("stemmed")
      .transform(data)

    val expected = sqlContext.createDataFrame(Seq(
      ("All", 1, "All"),
      ("mimsy", 2, "mimsi"),
      ("were", 3, "were"),
      ("the", 4, "the"),
      ("borogoves", 5, "borogov")
    )).toDF("word", "id", "stemmed")

    assert(stemmed.collect().deep == expected.collect().deep)
  }

  test("Stemming of non-English words") {
    val data = sqlContext.createDataFrame(Seq(
      ("övrigt", 1),
      ("bildelar", 2),
      ("biltillbehör", 3)
    )).toDF("word", "id")

    val stemmed = new Stemmer()
      .setInputCol("word")
      .setOutputCol("stemmed")
      .setLanguage("Swedish")
      .transform(data)

    val expected = sqlContext.createDataFrame(Seq(
      ("övrigt", 1, "övr"),
      ("bildelar", 2, "bildel"),
      ("biltillbehör", 3, "biltillbehör")
    )).toDF("word", "id", "stemmed")

    assert(stemmed.collect().deep == expected.collect().deep)
  }
}
