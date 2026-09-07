package id.putra.core_erp.purchase_requisition.dto;

public class AppDto<T> {
    private T params;
    private Integer page;
    private Integer size;

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
