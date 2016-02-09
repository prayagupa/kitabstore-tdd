package com

import org.junit.Test

/**
 * Created by prayagupd
 * on 2/8/16.
 */

class AmazonPackage {
    public static String WEIGHT = "WEIGHT"
}

class PackageSpecs {

    @Test
    void "map should return value by simple key" () {
        def map = ["w" : 100]
        assert map."w" == 100
    }

    @Test
    void "map should return value by dynamic key" () {
        def map = [ (AmazonPackage.WEIGHT) : 100, "id": "package001"]

        assert map['id'] == "package001"
        assert 2 == map.keySet().size()

        assert map[("${AmazonPackage.WEIGHT}")] == 100
        assert map."${AmazonPackage.WEIGHT}" == 100
        assert map."WEIGHT" == 100
    }
}
