# Setup

## Prerequisites

- JDK 25 (GraalVM 25 if you want the native build)
- Docker with the Compose plugin. Quarkus auto-detects `compose-devservices.yml` and starts
  PostgreSQL for you in dev and test. **The Docker daemon must be running first** — there is no
  fallback. If it is not, Quarkus warns `ComposeDevServicesProcessor` and then tries
  `localhost:5432`, which is not listening, and every `@QuarkusTest` fails with
  `Connection refused`. Start Docker Desktop before anything else.
- An Auth0 tenant, or any OIDC provider you configure yourself

`mvnw` needs `JAVA_HOME` pointing at a JDK 25 install. On Windows, `./mvnw` from a WSL/Git shell
may not see it — run `mvnw.cmd` from `cmd.exe` and set `JAVA_HOME` there.

## Environment

Create `.env` in the project root (gitignored):

```properties
AUTH0_ISSUER=https://<your-tenant>.eu.auth0.com/
AUTH0_CLIENT_ID=<client-id>
AUTH0_CLIENT_SECRET=<client-secret>
```

The issuer URL must include the scheme and end with `/`. The Auth0 application must be type
**Regular Web Application**, with allowed callback and logout URLs pointing at
`http://localhost:8080`.

## Dev mode

```bash
./mvnw quarkus:dev
```

Quarkus starts a PostgreSQL 18 container from `compose-devservices.yml` on port 5432 and creates
the schema on first boot. The UI is at `http://localhost:8080`.

Dev mode is the intended inner loop: Vaadin hot-reloads view classes without a restart.

## Tests

```bash
./mvnw test
```

Tests that boot Quarkus need the database, so they need Docker running. Plain-JUnit tests do not.

## Production build

```bash
./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar
```

## Native build

```bash
./mvnw clean package -Dnative
./target/*-runner
```

CI runs both `clean verify` and the native build on every push to `main`
(`.github/workflows/simple-build-test.yml`).
