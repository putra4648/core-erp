| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| customer_code           | String | Unique, Required              | Kode pelanggan wajib unik (contoh: CUST-001)         |
| credit_limit            | Decimal| Min 0.00, Precision 2        | Limit kredit untuk transaksi penjualan               |

✅ Validation:
- Customer code wajib diisi
- Credit limit dapat null untuk customer retail
- Tax number harus valid untuk B2B customers
