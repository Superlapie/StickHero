# StickHero

StickHero is a native Android/Kotlin stick-figure fighting prototype. It uses Android `Canvas` rendering for the platform layer and plain Kotlin systems for game rules.

## Build

Open the repository in Android Studio and run the `app` configuration, or build from a machine with Java, Gradle, and the Android SDK installed:

```sh
gradle :app:assembleDebug
```

## Architecture

- `com.stickhero.MainActivity` starts the game view only.
- `platform.android` owns Android surface, timing, Canvas rendering, lifecycle, and touch mapping.
- `game.*` packages are pure Kotlin and contain match flow, fighters, combat, AI, animation state, input, physics, config, and render snapshots.
- The renderer draws only from `RenderSnapshot`; combat and movement rules live in game systems.

## Prototype Controls

- Left button: move left.
- Right button: move right.
- ATK button: melee attack.
- Restart appears after win or loss.
