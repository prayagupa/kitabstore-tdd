package com.beard

import scala.Option
import scala.Tuple2
import scala.collection.JavaConverters
import scala.collection.Map
import scala.collection.Seq
import spock.lang.Specification

class ScalacGroovySpecs extends Specification {

    def setup() {
        List.metaClass.asScala = { -> JavaConverters.asScalaBuffer(delegate) }
    }

    def 'can invoke scala Map[K, V]'() {

        when:
        def map = new HashMap<String, String>()
        map.put("key", "value")

        then:
        assert JavaConverters.mapAsScalaMap(map) instanceof Map == true

    }

    def "translates to scala Map" () {
        when:
        def x = new Moustache(Option.apply("data"))

        then:
        x.name() == Option.apply("data")
    }

    def "initialises scala Seq"() {

        expect:
        assert JavaConverters.asScalaBuffer(["a", "b"]) instanceof Seq == true
    }

    def "initialises scala Seq with groovy metaclass"() {
        given:
        List.metaClass.asScalac = { -> JavaConverters.asScalaBuffer(delegate) }

        expect:
        assert ["a", "b"].asScalac() instanceof Seq
    }

    def "tuple"() {
        given:
        List.metaClass.asFreakingScala = { -> JavaConverters.asScalaBuffer(delegate) }

        expect:
        new Tuple2<String, String>("a", "b")._1() == "a"
        new Tuple2<String, String>("a", "b")._2() == "b"

        JavaConverters.asScalaBuffer([new Tuple2<String, String>("a", "b")]).foreach({x -> assert x._1() == "a"})
        [new groovy.lang.Tuple2<String, String>("a", "b")].asFreakingScala() instanceof Seq
    }
}
