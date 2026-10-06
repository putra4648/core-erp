| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| product_id              | Long   | Required, FK                  | Produk wajib valid dan active                          │
| location_id             | Long   | Required, FK                  | Lokasi wajib valid dalam warehouse hierarchy          │
| quantity_on_hand        | Decimal| Min 0.00                      | Fisik tidak boleh negatif                              │
| quantity_allocated      | Decimal| Min 0.00                      | Reserved untuk SO tidak boleh lebih dari on hand       │

✅ Validation:
- Quantity available = quantity_on_hand - quantity_allocated
- Expiry date harus valid (tidak dalam masa lalu)
- Status EXPIRED jika expiry_date < today
