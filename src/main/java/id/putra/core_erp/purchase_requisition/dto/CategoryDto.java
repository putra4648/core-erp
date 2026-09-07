package id.putra.core_erp.purchase_requisition.dto;

import java.util.ArrayList;
import java.util.List;

public class CategoryDto {
    private String id;
    private String name;
    private String parent_id;
    private List<CategoryDto> children = new ArrayList<>();

    public void addChildren(CategoryDto child) {
        this.children.add(child);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public List<CategoryDto> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryDto> children) {
        this.children = children;
    }
}
