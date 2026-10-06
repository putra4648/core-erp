| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| purchase_order_id       | Long   | Nullable, FK                  | PO reference untuk tracking                          │
| reason                  | Enum   | Required                      | Reason wajib dari: DEFECTIVE/DAMAGED/WARRANTY         │

✅ Validation:
- RTV hanya dapat dari QC hasil FAILED atau CONDITIONAL
- Supplier harus valid dan active dalam sistem
