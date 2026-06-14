# Tackle Agent Guide

Tackle is a Kotlin Multiplatform Mastodon client in active development. Treat this file as the source of truth for project orientation and for adding new screens/components.

## Project Shape

- `composeApp` is the platform application module. It owns Android, Desktop, and iOS app entry points and wires the root component into Compose UI.
- `iosApp` is the native SwiftUI shell. It depends on the `ComposeApp` framework and hosts `MainViewController()` from Kotlin.
- `shared:domain` contains public interfaces, domain models, exceptions, and cross-component outputs.
- `shared:network`, `shared:database`, and `shared:settings` implement domain APIs and expose them through `NetworkModule`, `DatabaseModule`, and `SettingsModule`.
- `shared:component:*` modules contain Decompose components and MVIKotlin stores. Most component modules follow: interface, gateways, `integration/*Default`, optional `*Preview`, store, domain manager, tests.
- `shared:compose` contains Compose Multiplatform UI. UI depends on component interfaces and calls component methods; components do not depend on UI.
- `shared:utils` contains common utilities, extensions, and test base classes.
- `gradle/build-logic/convention` defines `tackle.config.android`, `tackle.config.multiplatform`, and `tackle.config.compose`.

Modules are declared in `settings.gradle.kts`. New shared modules normally use `id("tackle.config.android")` and `id("tackle.config.multiplatform")`; Compose UI modules also use `id("tackle.config.compose")`.

## Platform Entry Points

- Android starts in `composeApp/src/androidMain/kotlin/com/sedsoftware/tackle/MainActivity.kt`. It initializes FileKit, creates `AndroidCodeAuthFlowFactory`, builds `RootComponentFactory(defaultComponentContext(), context, platformTools, authFlowFactory, dispatchers)`, registers the auth factory with the Activity, and calls `RootContent(root)` inside `TackleTheme`.
- `composeApp/src/androidMain/kotlin/com/sedsoftware/tackle/TackleApp.kt` configures Sketch singleton image loading.
- Desktop starts in `composeApp/src/desktopMain/kotlin/main.kt`. It creates a `LifecycleRegistry`, builds `RootComponentFactory(DefaultComponentContext(lifecycle), ...)`, configures Sketch, then hosts `RootContent(root)` inside `Window`.
- iOS starts in `iosApp/iosApp/iOSApp.swift` and `ContentView.swift`. SwiftUI hosts `MainViewControllerKt.MainViewController()`.
- iOS Compose entry is `composeApp/src/iosMain/kotlin/MainViewController.kt`. It creates a static `LifecycleRegistry`, `RootComponentFactory(DefaultComponentContext(lifecycle), ...)`, configures Sketch, and returns `ComposeUIViewController { RootContent(root) }`.
- iOS also sets `ViewControllerFactory.shared = TheViewControllerFactory()` so Kotlin `ComposeGlobals` can create a native image `UIViewController`.
- Platform-specific `DefaultDispatchers` and `PlatformToolsFactory` live under `composeApp/src/<platform>Main/kotlin/com/sedsoftware/tackle`.

## Dependency Injection

DI is manual and constructor-based.

- `RootComponentFactory` is the composition root. Platform code passes `ComponentContext`, `TacklePlatformTools`, `CodeAuthFlowFactory`, and `TackleDispatchers`.
- `SettingsModule` wraps platform `Settings` from `SharedSettingsFactory` and exposes `TackleSettings`.
- `DatabaseModule` wraps platform `SqlDriver`, an IO coroutine context, and a current-domain provider; it exposes `TackleDatabase`.
- `NetworkModule` receives `CodeAuthFlowFactory`, `domainProvider`, and `tokenProvider`; it exposes `UnauthorizedApi`, `AuthorizedApi`, and `OAuthApi`.
- `RootComponentDefault` receives domain interfaces plus `StoreFactory`, then adapts them into component-specific gateway interfaces.
- Component modules should not reach into global singletons. Define narrow `*Gateways.Api`, `*Gateways.Settings`, `*Gateways.Database`, or `*Gateways.Tools` interfaces in the component module, then implement adapters near the parent/root module that has the real dependency.

## Domain Layer

Key domain interfaces are in `shared/domain/src/commonMain/kotlin/com/sedsoftware/tackle/domain/api`:

- `UnauthorizedApi`: public/unauthenticated Mastodon calls and direct file download.
- `AuthorizedApi`: bearer-token Mastodon calls.
- `OAuthApi`: OIDC authorization flow.
- `TackleDatabase`: local cache operations.
- `TackleSettings`: persisted app settings such as domain, client credentials, token, current account, emoji cache timestamp, and last language.
- `TacklePlatformTools`: browser/open URL, client registration data, locales, sharing.
- `TackleDispatchers`: `main`, `io`, `unconfined`.

Errors use `TackleException`. Network, remote server, serialization, validation, and file errors declare an `action`: `SHOW_MESSAGE`, `LOGOUT`, or `NONE`. Components publish errors as `ComponentOutput.Common.ErrorCaught`; `RootComponentDefault` feeds them to `TackleExceptionHandler`. `RootContent` collects `root.errorMessages` and maps them with `exceptionToString`.

`ComponentOutput` is the cross-component event bus. Add new navigation or parent-level events there only when a child must ask an ancestor to do something.

## Settings

`TackleSettingsInternal` stores values through `multiplatform-settings` with compact keys. Platform factories:

- Android: `SharedPreferencesSettings(PreferenceManager.getDefaultSharedPreferences(context.applicationContext))`
- Desktop: `PreferencesSettings(Preferences.userRoot())`
- iOS: `NSUserDefaultsSettings(NSUserDefaults.standardUserDefaults())`

When adding a setting, update `TackleSettings`, `TackleSettingsInternal`, and any component gateway settings adapter. Prefer exposing only the property a component needs through its gateway.

## Database

The database module uses SQLDelight.

- Schema: `shared/database/src/commonMain/sqldelight/com/sedsoftware/tackle/database/TackleAppDatabase.sq`
- Real implementation: `TackleSharedDatabase`
- Test/in-memory implementation: `TackleTestDatabase`
- Current tables cache server emojis and instance info by domain.
- Platform drivers:
  - Android: `AndroidSqliteDriver`, `TackleDatabase.db`
  - Desktop: `JdbcSqliteDriver` under `java.io.tmpdir`, creates schema manually
  - iOS: `NativeSqliteDriver`, currently named `NexusDatabase.db`

When adding cache data, update the `.sq` file, mappers, `TackleDatabase` if the operation is public, `TackleSharedDatabase`, and `TackleTestDatabase`. Use SQLDelight coroutine extensions (`asFlow`, `mapToList`, `mapToOne`) with the injected coroutine context.

## Network And Mastodon API

The network module uses Ktor 3 and kotlinx.serialization.

- `BaseApi` owns `HttpClient`, JSON config (`coerceInputValues`, `ignoreUnknownKeys`, `useAlternativeNames=false`), common headers, bearer header injection, response status handling, and exception wrapping.
- `TackleUnauthorizedApi` calls public endpoints and direct downloads. Pass `authenticated = false` to `doRequest`.
- `TackleAuthorizedApi` calls authenticated endpoints. Pass `authenticated = true`; bearer token comes from `tokenProvider`.
- `TackleOAuthApi` builds an `OpenIdConnectClient` with Mastodon authorization/token endpoints and uses platform `CodeAuthFlowFactory`.
- Response DTOs are internal `@Serializable` classes in `network/response`.
- Request DTOs are internal `@Serializable` classes in `network/request`.
- Mappers live in `network/mapper` and convert DTOs to domain models.
- JSON fixtures live in `shared/network/src/commonTest/resources/responses`, with mapper tests using `JsonBasedTest.responseFromFile`.

When adding a REST API call:

1. Check current Mastodon docs at `https://docs.joinmastodon.org/api/` and `https://docs.joinmastodon.org/methods/`.
2. Add the method to `AuthorizedApi` or `UnauthorizedApi` based on whether bearer auth is required. Add Javadoc with the exact Mastodon doc link.
3. Add request/response DTOs with `@SerialName` and sensible defaults for optional fields.
4. Add mapper(s) to domain models. Keep DTOs internal.
5. Implement the method in `TackleAuthorizedApi` or `TackleUnauthorizedApi` using `doRequest`.
6. If a component needs the call, expose it through that component's gateway interface and adapter, not directly through the concrete API.
7. Add fixture-based mapper tests. Add manager/store/component tests if the call changes screen behavior.

## OAuth Authorization

Auth is centered in `shared/component/auth`.

- `AuthStore` bootstraps by checking existing credentials.
- If settings miss domain/token, `MissedRegistrationData` is treated as unauthorized.
- Text input is debounced through `AuthFlowManager.getTextInputEndDelay()`, trimmed/normalized, and used to load instance info.
- `AuthFlowManager.createApp(domain)` stores `domainNormalized`, `domainShort`, `clientId`, and `clientSecret`.
- `AuthFlowManager.startAuthFlow(credentials)` calls `OAuthApi.startAuthFlow(...)` and stores the token.
- Successful auth publishes `ComponentOutput.Auth.AuthFlowCompleted`; root replaces Auth with Main.
- Android must call `authFlowFactory.registerActivity(this)` after creating the root.

## Decompose Component Tree

Root:

- `RootComponentDefault` owns a `StackNavigation<Config>` with `Auth`, `Main`, `Editor`, `ViewImage`, `ViewVideo`, `ViewHashTagTimeline`.
- It also owns `SlotNavigation<AlternateTextConfig>` for the alternate text bottom sheet.
- Root starts at `Config.Auth`.
- Auth success replaces current with `Main`.
- Home new post pushes `Editor`.
- Published status pops editor and calls `MainComponent.showCreatedStatus`.
- Single status outputs can open media viewer, hashtag timeline, or alternate text.
- `ErrorCaught` goes to `TackleExceptionHandler`; `401` remote errors trigger logout by replacing current with `Auth`.

Main:

- `MainComponentDefault` owns a tab `StackNavigation<Config>` with Home, Explore, Publications, Notifications, Profile.
- Bottom tab clicks call `bringToFront`.
- Home owns a permanent `StatusListComponent` child via `childContext`.
- Publications owns local/remote timeline children through a nested stack.
- Explore/Profile/Notifications are currently simple placeholder components with `MutableValue`.

Status list and status:

- `StatusListComponent` is a screen/list component. It owns a `StatusListStore` and Decompose `LazyChildItems<Status, StatusComponent>`.
- Each status row is a `StatusComponent`; it is not a screen, it represents a single status item.
- `StatusListComponentDefault` listens to store `items` and calls `ItemsNavigation.setItems`. Child status outputs are either handled locally (`Deleted`) or propagated upward.
- `StatusComponentDefault` wraps status actions and context menu actions into store intents; URL/hash/mention/media/alternate-text actions publish `ComponentOutput.SingleStatus`.

Editor:

- `EditorComponentDefault` is a composed component with its own `EditorStore` plus child components: attachments, emojis, header, poll, warning, and attachment details slot.
- Child components communicate back through `ComponentOutput.StatusEditor`.
- The editor coordinates cross-child availability, for example attachments vs poll, send availability, and attachment details dialog.
- On send, editor builds `NewStatusBundle` from its own model and child models, then passes it to `EditorStore.Intent.SendStatus`.

## MVIKotlin Pattern

Most stores use:

- `internal interface XStore : Store<Intent, State, Label>`
- public `Intent`, immutable `State`, one-shot `Label`
- `internal class XStoreProvider(...): create(autoInit: Boolean = true)`
- `storeFactory.create<Intent, Action, Msg, State, Label>(...)`
- `coroutineBootstrapper(mainContext)` for initial load if needed
- `coroutineExecutorFactory(mainContext)` for intents/actions
- `withContext(ioContext)` for IO/domain/network work
- `Result` returning managers plus `unwrap(result, onSuccess, onError)`
- private `Action` and `Msg`
- reducer as the only place that mutates state

Do not use singleton bootstrappers/executors. MVIKotlin stores are main-thread oriented; tests disable main-thread assertions and use `Dispatchers.Unconfined`.

Use `utils.extension.asValue()` to expose store state as Decompose `Value`, then map state to component `Model` in `integration/Mappers.kt` or a private function.

## Compose UI

Compose code lives under `shared/compose`.

- Entry UI is `RootContent`.
- UI subscribes to component `Value<Model>` with `subscribeAsState()`.
- Navigation rendering uses Decompose Compose extensions: `ChildStack`, `Children`, `ChildSlot`, and `ChildItemsLifecycleController`.
- UI calls component methods and should not call stores, managers, network, database, or settings directly.
- Previews use `*ComponentPreview` classes from component modules.
- Shared theme/resources live in `TackleTheme`, `composeResources`, widgets, extensions, and models.
- Use existing widgets (`TackleButton`, `TackleIconButton`, `TackleTextField`, `TackleImage`, etc.) before adding new UI primitives.

## Utils

Important utilities:

- `StoreExt.asValue()`: adapts MVIKotlin store state to Decompose `Value`.
- `Unwrap.unwrap()`: dispatch success/error from `Result`.
- `PrimitivesExt`: URL normalization/validation, date parsing fallback, nullable defaults, file size formatting, duration formatting, media focus conversion.
- `Fields.kt`: `Throwable.isUnauthorized`, `PlatformFileWrapper.isAudio/isImage/isVideo`.
- `DateTimeUtils`: picker conversion, UTC conversion, short relative labels, display formatting.
- `StringUtils`: HTML text extraction/decoding and YouTube ID extraction.
- `FileUtils`: extension to MIME type mapping.
- `PlatformUtils.generateUUID()`: expect/actual UUID.
- `TackleRegex`: URL, hashtag/mention, emoji regex.
- Test helpers: `StoreTest`, `ComponentTest`, `JsonBasedTest`.

## Testing

Use common tests unless platform-specific behavior forces otherwise.

- Store tests extend `StoreTest<Intent, State, Label>`.
  Create stores with `DefaultStoreFactory`, `Dispatchers.Unconfined`, and `create(autoInit = false)`. Call `store.init()` explicitly in tests that assert bootstrap behavior.
- Component tests extend `ComponentTest<Component>`.
  Create `DefaultComponentContext(lifecycle)`, pass `testDispatchers`, collect outputs into `componentOutput`, and assert `component.model.value`.
- Domain manager tests use Mokkery mocks and verify API/tool calls plus `Result` behavior.
- Network mapper tests extend `JsonBasedTest` and use response fixtures from `src/commonTest/resources/responses`.
- Use stubs in `commonTest/.../stubs` for mutable settings-like dependencies.

Common commands:

- Main project check: `.\gradlew.bat assembleDebug`
- If tests changed: `.\gradlew.bat testDebugUnitTest`
- Final style check: `.\gradlew.bat detekt`

## Project Skills

Project-specific skills live in `.ai`:

- `.ai/tackle-mvi`
- `.ai/tackle-decompose`
- `.ai/tackle-mvi-tests`
- `.ai/tackle-component-tests`
- `.ai/tackle-unit-tests`

Use them when adding or changing stores, components, or tests. They intentionally point back to this file for full architecture context.

