package Generator;

public class Order {

    private String orderName;
    private OrderType orderType;

    public Order(String orderName, String orderType) {
        this.orderName = orderName;
        if(orderType==null || orderType.equals("ASC")){
            this.orderType = OrderType.ASC;
        }else if(orderType.equals("DESC")){
            this.orderType = OrderType.DESC;
        }
    }

    public String getOrderName() {
        return orderName;
    }

    public OrderType getOrderType() {
        return orderType;
    }
}
