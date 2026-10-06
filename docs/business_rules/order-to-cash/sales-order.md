| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| so_number               | String | Unique, Pattern: [A-Z0-9]+    | Nomor SO wajib unik per company                       │
| customer_id             | Long   | Required, FK                  | Customer wajib valid dan active                        │
| total_amount            | Decimal| Min 0.01                      | Total harga sesuai sum baris item                     │

✅ Validation:
- Sales rep harus user dari sales department
- Delivery date tidak boleh sebelum tanggal saat ini
- Status hanya dapat maju dalam lifecycle (tidak boleh mundur)
