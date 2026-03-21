This is a Kotlin Multiplatform project targeting Android, iOS, and server.

## Bootstrap From Clone

Prerequisites:

- JDK 17
- Android SDK with API 36 installed
- Xcode for the iOS host app
- Node.js and npm for repository-local commit tooling

From a fresh clone:

```shell
git clone <your-remote-url>
cd kolo
npm install
git config core.hooksPath .githooks
```

Build Android:

```shell
./gradlew :androidApp:assembleDebug
```

Run the server:

```shell
./gradlew :server:run
```

Open the iOS app in Xcode:

```shell
open iosApp/iosApp.xcodeproj
```

Recommended verification before you start work:

```shell
./gradlew :build-logic:convention:jar :shared:core:store:impl:jvmTest :androidApp:assembleDebug :shared:compileKotlinIosSimulatorArm64
```

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
    folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/server](./server/src/main/kotlin) is for the Ktor server application.

* [/shared](./shared/src) is for the code that will be shared between all targets in the project.
  The most important subfolder is [commonMain](./shared/src/commonMain/kotlin). If preferred, you
  can add code to the platform-specific folders here too.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :androidApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :androidApp:assembleDebug
  ```

### Build and Run Server

To build and run the development version of the server, use the run configuration from the run widget
in your IDE’s toolbar or run it directly from the terminal:
- on macOS/Linux
  ```shell
  ./gradlew :server:run
  ```
- on Windows
  ```shell
  .\gradlew.bat :server:run
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Commit Convention

This repo uses Conventional Commits.

Examples:

- `feat(shared): add reminder scheduling contract`
- `fix(androidApp): correct back press handling`
- `docs(planning): clarify persistence workstream`
- `chore(build-logic): align plugin wiring`

Breaking changes must use `!` or a `BREAKING CHANGE:` footer, for example:

- `feat(shared)!: rename store action API`

To enable the local commit message hook once per clone:

```shell
npm install
git config core.hooksPath .githooks
```

GitHub Actions also lints commits on pull requests and pushes to `main`.

### Versioning

The repo uses Semantic Versioning for the shared release version.

- Gradle source of truth: `kolo.version.name` and `kolo.version.code` in `gradle.properties`
- Android reads both values from Gradle properties
- Server reads `kolo.version.name` from Gradle properties
- iOS `MARKETING_VERSION` is currently mirrored in `iosApp/Configuration/Config.xcconfig`

Use these bump rules:

- patch: backwards-compatible bug fix
- minor: backwards-compatible feature
- major: breaking change

Current baseline:

- release version: `0.1.0`
- build number: `1`
