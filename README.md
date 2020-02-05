Workshop on functional programming in kotlin using arrow 
================

Workshop devoxx 2020

# Error accumulation
Using applicative to accumulate errors

```
./gradlew test --tests com.github.devoxx.formvalidator.UserValidationSpec
```

# Sequencing operations
* Using Try<T>
* Using Either<Throwable, T>

```
./gradlew test --tests com.github.devoxx.errorhandling.ConnectionsSpec
```

# Pure setters/getters
Using Lens

```
./gradlew test --tests com.github.devoxx.optics.LensExerciseSpec
```

# Pure state management
Using State Monad

```
./gradlew test --tests com.github.devoxx.state.StateSpec
```
