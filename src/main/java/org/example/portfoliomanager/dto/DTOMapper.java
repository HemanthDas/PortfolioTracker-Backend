package org.example.portfoliomanager.dto;

import org.example.portfoliomanager.models.Stock;
import org.example.portfoliomanager.models.User;

public class DTOMapper {
    public static UserDTO toUserDTO(User user) {
        return new UserDTO(user.getId(), user.getUsername());
    }

    public static StockDTO toStockDTO(Stock stock) {
        UserDTO userDTO = toUserDTO(stock.getUser());
        return new StockDTO(stock.getId(), stock.getName(), stock.getTicker(), stock.getQuantity(), stock.getBuyPrice(), userDTO);
    }
}
