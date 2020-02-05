Workshop on FP in kotlin using arrow 
================

Workshop devoxx 2020

# 1. Sequencing operations
* Using `Try<T>`
* Using `Either<Throwable, T>`

```
./gradlew test --tests com.github.devoxx.errorhandling.ConnectionsSpec
```

# 2. Pure setters/getters
Using `Lens`

```
./gradlew test --tests com.github.devoxx.optics.LensExerciseSpec
```

# 3. Error accumulation
Using `applicative` to accumulate errors

```
./gradlew test --tests com.github.devoxx.formvalidator.UserValidationSpec
```

# 4. Pure state management
Using `State Monad`

```
./gradlew test --tests com.github.devoxx.state.StateSpec
```
