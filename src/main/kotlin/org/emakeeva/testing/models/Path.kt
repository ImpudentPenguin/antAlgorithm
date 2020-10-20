package org.emakeeva.testing.models

class Path() {

    var route: MutableList<Int> = mutableListOf()
    var distance: Double = 0.0

    constructor(maxDistance: Double) : this() {
        distance = maxDistance
    }
}