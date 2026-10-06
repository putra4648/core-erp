| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| receiving_id            | Long   | Required, FK                  | Wajib terkait ke receiving                            |
| result                  | Enum   | Required                      | Result wajib satu dari: PASSED, FAILED, CONDITIONAL  │

✅ Validation:
- Inspection harus dilakukan sebelum labelling atau putaway
- Result CONDITIONAL memerlukan review manual dari approver
