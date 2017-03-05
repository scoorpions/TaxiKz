package kz.taxikz.controllers.order;

import java.util.List;
import kz.taxikz.data.api.pojo.Order;

public class OrderEvents {

    public static class CreateOrderFailed {
    }

    public static class CreateOrderSuccess {
    }

    public static class GetOrdersFailed {
    }

    public static class GetOrdersSuccess {
        private List<Order> orders;

        public List<Order> getOrdersList() {
            return this.orders;
        }

        public GetOrdersSuccess(List<Order> orders) {
            this.orders = orders;
        }
    }

    public static class OrderCancelFailed {
    }

    public static class OrderCancelSuccess {
    }
}
