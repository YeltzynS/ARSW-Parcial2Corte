/*
 * Copyright (C) 2016 Pivotal Software, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package edu.eci.arsw.myrestaurant.restcontrollers;

import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.ProductType;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.services.OrderServicesException;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServices;
import edu.eci.arsw.myrestaurant.services.RestaurantOrderServicesStub;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/**
 *
 * @author hcadavid
 */


    
    @RestController
public class OrdersAPIController {

    @Autowired
    @Qualifier("Stub")
    RestaurantOrderServices orderServices;
    
    @RequestMapping(value = "/orders", method = RequestMethod.GET)
    public ResponseEntity<?> handlerGetBlueprints() {
        try {
            String orderJSON = getOrders();
            return new ResponseEntity<>(orderJSON, HttpStatus.ACCEPTED);
        } catch (Exception ex) {
            return new ResponseEntity<>("Error " + ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    private String getOrders() throws OrderServicesException{
        Set<Integer> orders = orderServices.getTablesWithOrders();
        Object[] orderP = orders.toArray();
        Gson gson = new Gson();
        String response = "[";
        for(int i = 0; i < orderP.length; i++){
            Order order = orderServices.getTableOrder((Integer) orderP[i]);
            JsonElement jsonElement = gson.toJsonTree(order);
            jsonElement.getAsJsonObject().addProperty("price", orderServices.calculateTableBill(order.getTableNumber()));
            response += gson.toJson(jsonElement);
            if(i != orderP.length - 1){
                response += ",";
            }else if(i == orderP.length - 1){
                response += "]";
            }
        }
        return response;
    }
}
    

