| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| transaction_id          | Long   | Required, FK                  | Wajib terkait ke transaction parent                    │
| product_id              | Long   | Required, FK                  | Produk wajib valid                                      │

✅ Validation:
- Total price harus sesuai quantity × unit_price
- Lot batch dapat null jika tidak ada lot tracking
