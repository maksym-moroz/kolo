# Code Style

This document is the durable source of truth for repository code-style rules that go beyond formatter defaults.

Keep this file small and explicit:

- add only rules that have been intentionally agreed
- prefer concrete yes/no rules over broad taste statements
- keep formatter and linter configuration aligned with this document

## Agreed Rules

1. Use explicit `.invoke(...)` for lambda and function-type invocation.

Use:

```kotlin
onClick.invoke()
transform.invoke(value)
```

Do not use:

```kotlin
onClick()
transform(value)
```

2. Never use wildcard imports.

3. Keep one top-level type per Kotlin file.

4. Prefer expression bodies for single-expression functions.

5. Prefer block bodies when a function has side effects or branching.

6. Do not use implicit `it` in lambdas longer than one expression.

7. Do not use implicit `it` when lambdas are nested.

8. Use named arguments whenever passing a boolean literal.

9. Use named arguments when a call has 2 or more same-kind primitive arguments.

10. Use name-based destructuring whenever Kotlin 2.3.20 language support makes it available.

11. Prefer guard clauses and early returns over nested `if` blocks.

12. Use `if` for simple binary branching. Use `when` for subject matching or 3 or more branches.

13. Keep trailing commas disabled in declarations and call sites.

14. Put constructor and data-class parameters on separate lines starting from 1 parameter.

15. Keep each chained collection or `Flow` operator on its own line once the chain is multiline.

16. Prefer lambdas over method references unless the method reference is clearly shorter and clearer.

17. Do not nest scope functions more than one level deep.

18. Use `let` only for null-scoping or value transformation, not as a generic block wrapper.

19. Use `also` only for side effects. Do not return transformed values from `also`.

20. Prefer `sealed interface` over `sealed class` when no shared state or implementation is needed.

21. Prefer null-guard exits like `?: return` or `?: continue` over wrapping the rest of the block in a null check.
