package fr.lcwi.mtgdeckassociation.main.test

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkConf, SparkContext}
import org.scalatest.{Suite, _}

import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
  *
  */
trait SparkSpec extends FlatSpec with BeforeAndAfterAll with BeforeAndAfter with Matchers with Inspectors {

  this: Suite =>

  var sc: SparkContext = _

  var sqlContext: SQLContext = _


  override def beforeAll() = {
    // To avoid Akka rebinding to the same port, since it doesn't unbind immediately on shutdown
    System.clearProperty("spark.driver.port")
    System.clearProperty("spark.hostPort")

    val conf = new SparkConf()
    conf
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryoserializer.buffer.max", "1024m")
      .registerKryoClasses(Array(classOf[ArrayBuffer[String]], classOf[ListBuffer[String]]))
      .setAppName("tests")
      .setMaster("local[*]")
    sc = new SparkContext(conf)
    sqlContext = new SQLContext(sc)
  }

  override def afterAll() = {
    // Cleanup spark context data
    sc.stop()
    sqlContext = null
    sc = null
    System.clearProperty("spark.driver.port")
    System.clearProperty("spark.hostPort")
  }

}
