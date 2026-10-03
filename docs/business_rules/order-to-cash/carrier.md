| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| carrier_name            | String | Required, Unique              | Nama carrier wajib unik (contoh: JNE, FedEx)          │
| api_key                 | String | Nullable                       | API key untuk integration                            │

✅ Validation:
- Service fee harus diisi untuk carrier berbayar
- API key harus di-enkripsi untuk keamanan data
