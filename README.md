# Andromeda Library

The Andromeda Library provides you access to the APIs included in the [Andromeda Server][andromeda-play-store]. This library is made specifically to work with Kotlin - it technically will work with Java, but not pleasantly.

# Installation
[![](https://jitpack.io/v/prjkt-io/lib-andromeda.svg)](https://jitpack.io/#prjkt-io/lib-andromeda)

Add in your root `build.gradle` at the end of repositories
```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

Add the dependency to your project `build.gradle`
```groovy
dependencies {
    implementation 'com.github.prjkt-io:lib-andromeda:[latest_version]'
}
```

# Usage

Request for explicit permission on an activity:
```kotlin
requestPermissions(
      arrayOf(AndromedaClient.ACCESS_PERMISSION),
      ANDROMEDA_REQUEST_CODE_PERMISSION
)
```

Initialize the the app to use the APIs:
```kotlin
AndromedaClient.initialize(this)
```

You're set! From here you can call any methods from `Andromeda*` classes.

There is an example app (`:example`) to check/play with. Also checkout the full documentation [here][doc].

[andromeda-play-store]: <https://play.google.com/store/apps/details?id=projekt.andromeda>
[doc]: <https://docs.prjkt.io/lib-andromeda/>