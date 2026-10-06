| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| purchase_order_id       | Long   | Nullable, FK                  | Harus terkait ke PO jika pembelian eksternal          |
| status                  | Enum   | Required                      | Status wajib DRAFT → IN_PROGRESS → CLOSED             |

✅ Validation:
- Receiving tanpa PO hanya untuk inbound tanpa pembelian (contoh: barang retur)
- Supplier contact person harus valid user dari sistem
