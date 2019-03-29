package org.apache.spark.mllib.feature

import org.tartarus.snowball.SnowballStemmer

import org.apache.spark.sql.types.{DataType, StringType, ArrayType}
import org.apache.spark.ml.util.Identifiable
//import org.apache.spark.ml.param.{Param, ParamMap}
import org.apache.spark.ml.param._
import org.apache.spark.ml.util._
import org.apache.spark.ml.UnaryTransformer

class Stemmer(override val uid: String) 
  extends UnaryTransformer[Seq[String], Seq[String], Stemmer] with DefaultParamsWritable {

  def this() = this(Identifiable.randomUID("stemmer"))

  val language: Param[String] = new Param(this, "language", "stemming language (case insensitive).")
  def getLanguage: String = $(language)
  def setLanguage(value: String): this.type = set(language, value)
  setDefault(language -> "English")

  val splitToken: BooleanParam = new BooleanParam(this, "splitToken", "Apply stemming on sub tokens if enabled ( default false )")
  def getSplitToken: Boolean = $(splitToken)
  def setSplitToken(value: Boolean): this.type = set(splitToken, value)
  setDefault(splitToken -> false)

  val minLen: IntParam = new IntParam(this, "minLen", "Strip away words if min len of token is lower than given size; default 2")
  def getMinLen: Int = $(minLen)
  def setMinLen(value: Int): this.type = set(minLen, value)
  setDefault(minLen -> 2)

  override protected def createTransformFunc: Seq[String] => Seq[String] = { strArray =>
    val stemClass = Class.forName("org.tartarus.snowball.ext." + $(language).toLowerCase + "Stemmer")
    val stemmer = stemClass.newInstance.asInstanceOf[SnowballStemmer]

    // Words with space are stepped separately.
    if ($(splitToken)) {
      strArray.map(originStr => {
        originStr.split(" ").map {tok => 
          stemmer.setCurrent(tok)
          stemmer.stem()
          stemmer.getCurrent
        }.filter(_.length >= $(minLen)).mkString(" ")
      }).filter(_.length > 0) // Filter any empty strings, after all this cleanup.
    } else {
      strArray.map(originStr => {
          stemmer.setCurrent(originStr)
          stemmer.stem()
          stemmer.getCurrent
      }).filter(_.length >= $(minLen))
    }
  }

  override protected def validateInputType(inputType: DataType): Unit = {
    require(inputType.sameType(ArrayType(StringType)),
            s"Input type must be ArrayType(StringType) but got $inputType.")
  }

  override protected def outputDataType: DataType = new ArrayType(StringType, false)

  override def copy(extra: ParamMap): Stemmer = defaultCopy(extra)
}
