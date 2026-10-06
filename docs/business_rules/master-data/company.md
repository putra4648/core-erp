# Business Rules: Company

## Entity Definition
Manages legal entity profiles, tax identifiers, banking configuration, and operational policy defaults.

| Field                 | Type         | Constraint                   | Business Rule                                                                |
|:----------------------|:-------------|:-----------------------------|:-----------------------------------------------------------------------------|
| `id`                  | Long         | PK, Auto-increment           | Primary identifier inherited from `BaseEntity`.                              |
| `name`                | String       | Required, Unique             | Legal registered company name (e.g. `PT Core ERP Indonesia`).                |
| `telp`                | String       | Nullable                     | Primary telephone/switchboard number.                                        |
| `npwp`                | String       | Nullable                     | National Taxpayer Identification Number (NPWP/Tax ID).                       |
| `logoPath`            | String       | Nullable                     | Relative or absolute storage path to company logo asset.                     |
| `accountBank`         | String       | Required                     | Primary corporate bank account details (Bank Name & Account Number).         |
| `financialEndYear`    | Date         | Nullable                     | Fiscal year closing date (`end_year`).                                       |
| `curr`                | List<String> | Nullable (jsonb/collection)  | Supported operating currencies (e.g., `["IDR", "USD", "EUR"]`).              |
| `needRegisterApproval`| Boolean      | Required, Default false      | Controls if vendor/customer registration requires executive review.          |
| `needAutoCloseOrder`  | Boolean      | Required, Default false      | Automatically closes purchase & sales orders once receipt/dispatch is 100%.  |

## ✅ Validation & Operational Rules
- `name` and `accountBank` must not be blank.
- `curr` must contain valid ISO-4217 currency codes.
- Only active companies can issue Purchase Orders and Sales Invoices.
