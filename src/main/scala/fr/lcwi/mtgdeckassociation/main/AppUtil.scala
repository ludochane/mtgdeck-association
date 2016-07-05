package fr.lcwi.mtgdeckassociation.main

import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object AppUtil {

  /**
    * Init Spark context.
    *
    * @param master
    * @return
    */
  def initSpark(master: String): SparkContext = {
    val sparkConf = new SparkConf().setAppName("MTG Card Association")
    if (master != null) sparkConf.setMaster(master)
    new SparkContext(sparkConf)
  }

}
