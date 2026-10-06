| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| budget_number           | String | Unique, Pattern: [A-Z0-9]+    | Nomor anggaran wajib unik per company/year            |
| department_id           | Long   | Required, FK                  | Budget harus terkait ke departemen                   |
| fiscal_period           | Integer| Min 1, Max 12                 | Periode fiskal antara 1-12                           |
| total_amount            | Decimal| Min 0.01                      | Total anggaran minimal 0.01                          |

✅ Validation:
- Budget number wajib diisi
- Fiscal period tidak boleh melebihi periode tahun saat ini
- Department dengan budget harus memiliki approved budget dari parent