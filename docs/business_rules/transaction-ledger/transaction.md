| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| txn_number              | String | Unique, Pattern: [A-Z0-9]+    | Nomor transaksi wajib unik                           │
| txn_type                | Enum   | Required                      | Harus satu dari: INBOUND, OUTBOUND, TRANSFER, ADJUSTMENT│

✅ Validation:
- Transaction tidak boleh di-reverse jika sudah DELIVERED/SHIPPED
- Reference number wajib diisi untuk audit trail
- Source location dan dest location harus valid warehouse locations
