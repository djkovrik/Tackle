---
name: tackle-mvi
description: Create or update MVIKotlin stores in the Tackle Kotlin Multiplatform project. Use when adding a new component store, changing store intents/state/labels, wiring a domain manager into a store, or adapting store state to a Decompose component model.
---

# Tackle MVIKotlin

Read `AGENTS.md` first for project architecture, module boundaries, DI, error flow, and validation commands.

## Workflow

1. Locate the closest existing store pattern: auth, editor, status, or statuslist.
2. Keep the store internal to the component module: `internal interface XStore : Store<Intent, State, Label>` and `internal class XStoreProvider`.
3. Model the store API as `Intent` for input, immutable `State` for durable UI state, and `Label` for one-shot effects.
4. Put business/API orchestration in an internal manager that returns `Result<T>` via `runCatching`.
5. Inject narrow component gateways into the manager. Do not inject concrete network/database/settings implementations unless the module owns that boundary.
6. Build with `storeFactory.create<Intent, Action, Msg, State, Label>`.
7. Use `coroutineBootstrapper(mainContext)` only for initial work. Keep bootstrap actions explicit.
8. Use `coroutineExecutorFactory(mainContext)` for intents/actions and switch to `withContext(ioContext)` for IO/API/database work.
9. Use `unwrap(result, onSuccess, onError)` and publish `Label.ErrorCaught(...)` on failures that must reach Root.
10. Mutate state only in the reducer with `copy(...)`.
11. Add or update `integration/Mappers.kt` to map `Store.State` to component `Model` when the mapping is non-trivial.

## Project Rules

- Store names are stable strings, usually `"XStore"` or `"XStore_$id"` for item stores.
- `create(autoInit: Boolean = true)` must support `autoInit = false` for tests.
- Keep `Intent`, `State`, and `Label` nested in the store interface.
- Keep `Action` for internal bootstrap/reusable executor actions and `Msg` for reducer inputs.
- Prefer optimistic updates only when existing local behavior supports rollback, as in `StatusStore`.
- For errors, publish a label and let the component convert it to `ComponentOutput.Common.ErrorCaught`.
- For parent navigation, publish a semantic label, then convert it in the component to `ComponentOutput`.

## Framework Notes

MVIKotlin stores expose `states` and `labels` as coroutine flows through `mvikotlin-extensions-coroutines`. Stores, bootstrapper dispatch, executor dispatch, labels, and reducer work are main-thread oriented; async work should switch context and return to the main executor before dispatching.

## Verification

- Add/update store tests with `$tackle-mvi-tests`.
- Run `.\gradlew.bat testDebugUnitTest` when tests changed.
- Run `.\gradlew.bat assembleDebug` and final `.\gradlew.bat detekt` for broader validation.

