package fr.lcwi.mtgdeckassociation.main

import org.apache.spark.SparkContext
import org.apache.spark.mllib.fpm.{FPGrowth, FPGrowthModel}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Row, SQLContext}

/**
  * Main class.
  */
object MtgAssociationApp extends App {

  /**
    * Case class to define all the command-line arguments
    *
    */
  case class CmdOptions(
                         master: String = "local[*]",
                         action: String = null
                       )

  val parser = new scopt.OptionParser[CmdOptions]("mtgdeck-association") {
    head("MTG Card Association")

    opt[String]('m', "master") action { (x, c) =>
      c.copy(master = x)
    } text ("Spark master url (ex: local, yarn-cluster)")

    opt[String]('a', "action") action { (x, c) =>
      c.copy(action = x)
    } validate { x => x match {
      case "csv" => success
      case "parquet" => success
      case _ => failure("Possible values are parquet, csv")
    }
    } text ("Persistence mode : parquet, csv (default : parquet)")
  }

  parser.parse(args, CmdOptions()) match {
    case None =>
      println("Wrong arguments")
      System.exit(-1)

    case Some(cmdOptions) =>
      // Spark init
      val sc: SparkContext = AppUtil.initSpark(cmdOptions.master)

      go(cmdOptions, sc)
  }

  /**
    * Main method.
    *
    * @param cmdOptions
    * @return
    */
  protected[main] def go(cmdOptions: CmdOptions, sc: SparkContext): Unit = {
    val sqlContext = new SQLContext(sc)

    //val data: RDD[String] = sc.textFile("src/main/resources/data/recipes.txt")
    val decksDF: DataFrame = sqlContext.read.json("src/main/resources/data/decks.json")
    val cardsDF = decksDF.select("maincards.name")
    val cards: RDD[Array[String]] = cardsDF.rdd.map(row => row.getSeq[String](0).distinct.toArray)
    cards.cache()

    //cards.collect().foreach(cardNames => println(cardNames.mkString(";")))

    val fpg = new FPGrowth()
      .setMinSupport(0.2)
      //.setNumPartitions(10)
    val model: FPGrowthModel[String] = fpg.run(cards)

    model.freqItemsets.collect().foreach { itemset =>
      println(itemset.items.mkString("[", ",", "]") + ", " + itemset.freq)
    }
//
//    println("##################")

//    val fPGrowthModelWith1Antecedent: FPGrowthModel[String] = new FPGrowthModel[String](
//      model.freqItemsets.filter(_.items.length == 2)
//    )
//
//    val minConfidence = 0.8
//    fPGrowthModelWith1Antecedent.generateAssociationRules(minConfidence).collect().foreach { rule =>
//      println(
//        rule.antecedent.mkString("[", ",", "]")
//          + " => " + rule.consequent.mkString("[", ",", "]")
//          + ", " + rule.confidence)
//    }
  }

}
