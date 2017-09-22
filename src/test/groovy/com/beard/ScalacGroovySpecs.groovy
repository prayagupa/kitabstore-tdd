package com.beard

import scala.Option
import scala.collection.JavaConverters
import scala.collection.Map
import spock.lang.Specification

class ScalacGroovySpecs extends Specification {

    def 'can invoke scala Map[K, V]'() {

        when:
        def map = new HashMap<String, String>()
        map.put("key", "value")

        then:
        assert JavaConverters.mapAsScalaMap(map) instanceof Map == true

    }

    //TODO
//    def "translates to scala Map" () {
//        when:
//        def x = new Moustache(Option.apply("data"))
//
//        then:
//        x.name() == Option.apply("data")
//    }
}
