package org.apache.spark.mllib.feature

import org.scalatest.FunSuite

class StemmerSuite extends FunSuite with LocalSparkContext {
  test("Stemming of English words") {
    val data = sqlContext.createDataFrame(Seq(
      (Array("All", "is", "mimsy"), 1),
      (Array("were", "the", "borogroves"), 2),
      (Array("I", "used", "to", "like", "this"), 3)
    )).toDF("word", "id")

    //minLen = 2 and subTokenSplit = false as default.
    val stemmed = new Stemmer()
      .setInputCol("word")
      .setOutputCol("stemmed")
      .transform(data)

    val expected = sqlContext.createDataFrame(Seq(
      (Array("All", "is", "mimsy"), 1, Array("All", "is", "mimsi")),
      (Array("were", "the", "borogroves"), 2, Array("were", "the", "borogrov")),
      (Array("I", "used", "to", "like", "this"), 3, Array("use", "to", "like", "this"))
    )).toDF("word", "id", "stemmed")

    assert(stemmed.collect().deep == expected.collect().deep)
  }

  test("Stemming of non-English words") {
    val data = sqlContext.createDataFrame(Seq(
      (Array("övrigt"), 1),
      (Array("bildelar"), 2),
      (Array("biltillbehör"), 3)
    )).toDF("word", "id")

    val stemmed = new Stemmer()
      .setInputCol("word")
      .setOutputCol("stemmed")
      .setLanguage("Swedish")
      .transform(data)

    val expected = sqlContext.createDataFrame(Seq(
      (Array("övrigt"), 1, Array("övr")),
      (Array("bildelar"), 2, Array("bildel")),
      (Array("biltillbehör"), 3, Array("biltillbehör"))
    )).toDF("word", "id", "stemmed")

    assert(stemmed.collect().deep == expected.collect().deep)
  }

  test("Stem subtokens") {
    val data = sqlContext.createDataFrame(Seq(
      (Array("All", "mimsy"), 1),
      (Array("were", "the", "borogroves"), 2),
      (Array("electronic device", "mobile devices"), 3)
    )).toDF("word", "id")

    val stemmed = new Stemmer()
      .setInputCol("word")
      .setOutputCol("stemmed")
      .setSplitToken(true)
      .setMinLen(4)
      .transform(data)

    val expected = sqlContext.createDataFrame(Seq(
      (Array("All", "mimsy"), 1, Array("mimsi")),
      (Array("were", "the", "borogroves"), 2, Array("were", "borogrov")),
      (Array("electronic device", "mobile devices"), 3, Array("electron devic", "mobil devic"))
    )).toDF("word", "id", "stemmed")

    assert(stemmed.collect().deep == expected.collect().deep)
  }
}
