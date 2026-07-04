# Contributing to EnhancedMOTD

Thanks for your interest in improving EnhancedMOTD! This document explains how to
build the mod, the commit conventions used for automated releases, and how to
submit changes.

## Prerequisites

- **JDK 25** (Minecraft 26.1 runs on Java 25)
- **Git**
- The Gradle wrapper is included — you do **not** need a system Gradle install.

## Building

```bash
./gradlew build
```

The built jar lands in `build/libs/`. To run a test server or client:

```bash
./gradlew runServer   # dedicated server in run/server
./gradlew runClient   # client in run
```

## Making changes

1. Fork the repository and create a branch off `main`.
2. Make your change. Keep the code style consistent with the surrounding code.
3. Build locally (`./gradlew build`) and, where possible, test in-game by
   checking the server-list MOTD and icon.
4. Open a pull request against `main` and fill out the PR template.

## Commit message convention

This project uses [Conventional Commits](https://www.conventionalcommits.org/)
to drive automated versioning and release notes via semantic-release. Format
your commits as:

```
<type>(<scope>): <subject>
```

**Common types:**

- `feat:` — a new feature (minor version bump)
- `fix:` — a bug fix (patch version bump)
- `perf:` — a performance improvement (patch)
- `refactor:` — code change that neither fixes a bug nor adds a feature (patch)
- `docs:` — documentation only
- `build:` / `ci:` / `chore:` / `style:` / `test:` — no release

Add `BREAKING CHANGE:` in the commit footer to trigger a major version bump.

**Examples:**

```
feat(motd): add support for multi-line gradients
fix(config): prevent crash when the icons array is empty
docs(readme): update the formatting guide
```

See [`RELEASING.md`](RELEASING.md) for how releases are cut from these commits.

## Reporting bugs & requesting features

Use the issue templates on the
[Issues](https://github.com/lusouzadev/EnhancedMOTD/issues) tab. For security
issues, follow [`SECURITY.md`](SECURITY.md) instead of opening a public issue.

## License

By contributing, you agree that your contributions will be licensed under the
[LGPL-3.0](LICENSE) license that covers the project.
