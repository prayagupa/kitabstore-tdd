package com.beard
/**
 * Created by prayagupd
 * on 1/5/16.
 */

public class BeardDownload {

    private List<Person> persons

    def count(def few) {
        if (few.equals("all")) {
            Long.MAX_VALUE
        } else {
            persons.size()
        }
    }

    public def setPersons(def p) {
        this.persons = p
    }

    String doSomething(Closure<String> fn, def param) {
        fn(param)
    }

    Integer doSomething(Closure<Integer> fn) {
        fn()
    }

    def closure(String x1, int y1) {
        { String x, int y ->
            println "hey ${x} the value is ${y}"
        }
    }
}
