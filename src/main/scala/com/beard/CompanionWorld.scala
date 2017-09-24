package com.beard

class CompanionWorld {
  import CompanionWorld._
  println(SingletonName)
}

object CompanionWorld {
  val SingletonName = "who cares about you"
}