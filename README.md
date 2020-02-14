Workshop on FP in kotlin using arrow 
================

# 1. Sequencing operations
* Using `Try<T>`
* Using `Either<Throwable, T>`
* Using `IO<T>`

## 1.1. Running all tests in this category
```
./gradlew test --tests com.github.devoxx.errorhandling.ConnectionsSpec
```

## 1.2. Running Try tests
```
./gradlew test -Dkotlintest.tags.include=TryTests --tests com.github.devoxx.errorhandling.ConnectionsSpec
```

## 1.3. Running Either tests
```
./gradlew test -Dkotlintest.tags.include=EitherTests --tests com.github.devoxx.errorhandling.ConnectionsSpec
```

## 1.4. Running IO tests
```
./gradlew test -Dkotlintest.tags.include=IOTests --tests com.github.devoxx.errorhandling.ConnectionsSpec
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

# 5. Monad Transformers
Using `OptionT`

```
./gradlew test --tests com.github.devoxx.monadtransformer.MonadTransformerExerciseSpec
```
