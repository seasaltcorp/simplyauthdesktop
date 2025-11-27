# SimplyAuth Desktop

<div>
  <img src="https://github.com/salomaoface/simplyauthdesktop/blob/main/assets/logo.jpg?raw=true" width="180" alt="SimplyAuth Logo"/>
  <h1>Beautiful, Secure, Offline-First Authentication for Compose Desktop</h1>

  <p>
    <strong>Login • Sign Up • Password Recovery • Dark/Light Theme • Local SQLite • Argon2id</strong>
  </p>

  <p>
    <a href="https://github.com/salomaoface/simplyauthdesktop/stargazers">
      <img src="https://img.shields.io/github/stars/salomaoface/simplyauthdesktop?style=for-the-badge&color=ff79c6" alt="GitHub stars"/>
    </a>
    <a href="https://github.com/salomaoface/simplyauthdesktop/releases">
      <img src="https://img.shields.io/github/v/release/salomaoface/simplyauthdesktop?style=for-the-badge&color=8b5cf6" alt="GitHub Release"/>
    </a>
    <a href="https://github.com/salomaoface/simplyauthdesktop/blob/main/LICENSE">
      <img src="https://img.shields.io/github/license/salomaoface/simplyauthdesktop?style=for-the-badge&color=6ee7b7" alt="License"/>
    </a>
    <a href="https://central.sonatype.com/search?q=br.com.simply">
      <img src="https://img.shields.io/maven-central/v/br.com.simply/simplyauthdesktop-compose?style=for-the-badge&color=3b82f6" alt="Maven Central"/>
    </a>
  </p>

[//]: # (  <br/>)

[//]: # (  <img src="https://github.com/salomaoface/simplyauthdesktop/blob/main/assets/demo.gif?raw=true" alt="SimplyAuth Desktop in action" width="100%"/>)

[//]: # (  <br/><br/>)

<strong>The only ready-to-use, production-grade authentication library for <code>Compose for Desktop</code> — completely offline, zero backend required.</strong>
</div>

## Why SimplyAuth Desktop?

| Problem                                      | SimplyAuth solves it                                   |
|---------------------------------------------|---------------------------------------------------------|
| "I need login but don't want a backend"     | Local SQLite + Argon2id hashing                         |
| "I want beautiful auth screens fast"         | Ready-made Material3 login, signup & recovery screens |
| "Users want dark mode"                       | Built-in dark/light toggle with sun/moon icon          |
| "I just want to ship"                        | Add in 5 lines of code                                 |

## Installation

```kotlin
// build.gradle.kts (Compose Desktop project)
dependencies {
    implementation("br.com.simply:simplyauthdesktop-compose:1.0.0")
    implementation("br.com.simply:simplyauthdesktop-sqldelight:1.0.0") // optional – bring your own DB if you want
}
```

## Usage – Just 5 lines in main.kt

````kotlin
fun main() = application {
    startKoin { modules(appModule) } // included in the library

    var isDark by remember { mutableStateOf(false) }

    Window(onCloseRequest = ::exitApplication, title = "My Awesome App") {
        AppTheme(useDarkTheme = isDark) {
            AuthNavigation(
                isDarkTheme = isDark,
                onThemeToggle = { isDark = !isDark },
                onAuthSuccess = { user ->
                    // Your main app starts here!
                    MyMainScreen(user)
                }
            )
        }
    }
}
````

#### That’s it — full authentication, beautiful UI, secure storage, theme toggle — all working out of the box.

## Features

- Email-based login
- Full registration (name + email + phone)
- Password recovery flow (ready for SMTP/Resend/SendGrid integration)
- Passwords hashed with **Argon2id** (the gold standard in 2025)
- Local SQLite database stored in ~/.simplyauthdesktop/app.db
- Dark / Light theme with smooth toggle
- 100% offline-first
- Clean Architecture + Koin + SQLDelight
- No Android, no KMP bloat — pure JVM Desktop

## Roadmap

- [ ] Real email delivery (SMTP / Resend / SendGrid)
- [ ] Email verification flow
- [ ] OAuth providers (Google, GitHub, etc.)
- [ ] `simplyauth-android` – native Android version
- [ ] `simplyauth-ios` – SwiftUI version
- [x] Secure password hashing with Argon2id
- [x] Beautiful Material3 screens (Login / Register / Recovery)
- [x] Dark / Light theme toggle
- [x] 100% offline-first with local SQLite
- [x] Zero backend required

<!-- links de apoio -->

## License
MIT License — free for commercial and open-source use.

---
<div>
      Made in Brazil by <a href="https://github.com/seasaltstudio">Sealsalt Studio</a>
</div>