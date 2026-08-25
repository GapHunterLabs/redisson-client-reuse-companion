# Demo data — Redisson Client Reuse Companion

For capturing the real Marketplace screenshot:

1. `./gradlew runIde`
2. Open `demo/CacheService.java` as a scratch/standalone file (or drop
   it into any sandbox project) inside the sandbox IDE.
3. The `Redisson.create(config)` call inside `getUnsafely` shows the
   warning — hover it for the tooltip. `getSafely`'s constructor-
   assigned instance stays clean, for contrast.
4. Enter Full Screen (`View > Appearance > Enter Full Screen`), capture
   with `Win+Shift+S`, save directly to `docs/screenshots/` in this
   repo.
