# Business Rules: ParameterLookup

## Entity Definition
Dynamic system configuration key-value dictionary partitioned by module.

| Field         | Type    | Constraint         | Business Rule                                            |
|:--------------|:--------|:-------------------|:---------------------------------------------------------|
| `id`          | Long    | PK, Auto-increment | Primary identifier inherited from `BaseEntity`.          |
| `module`      | String  | Nullable           | Business module namespace (e.g., `SYSTEM`, `INVENTORY`). |
| `key`         | String  | Required, Unique   | Unique configuration key identifier.                     |
| `value`       | String  | Required           | Value payload or configuration setting.                  |
| `description` | String  | Nullable           | Functional explanation of this parameter key.            |
| `isActive`    | Boolean | Default true       | Controls if this configuration entry is active.          |

## ✅ Validation & Operational Rules
- `key` and `value` must not be blank.
- Search lookup matches `key`, `value`, or `description` case-insensitively.
