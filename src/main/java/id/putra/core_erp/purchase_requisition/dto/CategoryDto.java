package id.putra.core_erp.purchase_requisition.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategoryDto {
    private String id;
    private String name;
    private String parent_id;
    private List<CategoryDto> children = new ArrayList<>();

    public void addChildren(CategoryDto child) {
        this.children.add(child);
    }
}
