 Field                    Type    Constraint                     Business Rule                                        
-----------------------------------------------------------------------------------------------------------------------
 id                       Long    Auto-increment, PK             Primary identifier                                    
 packing_id               Long    Required, FK                   Packing harus valid dan completed                       │
 carrier                  User    Nullable                       Carrier dapat null untuk pengiriman internal          │

✅ Validation
- Tracking number wajib diisi jika menggunakan carrier eksternal
- Status DELIVERED hanya dapat setelah customer confirmation
