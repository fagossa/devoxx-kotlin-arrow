devoxx-kotlin-arrow
================


Workshop devoxx 2020

# <img alt="Kotlin" src="https://kotlinlang.org/assets/images/open-graph/kotlin_250x250.png" height="60"/> <img alt="Arrow" src="https://avatars0.githubusercontent.com/u/29458023?s=400&v=4" height="50"/> KotlinKatas [![Build Status](https://travis-ci.com/fagossa/devoxx-kotlin-arrow.svg?branch=master)](https://travis-ci.com/fagossa/devoxx-kotlin-arrow)

Kotlin training repository used to learn Kotlin and Functional Programming by solving some common katas using just purely functional programming with [Arrow](https://github.com/arrow-kt/arrow).

### List of katas:


| # | Kata Statement | PR | Topic |
|---|----------------|----|-------|
| 1 | [Maxibons](https://github.com/Karumi/MaxibonKataJava#-kata-maxibon-for-java-) | [https://github.com/pedrovgs/KotlinKatas/pull/1](https://github.com/pedrovgs/KotlinKatas/pull/1) | Polymorphic programming |
| 2 | [Form validation](https://gist.github.com/pedrovgs/d83fe1f096928715a6f31946e557995a) | [https://github.com/pedrovgs/KotlinKatas/pull/4](https://github.com/pedrovgs/KotlinKatas/pull/4) | Validated data type|
| 3 | [Game of Life](https://en.wikipedia.org/wiki/Conway%27s_Game_of_Life) | [https://github.com/pedrovgs/KotlinKatas/pull/6](https://github.com/pedrovgs/KotlinKatas/pull/6) | Pure functions, IO monad complex property-based testing generators|

### Executing tests:

This project contains some tests written using [KotlinTest](https://github.com/kotlintest/kotlintest). You can easily run the tests by executing any of the following commands:

```
./gradlew test // Runs every test in your project
./gradlew test --tests *SomeSpecificTest // Runs specs matching with the filter passed as param.
```

### Checkstyle:

For the project checkstyle we are using [Ktlint](https://github.com/shyiko/ktlint). The code format will be evaluated after accepting any contribution to this repository using this tool. You can easily format your code changes automatically by executing ``./gradlew ktlinTformat``.
