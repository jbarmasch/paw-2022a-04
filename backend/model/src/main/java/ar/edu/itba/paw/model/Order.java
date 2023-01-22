package ar.edu.itba.paw.model;

public enum Order {
    DATE_ASC("date", "ASC"),
    DATE_DESC("date", "DESC");

    private final String order;
    private final String orderBy;

    Order(String order, String orderBy) {
        this.order = order;
        this.orderBy = orderBy;
    }

    public String getOrder() {
        return order;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
