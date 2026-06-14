---
name: tackle-unit-tests
description: Write or update non-component unit tests in the Tackle Kotlin Multiplatform project. Use when testing domain managers, mappers, utilities, database behavior, settings wrappers, network response mapping, or other commonTest code that is not a MVIKotlin store or Decompose component.
---

# Tackle Unit Tests

Read `AGENTS.md` first for module roles, data flow, and validation commands.

## Choose The Test Style

- Domain manager tests: plain common tests with Mokkery gateway/tool mocks.
- Network mapper tests: extend `JsonBasedTest`, parse JSON fixtures from `src/commonTest/resources/responses`, and assert mapped domain values.
- Utility tests: plain common tests under the utility module.
- Database-like behavior: prefer `TackleTestDatabase` or direct mapper tests unless the change specifically needs SQLDelight integration.
- Settings behavior: test `TackleSettingsInternal` through a test `Settings` implementation when needed.

## Domain Manager Tests

1. Instantiate the manager directly.
2. Mock only its gateway interfaces.
3. Assert returned `Result` success/failure and key payload fields.
4. Verify gateway calls with exact parameters.
5. Cover branching logic, especially current-locale decisions and toggle methods that choose between paired API calls.

## Mapper Tests

1. Add a realistic JSON fixture under the module's `src/commonTest/resources/responses`.
2. Parse with `responseFromFile<Dto>("src/commonTest/resources/responses/file.json")`.
3. Map DTO to domain model.
4. Assert required fields, optional defaults, nested lists, enums, dates, and fallback behavior.
5. Keep response DTOs internal; tests in the same module can access them.

## Utility Tests

- Keep tests small and deterministic.
- Cover edge cases: empty strings, invalid dates, unknown MIME extensions, null numeric helpers, URL normalization, and regex matches.
- For time-sensitive functions, pass explicit `now` or timezone parameters when the utility allows it.

## Verification

Run `.\gradlew.bat testDebugUnitTest` after changing tests. If production code changed, also run `.\gradlew.bat assembleDebug` and final `.\gradlew.bat detekt`.

