package edu.eci.arsw.myrestaurant.beans.impl;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import java.util.Map;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;



@Component
@Qualifier("Basic")
public class BasicBillCalculator implements BillCalculator {

	@Override
	public int calculateBill(Order o,Map<String,RestaurantProduct> productsMap) {
		int total=0;
		for (String p:o.getOrderedDishes()){
                    RestaurantProduct rp=productsMap.get(p);
                    total+=o.getDishOrderedAmount(p)*rp.getPrice();                    
		}
		return total;
	}

}
