| Field                  | Type   | Constraint                    | Business Rule                                      |
|------------------------|--------|-------------------------------|----------------------------------------------------|
| id                     | Long   | Auto-increment, PK            | Primary identifier                                  |
| supplier_code          | String | Unique, Required              | Kode vendor wajib unik (contoh: SUP-ABC)           |
| tax_number             | String | Optional, Pattern: NPWP      | NPWP untuk invoice dan legal compliance           |

✅ Validation:
- Supplier code wajib diisi
- Address billing harus berbeda dengan shipping address jika ada
