# Vaadin & Spring Boot GraalVM Native Image Configuration

## Goal
Configure Vaadin Flow and Spring Boot build plugins, profiles, packaging, and AOT runtime hints for GraalVM Native Image generation.

## Tasks
- [x] Task 1: Update [pom.xml](file:///e:/Project/core-erp/pom.xml) packaging to `jar` and remove `provided` scope from Tomcat → Verify: `./mvnw clean compile` runs successfully.
- [x] Task 2: Configure `production` and `native` profiles in [pom.xml](file:///e:/Project/core-erp/pom.xml) with `vaadin-maven-plugin` and `native-maven-plugin` → Verify: `./mvnw help:all-profiles` shows `production` and `native`.
- [x] Task 3: Create [NativeRuntimeHints.java](file:///e:/Project/core-erp/src/main/java/id/putra/core_erp/config/NativeRuntimeHints.java) to register reflection/resource hints for Apache POI, ZXing, and Tika → Verify: `./mvnw -P production spring-boot:process-aot -DskipTests` executes without AOT errors.
- [x] Task 4: Test Vaadin frontend bundle generation and AOT processing → Verify: Frontend assets exist in `target/classes/META-INF/VAADIN/webapp/`.

## Done When
- [x] `./mvnw -P production spring-boot:process-aot -DskipTests` passes cleanly.
- [x] Both local GraalVM (`native:compile`) and containerized buildpack (`spring-boot:build-image`) pathways are configured.

## Notes
- Building native image locally requires `JAVA_HOME` pointing to GraalVM JDK with `native-image` installed.
- Building native container image via Docker uses `./mvnw spring-boot:build-image -P production` without requiring local GraalVM.
- In PowerShell, quote combined profiles: `-P "production,native"` instead of `-Pproduction,native`.
