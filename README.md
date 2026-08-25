# Redisson Client Reuse Companion

Gutter warning icon on a `Redisson.create(...)` call built inside a
regular method body — Redisson's own Getting Started guide states:
"RedissonClient is thread-safe, so create a single instance and reuse
it as a shared singleton across your application." A client built
inside a method means a brand new connection pool to the Redis/Valkey
server gets created on every call.

## Why it exists

`RedissonClient redisson = Redisson.create(config);` compiles fine
and returns a working client — call it once per request handler and
every single call quietly spins up a new connection pool to Redis,
instead of reusing the one client instance the application already
has.

## Why built this way

- **100% static text/PSI analysis** — matches the class/method name by
  simple text, so it works whether the real Redisson jar is on the
  classpath or not. Java and Kotlin.
- **Confirmed gap**: every Redis-related plugin found on Marketplace
  (Redis Helper, Redis Manager, Redis Client, Redis-Cli) is a data
  browser/client tool — none check `RedissonClient` reuse in
  application code.

## v0.1 scope — stated honestly, not exhaustively

Only flags the direct `Redisson.create(...)` call — matches by simple
class/method name, not real type resolution. Never flags a call
inside a constructor (a legitimate "create once" location).

## Usage

Open any Java/Kotlin file using Redisson. A client built inside a
regular method shows a warning icon.

## Enterprise / Team Licensing

Need enterprise features, custom rules, or team licensing? Contact us at
**gaphunterlabs@gmail.com**.

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
