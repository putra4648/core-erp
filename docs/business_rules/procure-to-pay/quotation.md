| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| quotation_number        | String | Unique, Pattern: [A-Z0-9]+    | Nomor RFQ wajib unik per company                     |
| supplier_id             | Long   | Required, FK                  | Wajib terkait ke supplier                            |
| status                  | Enum   | Required                      | Status wajib sesuai lifecycle                         |

✅ Validation:
- Quotation dari supplier yang sama untuk PR yang sama harus ada minimal satu
- Winning quotation hanya dapat dipilih sekali per requisition
