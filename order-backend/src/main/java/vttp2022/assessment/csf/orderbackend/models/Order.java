package vttp2022.assessment.csf.orderbackend.models;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import jakarta.json.Json;
import jakarta.json.JsonObject;

// IMPORTANT: You can add to this class, but you cannot delete its original content

public class Order {

	private Integer orderId;
	private String name;
	private String email;
	private Integer size;
	private String sauce;
	private Boolean thickCrust;
	private List<String> toppings = new LinkedList<>();
	private String comments;

	public void setOrderId(Integer orderId) { this.orderId = orderId; }
	public Integer getOrderId() { return this.orderId; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }

	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	public void setSize(Integer size) { this.size = size; }
	public Integer getSize() { return this.size; }

	public void setSauce(String sauce) { this.sauce = sauce; }
	public String getSauce() { return this.sauce; }

	public void setThickCrust(Boolean thickCrust) { this.thickCrust = thickCrust; }
	public Boolean isThickCrust() { return this.thickCrust; }

	public void setToppings(List<String> toppings) { this.toppings = toppings; }
	public List<String> getToppings() { return this.toppings; }
	public void addTopping(String topping) { this.toppings.add(topping); }

	public void setComments(String comments) { this.comments = comments; }
	public String getComments() { return this.comments; }

	public static Order create(String payload) {
		Order order = new Order();
		List<String> list = new LinkedList<>();

		JsonObject obj = Json.createReader(new StringReader(payload)).readObject();
		order.setName(obj.getString("name"));
		order.setEmail(obj.getString("email"));
		order.setSauce(obj.getString("sauce"));
		order.setSize(obj.getInt("size"));
		
		if(obj.getString("base").equals("thin")){
			order.setThickCrust(false);
		} else {
			order.setThickCrust(true);
		}

		for (int i = 0; i < obj.getJsonArray("toppings").size(); i++) {
			list.add(obj.getJsonArray("toppings").getString(i));
		}
		order.setToppings(list);
		order.setComments(obj.getString("comments"));
		return order;
	}

	public static Order createRs(SqlRowSet rs) {
		Order order = new Order();

		order.setOrderId(rs.getInt("order_id"));
		order.setName(rs.getString("name"));
		order.setEmail(rs.getString("email"));
		order.setSize(rs.getInt("pizza_size"));
		order.setSauce(rs.getString("sauce"));
		order.setThickCrust(rs.getBoolean("thick_crust"));
		List<String> toppings = new LinkedList<>();
		String[] topping = rs.getString("toppings").split(",");
		for (int i = 0; i < topping.length; i++) {
			toppings.add(topping[i]);
		}
		order.setToppings(toppings);


		return order;
	}

}
