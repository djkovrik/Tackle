---
name: tackle-mvi-tests
description: Write or update unit tests for MVIKotlin stores in the Tackle Kotlin Multiplatform project. Use when testing store bootstrap, intents, reducer state, labels, error labels, optimistic updates, or pagination behavior.
---

# Tackle MVI Store Tests

Read `AGENTS.md` first for project testing conventions and store architecture.

## Base Pattern

1. Put tests in the component module's `src/commonTest/kotlin/.../store`.
2. Extend `StoreTest<XStore.Intent, XStore.State, XStore.Label>`.
3. Use `DefaultStoreFactory()`.
4. Pass `Dispatchers.Unconfined` for `mainContext` and `ioContext`.
5. Create the store with `create(autoInit = false)`.
6. Call `setUp()` in `@BeforeTest` and `tearDown()` in `@AfterTest`.
7. Call `store.init()` inside tests that assert bootstrap behavior.
8. Assert state through `store.state`.
9. Assert labels through the inherited `labels` list.

## Mocking

- Use Mokkery `mock`, `every`, `everySuspend`, `returns`, `throws`, and `verifySuspend`.
- Stub gateway interfaces, not concrete network/database/settings implementations.
- Use mutable settings stubs for settings-like state.
- Keep response fixtures or constants in `Responses.kt` and `Constants.kt` near the tests.

## What To Test

- Initial state before/after `store.init()`.
- Bootstrap success and failure.
- Each user-facing intent.
- State flags for loading/progress/empty/error.
- One-shot labels, especially `ErrorCaught` and navigation labels.
- Rollback behavior for optimistic updates.
- Pagination edge cases: first page, next page, last page, failed page.
- Cross-state derived behavior such as selected votes, text counters, menu actions, and availability flags.

## Assertions

- Use assertk.
- Prefer specific state and label assertions over broad non-empty checks.
- Use `runTest` for coroutine tests.
- Avoid timing sleeps. If a manager exposes delay, stub it to `0L`.

## Verification

Run `.\gradlew.bat testDebugUnitTest` after changing tests. Run `.\gradlew.bat detekt` before final handoff if code was changed.

