---
name: tackle-component-tests
description: Write or update unit tests for Decompose components in the Tackle Kotlin Multiplatform project. Use when testing component models, child navigation, child item creation, ComponentOutput propagation, lifecycle cleanup, or component method wiring.
---

# Tackle Decompose Component Tests

Read `AGENTS.md` first for component tree, output flow, and testing conventions.

## Base Pattern

1. Put tests in `src/commonTest/kotlin/.../integration`.
2. Extend `ComponentTest<XComponent>`.
3. Create the component with `DefaultComponentContext(lifecycle)`.
4. Use `DefaultStoreFactory()` for store-backed components.
5. Pass `testDispatchers` from `ComponentTest`.
6. Pass `output = { componentOutput.add(it) }` or the matching callback name.
7. Read current state from `component.model.value`.
8. `ComponentTest.setUp()` resumes the lifecycle; `tearDown()` clears outputs and resets main-thread assertion behavior.

## What To Test

- Component creation and initial model.
- Public component methods map to the right store intents or child behavior.
- Store labels become `ComponentOutput`.
- Errors become `ComponentOutput.Common.ErrorCaught`.
- Parent-handled outputs are consumed locally when intended.
- Forwarded outputs reach `componentOutput`.
- Navigation values update: `childStack`, `ChildSlot`, `LazyChildItems`.
- Child factory methods create children with correct inputs, when the method is internal and already tested in the project.

## Navigation Checks

- For stack components, assert `childStack.value.active.instance`.
- For slots, trigger activation/dismissal and assert `slot.child?.instance`.
- For lists, read `component.items.value.items` and `activeItems`.
- Use existing tests such as `StatusListComponentTest`, `AuthComponentTest`, and editor child component tests as templates.

## Mocking

- Mock gateway interfaces with Mokkery.
- Stub settings/tools/database narrowly.
- Do not instantiate platform factories or real network/database modules in component tests.

## Verification

Run `.\gradlew.bat testDebugUnitTest` after changing tests. Run `.\gradlew.bat assembleDebug` if component wiring changed.

