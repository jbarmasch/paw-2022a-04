package ar.edu.itba.paw.model;

public enum Order {
    price_asc("minPrice", "ASC"),
    price_desc("minPrice", "DESC"),
    date_asc("date", "ASC"),
    date_desc("date", "DESC");

    private String order;
    private String orderBy;

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
