---
name: tackle-decompose
description: Create or update Decompose components and navigation in the Tackle Kotlin Multiplatform project. Use when adding a new screen/component, wiring component gateways, adding child stacks/slots/items, connecting a component to Compose UI, or propagating ComponentOutput events.
---

# Tackle Decompose

Read `AGENTS.md` first for the full component tree, DI rules, event propagation, UI wiring, and validation commands.

## Component Shape

1. Create or update a component module under `shared/component/...`.
2. Define the public component interface in the module root package. Expose `Value<Model>`, child components/navigation values, and UI event methods.
3. Define `XComponentGateways` for dependencies the component needs.
4. Implement `integration/XComponentDefault` with constructor-injected gateways, `StoreFactory`, `TackleDispatchers`, and output callbacks.
5. Use `ComponentContext by componentContext`.
6. Retain stores with `instanceKeeper.getStore { XStoreProvider(...).create() }`.
7. Convert store state with `store.asValue().map(...)`.
8. Collect store labels in a `CoroutineScope(dispatchers.main)` and cancel it in `lifecycle.doOnDestroy`.
9. Add `XComponentPreview` if the component is rendered by Compose previews.

## Navigation

- Root-level screens belong in `RootComponentDefault.Config` and `RootComponent.Child`.
- Main tabs belong in `MainComponentDefault.Config` and `MainComponent.Child`.
- Dialog-like single child uses `SlotNavigation` plus `childSlot`.
- Lists of dynamic child components use `ItemsNavigation`/`childItems`, as in `StatusListComponent`.
- Permanent child components can use `childContext(key = "...")`, as in Home and Editor.
- Use unique keys when a component owns multiple child contexts/navigation models.
- Config classes should be `@Serializable` when used by Decompose navigation state.
- Navigation methods must be called from the main thread.

## Outputs And Errors

- Children communicate upward with `(ComponentOutput) -> Unit`.
- Add new output types in `shared/domain/ComponentOutput.kt` only for events an ancestor must handle.
- Convert store error labels to `ComponentOutput.Common.ErrorCaught`.
- Root is the only place that turns errors into snackbar/logout behavior.
- If a parent can handle an output locally, do it there and only forward the rest.

## Compose Connection

- UI functions live in `shared/compose`; components must not import Compose UI.
- In Compose, subscribe to `Value<Model>` and navigation values with `subscribeAsState()`.
- Use Decompose Compose extensions: `ChildStack`, `Children`, `ChildSlot`, and `ChildItemsLifecycleController`.
- UI should call component methods only. Do not call stores/managers/API/settings directly from Compose.

## New Screen Checklist

1. Add Gradle module in `settings.gradle.kts` if this is a new module.
2. Add `build.gradle.kts` with convention plugins and dependencies matching neighboring modules.
3. Add component interface, gateways, default integration, preview, optional store/domain manager.
4. Wire dependency adapters in the parent/root module that owns the real dependency.
5. Add navigation config and output handling in the parent.
6. Add Compose content and route it from parent UI.
7. Add component tests with `$tackle-component-tests`; add store tests with `$tackle-mvi-tests` if there is a store.

## Verification

- Run `.\gradlew.bat testDebugUnitTest` when tests changed.
- Run `.\gradlew.bat assembleDebug`.
- Run `.\gradlew.bat detekt` before finishing.

