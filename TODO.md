# TODO

## Immediate fixes

- [ ] Restore DTO mapping in `PackingQueryAdapterImpl` instead of returning empty DTOs.
- [ ] Fix repository injection and DTO mapping in `ShipmentQueryAdapterImpl`.
- [ ] Correct `CarrierDto` mapping errors in `CarrierQueryAdapterImpl`.
- [ ] Fix equivalent mapping issues in `PickingTaskQueryAdapterImpl`.

## Quarkus migration build fixes

- [ ] Resolve missing `PageRequest` and `Pageable` references.
- [ ] Restore or replace missing DTO `builder()` and `toBuilder()` methods.
- [ ] Fix malformed service and command adapter method calls reported by Maven.
- [ ] Run the full Maven test/build successfully.

## Regression and deployment review

- [ ] Add query adapter tests for `getById`, `getAll`, entity-to-DTO mapping, and CDI injection.
- [ ] Verify that removing the security classes did not disable required authentication or authorization.
- [ ] Decide whether the deleted Dockerfiles should be restored or replaced with updated deployment configuration and documentation.
- [ ] Review the current outbound adapter changes and commit them after validation.

## Validation note

The current build fails with 53 compilation errors. The Maven wrapper is not executable, so use `bash mvnw` or restore its execute permission when running the build.