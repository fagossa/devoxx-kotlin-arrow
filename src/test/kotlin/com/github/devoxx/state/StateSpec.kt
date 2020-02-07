package com.github.devoxx.state

import arrow.core.*
import arrow.core.extensions.id.monad.monad
import arrow.mtl.State
import arrow.mtl.extensions.fx
import com.github.devoxx.state.ReversePolishNotation.evalAll
import com.github.devoxx.state.ReversePolishNotation.evalOne
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec

class StateSpec : StringSpec({
    "should support simple values" {
        // TODO 06: make this test pass
        evalOne("42").runA(Id.monad(), ListK(emptyList())).value().shouldBe(Some(42))
    }

    "must handle the '+' operator" {
        // TODO 06: make this test pass
        State.fx<ForId, List<Int>, Option<Int>>(Id.monad()) {
            !evalOne("1")
            !evalOne("2")
            !evalOne("+")
        }.runA(Id.monad(), ListK(emptyList())).value().shouldBe(Some(3))
    }

    "must handle the '-' operator" {
        // TODO 06: make this test pass
        evalOne("2").flatMap(Id.monad()) {
            evalOne("1").flatMap(Id.monad()) {
                evalOne("-")
            }
        }.runA(Id.monad(), ListK(emptyList())).value().shouldBe(Some(-1))
    }

    "must handle the '*' operator" {
        // TODO 06: make this test pass
        evalOne("3").flatMap(Id.monad()) {
            evalOne("5").flatMap(Id.monad()) {
                evalOne("*")
            }
        }.runA(Id.monad(), ListK(emptyList())).value().shouldBe(Some(15))
    }

    "must handle the '/' operator" {
        // TODO 06: make this test pass
        evalOne("5").flatMap(Id.monad()) {
            evalOne("15").flatMap(Id.monad()) {
                evalOne("/")
            }
        }.runA(Id.monad(), ListK(emptyList())).value().shouldBe(Some(3))
    }

    "must handle multiple '+'" {
        // TODO 06: make this test pass
        State.fx<ForId, List<Int>, Option<Int>>(Id.monad()) {
            !evalOne("1")
            !evalOne("1")
            !evalOne("+")
            !evalOne("1")
            !evalOne("+")
        }.runA(Id.monad(), ListK(emptyList())).value().shouldBe(Some(3))
    }

    "accumulate multiple values" {
        // TODO 06: make this test pass
        evalAll(listOf("1", "2", "+", "3", "*")).runA(Id.monad(), ListK(emptyList())).value().shouldBe(Some(9))
    }
})
