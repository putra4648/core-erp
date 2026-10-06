| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| requisition_number      | String | Unique, Pattern: [A-Z0-9]+    | Nomor PR wajib unik per company                       |
| department_id           | Long   | Required, FK                  | Wajib terkait ke departemen                          |
| budget_id               | Long   | Nullable, FK                  | Budget line untuk kontrol anggaran                   |
| total_price             | Decimal| Min 0.01                      | Total harga harus sesuai sum baris item              |

✅ Validation:
- Status DRAFT tidak bisa di-commit sebelum semua lines diisi
- Over-budget requisition wajib melalui approval override
- Reference PO hanya terisi setelah PO created
