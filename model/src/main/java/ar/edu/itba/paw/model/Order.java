package ar.edu.itba.paw.model;

public enum Order {
    PRICE_ASC("price_asc", "minPrice", "ASC"),
    PRICE_DESC("price_desc", "minPrice", "DESC"),
    DATE_ASC("date_asc", "date", "ASC"),
    DATE_DESC("date_desc", "date", "DESC");

    private final String name;
    private final String order;
    private final String orderBy;

    Order(String name, String order, String orderBy) {
        this.name = name;
        this.order = order;
        this.orderBy = orderBy;
    }

    public String getName() {
        return name;
    }

    public String getOrder() {
        return order;
    }

    public String getOrderBy() {
        return orderBy;
    }
}
