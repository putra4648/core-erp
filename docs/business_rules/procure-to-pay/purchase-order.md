| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| po_number               | String | Unique, Pattern: [A-Z0-9]+    | Nomor PO wajib unik per company                       |
| supplier_id             | Long   | Required, FK                  | Wajib terkait ke supplier                            |
| budget_id               | Long   | Nullable, FK                  | Budget line untuk tracking pengeluaran               |
| status                  | Enum   | Required                      | Status wajib sesuai lifecycle (SENT → DELIVERED → CLOSED)│

✅ Validation:
- PO dapat dibuat setelah approval dari requisition
- Supplier reference harus diisi jika ada PO supplier lain
