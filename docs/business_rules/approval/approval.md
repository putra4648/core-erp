| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| document_id             | Long   | Required, FK                  | Harus merujuk ke dokumen yang perlu di-approve       |
| current_approval_stage  | Integer| Min 0                         | Stage saat ini minimal 0                              |
| total_approval_stage    | Integer| Min 1                         | Total stages maksimal sesuai policy                   |
| status                  | Enum   | Required                      | Status wajib sesuai lifecycle                         |

✅ Validation:
- Current approval stage tidak boleh melebihi total approval stage
- Rejection note wajib diisi jika status = REJECTED
- Last approved timestamp hanya terisi setelah approval permanen