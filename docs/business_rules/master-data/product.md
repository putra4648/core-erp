| Field           | Type   | Constraint                       | Business Rule                                              |
|-----------------|--------|----------------------------------|------------------------------------------------------------|
| id              | Long   | Auto-increment, PK               | Primary identifier                                          |
| sku             | String | Unique, Required                 | SKU wajib unik per company (contoh: PROD-001)              |
| barcode         | String | Nullable                         | Barcode untuk scanner (opsional)                          |
| unit_cost       | Decimal| Min 0.01, Precision 2           | Harga pokok modal minimal 0.01                             |
| active          | Boolean| Default true                    | Produk non-aktif tidak bisa diproses transaksi             |

✅ Validation:
- SKU wajib diisi untuk produk aktif
- Unit of measure harus sesuai standar (PCS, KGS, METERS)
- Tax code harus valid untuk produk kena pajak
