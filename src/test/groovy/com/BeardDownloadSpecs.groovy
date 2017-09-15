package com

import spock.lang.Specification

/**
 * Created by prayagupd
 * on 1/5/16.
 */

class BeardDownloadSpecs extends Specification {
    def counter

    def setup () {
        counter = new BeardDownload()
    }

    def "count should download and count people with beard and return the count" () {
        given: "instance of BeardDownload"
            def downloader = counter

        and: "3 people with beard"
            def mike = new Person("mike", "columbus")
            def zak = new Person("zak", "columbus")
            def brandon = new Person("brandon", "columbus")
            downloader.setPersons([mike, zak, brandon])

        when: "count method is called"
            def captor = downloader.count("few")

        then: "result should be equal to 3"
            captor == 3
    }

    def "count should bombast the internet if someone tries to download all beard" () {
        given:
        def downloader = counter

        when:
        def captor = downloader.count("all")

        then:
        captor == Long.MAX_VALUE
    }
}
