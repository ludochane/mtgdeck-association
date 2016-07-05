package fr.lcwi.mtgdeckassociation.main

import fr.lcwi.mtgdeckassociation.main.MtgAssociationApp.CmdOptions
import fr.lcwi.mtgdeckassociation.main.test.SparkSpec
import org.scalatest.Matchers

/**
  *
  */
class MtgAssociationAppSpec extends SparkSpec with Matchers {

  it should "generate association card rules" in {
    val cmdOptions: CmdOptions = CmdOptions()
    MtgAssociationApp.go(cmdOptions, sc)
  }

}
