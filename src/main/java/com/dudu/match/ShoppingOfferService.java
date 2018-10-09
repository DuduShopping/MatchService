package com.dudu.match;

import com.dudu.database.DatabaseHelper;
import com.dudu.database.DatabaseResult;
import com.dudu.database.DatabaseRow;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingOfferService {
    public static final DatabaseHelper databaseHelper = DatabaseHelper.getHelper();
    public static final String SHOPPING_OFFER_STATE_PENDING = "SF5";
    public static final String SHOPPING_OFFER_STATE_PULLED = "SF15";
    public static final String SHOPPING_OFFER_STATE_REJECTED = "SF20";
    public static final String SHOPPING_OFFER_STATE_MATCHED = "SF35";

    private DataSource source;

    public ShoppingOfferService(DataSource source) {
        this.source = source;
    }

    public ShoppingOffer createOffer(long userId, long shoppingRequestId, String description, long price) throws SQLException {
        try (Connection con = source.getConnection()) {
            String insert = "INSERT INTO ShoppingOffers(UserId, ShoppingRequestId, Text, Price, State) VALUES (?,?,?,?,?)";
            try (PreparedStatement ps = con.prepareStatement(insert, new String[]{"ShoppingOfferId"})) {
                ps.setLong(1, userId);
                ps.setLong(2, shoppingRequestId);
                ps.setString(3, description);
                ps.setLong(4, price);
                ps.setString(5, SHOPPING_OFFER_STATE_PENDING);

                int count = ps.executeUpdate();
                if (count != 1)
                    throw new SQLException("Expect a new offer.");

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    long ShoppingOfferId = rs.getLong(1);
                    ShoppingOffer shoppingOffer = getOffer(userId, ShoppingOfferId);
                    if (shoppingOffer == null)
                        throw new SQLException("ShoppingOffer is null");

                    return shoppingOffer;
                }
            }
        }
    }

    /**
     *
     * @param userId
     * @param shoppingOfferId   ignored on <= 0
     * @param shoppingRequestId ignored on <= 0
     * @param states
     * @return
     * @throws SQLException
     */
    public List<ShoppingOffer> searchOffers(long userId, long shoppingOfferId, long shoppingRequestId, List<String> states) throws SQLException {
        try (Connection con = source.getConnection()) {
            StringBuilder query = new StringBuilder("SELECT * FROM ShoppingOffers WHERE UserId = ? ");
            List<Object> params = new ArrayList<>();
            params.add(userId);

            if (shoppingOfferId > 0) {
                query.append(" AND ShoppingOfferId = ? ");
                params.add(shoppingOfferId);
            }

            if (shoppingRequestId > 0) {
                query.append(" AND ShoppingRequestId = ? ");
                params.add(shoppingRequestId);
            }

            if (states != null && states.size() > 0) {
                query.append(" AND State IN (");
                for (int i = 0; i < states.size(); i++) {
                    query.append(i == 0 ? "?" : ",?");
                    params.add(states.get(i));
                }
                query.append(") ");
            }

            try (PreparedStatement ps = con.prepareStatement(query.toString())) {
                for (int i = 1; i <= states.size(); i++) {
                    ps.setObject(i, params.get(i-1));
                }

                DatabaseResult databaseRows = databaseHelper.query(ps);
                List<ShoppingOffer> shoppingOffers = new ArrayList<>();
                for (DatabaseRow row : databaseRows)
                    shoppingOffers.add(ShoppingOffer.from(row));
                return shoppingOffers;
            }
        }
    }

    public void rejectOffer(long userId, long shoppingOfferId) throws SQLException {
        try (Connection con = source.getConnection()) {
            String update = "UPDATE ShoppingOffers SET State = ? WHERE UserId = ? AND ShoppingOfferId = ? AND State = ? ";
            int count = databaseHelper.update(con, update, SHOPPING_OFFER_STATE_REJECTED, userId, shoppingOfferId, SHOPPING_OFFER_STATE_PENDING);
            if (count != 1)
                throw new SQLException("Expect an offer rejected: userId=" + userId + ", shoppingOfferId=" + shoppingOfferId);
        }
    }

    public void pullOffer(long userId, long shoppingOfferId) throws SQLException {
        try (Connection con = source.getConnection()) {
            String update = "UPDATE ShoppingOffers SET State = ? WHERE UserId = ? AND ShoppingOfferId = ? AND State = ? ";
            int count = databaseHelper.update(con, update, SHOPPING_OFFER_STATE_PULLED, userId, shoppingOfferId, SHOPPING_OFFER_STATE_PENDING);
            if (count != 1)
                throw new SQLException("Expect an offer pulled: userId=" + userId + ", shoppingOfferId=" + shoppingOfferId);
        }
    }

    public ShoppingOffer getOffer(long userId, long shoppingOfferId) throws SQLException {
        try (Connection con = source.getConnection()) {
            String select = "SELECT * FROM ShoppingOffers WHERE UserId = ? AND ShoppingOfferId = ?";
            DatabaseResult databaseResult = databaseHelper.query(con, select, userId, shoppingOfferId);
            if (databaseResult.isEmpty())
                return null;

            return ShoppingOffer.from(databaseResult.get(0));
        }
    }
}
