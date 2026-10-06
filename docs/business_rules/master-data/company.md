| Field           | Type   | Constraint                     | Business Rule                                           |
|-----------------|--------|--------------------------------|---------------------------------------------------------|
| id              | Long   | Auto-increment, PK             | Primary identifier                                      |
| name            | String | Required, Min 2 chars          | Nama perusahaan wajib diisi (contoh: PT ABC)            |
| code            | String | Unique, Pattern: [A-Z0-9]+     | Kode unik, huruf kapital + angka (contoh: COMP001)      |
| tax_number      | String | Nullable, Pattern: NPWP valid  | NPWP dalam format sesuai regulasi (22 digit)            |
| fiscal_year_start | Date   | Required, ISO date             | Tahun fiskal mulai wajib diisi (YYYY-MM-DD)            |

✅ Validation:
- Nama tidak boleh mengandung karakter spesial (!@#$%^&*)
- Kode harus unik per company
- Fiscal year start tidak boleh lebih dari tahun saat ini
