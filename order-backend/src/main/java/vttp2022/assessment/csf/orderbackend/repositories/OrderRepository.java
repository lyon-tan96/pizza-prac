package vttp2022.assessment.csf.orderbackend.repositories;

import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2022.assessment.csf.orderbackend.models.Order;
import vttp2022.assessment.csf.orderbackend.models.OrderSummary;
import vttp2022.assessment.csf.orderbackend.services.PricingService;

@Repository
public class OrderRepository {
    
    private static String SQL_INSERT_ORDER = "insert into orders(name, email, pizza_size, thick_crust, sauce, toppings, comments) values(?,?,?,?,?,?,?)";
    private static String SQL_GET_ORDER = "select * from orders where email = ?";

    @Autowired
    private JdbcTemplate template;

    @Autowired
    private PricingService pricingSvc;

    public Boolean createOrder(Order order) {

        String toppings = order.getToppings().toString().replace("[", "").replace("]", "").replaceAll("\\s", "");
        int updated = template.update(SQL_INSERT_ORDER, order.getName(), order.getEmail(), order.getSize(), order.isThickCrust(), order.getSauce(), toppings, order.getComments());

        return 1 == updated;
    }

    public List<OrderSummary> getUserOrders(String email) {

        List<OrderSummary> orderItems = new LinkedList<>();

        SqlRowSet rs = template.queryForRowSet(SQL_GET_ORDER, email);

        while(rs.next()) {
            OrderSummary orderSummary = OrderSummary.convert(rs);
            Float total = getTotal(rs);
            orderSummary.setAmount(total);
            orderItems.add(orderSummary);
        }

        return orderItems;
    }

    public Float getTotal(SqlRowSet rs) {
        Float total = 0f;
        total += pricingSvc.size(rs.getInt("pizza_size"));
        if(rs.getBoolean("thick_crust")) {
            total += pricingSvc.thickCrust();
        }
        else if (!rs.getBoolean("thick_crust")) {
            total += pricingSvc.thinCrust();
        }
        total += pricingSvc.sauce(rs.getString("sauce"));
        
        String[] toppings = rs.getString("toppings").split(",");
        for (String topping: toppings) {
            total += pricingSvc.topping(topping);
        }

        

        return total;
    }
}
