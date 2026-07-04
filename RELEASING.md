# Releasing

Releases are **automated and git-driven** via [semantic-release](https://semantic-release.gitbook.io/).
You never bump the version by hand — it's derived from your commit messages.

## How it works

1. Commit using **Conventional Commits** and push to `main`.
2. The **Release** workflow (`.github/workflows/release.yml`) runs semantic-release, which:
   - decides the next version from your commits,
   - updates `CHANGELOG.md`,
   - creates the git tag `vX.Y.Z+neoforge-26.1.2` and a **GitHub Release**,
   - builds the mod jar at that version and attaches it (plus the sources jar) to the release,
   - publishes to **Modrinth** and **CurseForge** — only if those are configured (see below).
3. The **Build** workflow (`.github/workflows/build.yml`) compiles on every push/PR as a sanity check.

The build reads its version from the git tag (`getVersionFromGit()` in `build.gradle`);
`mod_version` in `gradle.properties` is only a fallback for untagged local builds.

## Commit types → version bump

| Commit | Result |
|---|---|
| `feat: …` | minor (x.**Y**.0) |
| `fix: …` / `perf: …` / `refactor: …` | patch (x.y.**Z**) |
| `feat!: …` or a `BREAKING CHANGE:` footer | major (**X**.0.0) |
| `docs:` `chore:` `ci:` `test:` `style:` `build:` | no release |

## Enabling Modrinth / CurseForge

By default only the GitHub Release is produced. To turn on the other platforms:

1. Create the project on Modrinth / CurseForge and note its **project ID**.
2. Put the ID(s) in `gradle.properties`:
   ```
   modrinth_id=AbCdEfGh
   curseforge_id=123456
   ```
3. Add the API token(s) as **GitHub Actions repository secrets**:
   - `MODRINTH_TOKEN`
   - `CURSEFORGE_TOKEN`

   (`GITHUB_TOKEN` is provided automatically by Actions.)

If a project ID is left blank, that platform is skipped and the rest of the release still runs.

## Local build

```
./gradlew build
```
Output: `build/libs/<archives_base_name>-<version>.jar`.
