| Field                   | Type   | Constraint                    | Business Rule                                        |
|-------------------------|--------|-------------------------------|-------------------------------------------------------|
| id                      | Long   | Auto-increment, PK            | Primary identifier                                    |
| location_id             | Long   | Required, FK                  | Lokasi tujuan wajib valid dan aktif                    |

✅ Validation:
- Location harus dalam hierarchy Warehouse → Zone → Rack
- Putaway dari receiving yang sama tidak boleh ke lokasi berbeda untuk item yang sama
