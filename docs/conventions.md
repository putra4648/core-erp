# Conventions

## Entities

Every entity extends `BaseEntity`:

```java
@Entity
@Table
public class Customer extends BaseEntity {
    @NotBlank(message = "Customer name is required")
    public String name;
}
```

`BaseEntity` supplies `id` (from `PanacheEntity`), `createAt` / `createBy` / `updateAt` /
`updateBy` audit columns, and the soft delete fields.

Fields are **public**. Quarkus Panache works with public fields, and Vaadin binders read them
directly — there is no getter/setter layer in this codebase. Do not add one for consistency's
sake.

## Money fields

Money is `BigDecimal`, never `double`:

```java
@NotNull(message = "Price is required")
@Column(precision = 19, scale = 4, columnDefinition = "numeric(19,4) default 0")
public BigDecimal price;
```

The `columnDefinition` is not decoration. `generation=update` emits `add column ... not null`
with no default when it adds a `@NotNull` field to an existing table, and PostgreSQL rejects that
on any table with rows. See `BaseEntity.deleted` for the same trick. `Product.price` is the
reference.

`NumberField` is `Double`-valued, so binding it to a `BigDecimal` entity field needs
`withConverter` in both directions — see `ProductView.configureValidation`.

## Soft delete

See [database.md](database.md#soft-delete).

## Excel export

Export is split into three layers so a report can change without touching styling, and styling
without touching reports:

```
service/excel/            UI-agnostic rendering primitives
├── ExcelColumn           column model: header, width, value type, alignment
├── ExcelReport           title + subtitle + columns + rows
├── ExcelStyles           every CellStyle, built once per Workbook
└── ExcelSheetBuilder     renders an ExcelReport onto a sheet

modules/report/excel/     report content
├── ReportType            enum: UI label, sheet name, which sheet class renders it
├── InventoryReportSheet
├── InboundReportSheet
├── OutboundReportSheet
└── AuditReportSheet
```

`ReportsView` owns selection and download only. Adding a report is one new sheet class plus one
`ReportType` constant — no view change.

The produced workbook follows a fixed formal layout:

| Row  | Content                                                               |
| ---- | --------------------------------------------------------------------- |
| 0    | Company name — merged, centred, 14pt navy                             |
| 1    | Report title — merged, centred, 16pt bold, uppercased                 |
| 2    | Period / generated / record count — merged, centred, 10pt italic grey |
| 3    | Spacer                                                                |
| 4    | Header row — white on dark blue, bordered, frozen, autofiltered       |
| 5..n | Data — zebra striped, bordered, typed values                          |
| n+1  | TOTAL — bold, grey fill, medium top border                            |
| n+2  | Footer — merged, 8pt italic grey                                      |

Decisions worth knowing before editing:

- **Column widths are explicit**, never `autoSizeColumn`. Auto-sizing depends on the fonts
  installed in the JVM, so the same report comes out different widths on a developer machine and
  a headless server.
- **Dates are written as real Excel dates**, not formatted strings, so the recipient can sort,
  filter and group by them.
- **Null values become blank cells**, never `0` or `"-"`. A fabricated zero flows into the
  recipient's SUM and pivot and reads as real data.
- **The total row skips the leading `INTEGER` column.** "No." is a row counter, not a measure.
- **An empty report emits a "no data" notice and no TOTAL row**, for the same reason.
- `XSSFWorkbook` holds the whole document in memory. Past roughly 50,000 rows, switch to
  `SXSSFWorkbook` — at the cost of the merged title block.

`InventoryReportSheet` is named *Stock Status*, not *Stock Valuation*, because it reports
quantities and thresholds only. `Product` now carries a `price`, so a valuation variant summing
`quantity * price` is buildable — that would be a separate report type, not a column added here.

Style guarantees are covered by `ExcelSheetBuilderTest`, which is plain JUnit: it renders a
workbook and asserts cell types, merges, widths and totals without booting Quarkus or needing
PostgreSQL. See [testing.md](testing.md).
