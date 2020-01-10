package com.github.devoxx.state

import arrow.core.Id
import arrow.core.Option
import arrow.core.Tuple2
import arrow.core.extensions.id.monad.monad
import arrow.core.extensions.list.foldable.foldLeft
import arrow.mtl.State
import arrow.mtl.StateApi

/*
     * You can think of State monad of something like this:
     *
     * type State[S, A] = S => (S, A)
     *
     * For example, you can create instances of state with the following structure:
     *
     * val s = State[String, String] { originalString =>
     *   (originalString, originalString.toUpperCase)
     * }
     * s.runA("hello").value === "HELLO"
     *
     * */
typealias CalcState<A> = State<List<A>, Option<A>>

object ReversePolishNotation {
    /*
     * TODO 06
     * This function must support the symbols {+,-,*,/}
     *
     * If the symbol is a number
     *   stack-it
     * otherwise
     *   pop the last two symbols
     *   execute the operation
     *   stack the result
     *
     */
    fun evalOne(sym: String): CalcState<Int> {
        return when {
            "+" == sym -> operator { a, b -> a + b }
            "-" == sym -> operator { a, b -> a - b }
            "*" == sym -> operator { a, b -> a * b }
            "/" == sym -> operator { a, b -> a / b }
            Regex("^[-+]?[1-9]\\d*\$").matches(sym) -> operand(sym.toInt())
            else -> {
                System.err.println("fail!")
                State { list -> Tuple2(list, Option.empty()) }
            }
        }
    }

    private fun operator(f: (Int, Int) -> Int): CalcState<Int> =
            State { list ->
                when {
                    list.size >= 2 -> {
                        val ans = f(list.toList()[0], list.toList()[1])
                        Tuple2(listOf(ans, *list.subList(2, list.size).toTypedArray()), Option(ans))
                    }
                    else -> {
                        System.err.println("fail!")
                        Tuple2(list, Option.empty())
                    }
                }
            }

    private fun operand(i: Int): CalcState<Int> =
            State { list ->
                Tuple2(listOf(i, *list.toTypedArray()), Option(i))
            }

    /*
     * TODO 06: Fold over the list calling evalOne
     *
     */
    fun evalAll(sym: List<String>): CalcState<Int> =
            sym.foldLeft(StateApi.just(Option(0)))
            { acc, b ->
                acc.flatMap(Id.monad()) { evalOne(b) }
            }
}
