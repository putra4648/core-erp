| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| department_number       | String | Unique, Pattern: [A-Z0-9]+    | Kode departemen unik (contoh: DEPT-SALES)             |
| parent_department_id    | Long   | Nullable, FK to id            | Parent dapat null (top-level dept), FK lain tidak    |
| cost_center             | String | Optional, Pattern: [A-Z0-9]+  | Cost center untuk budget tracking                    |

✅ Validation:
- Parent department tidak boleh merujuk ke child
- Hierarchy depth maksimal 5 level
- Manajer wajib diisi untuk departemen aktif
