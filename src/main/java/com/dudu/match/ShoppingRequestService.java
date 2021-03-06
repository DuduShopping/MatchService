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
public class ShoppingRequestService {
    public static final DatabaseHelper databaseHelper = DatabaseHelper.getHelper();
    public static final String SHOPPING_REQUEST_STATE_PENDING = "SR5";
    public static final String SHOPPING_REQUEST_STATE_CANCELED = "SR10";
    public static final String SHOPPING_REQUEST_STATE_ACCEPTED = "SR15";
    public static final String SHOPPING_REQUEST_STATE_MATCHED = "SR30";
    private DataSource source;

    public ShoppingRequestService(DataSource dataSource) {
        this.source = dataSource;
    }

    public ShoppingRequest createRequest(long userId, String description) throws SQLException {
        try (Connection con = source.getConnection()) {
            String insert = "INSERT INTO ShoppingRequests(UserId,Text,State) VALUES (?,?,?)";
            long shoppingRequestId;
            try (PreparedStatement ps = con.prepareStatement(insert, new String[] {"ShoppingRequestId"})) {
                ps.setLong(1, userId);
                ps.setString(2, description);
                ps.setString(3, SHOPPING_REQUEST_STATE_PENDING);
                int count = ps.executeUpdate();
                if (count != 1)
                    throw new SQLException("Expecting a new ShoppingRequest created.");

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    rs.next();
                    shoppingRequestId = rs.getLong(1);
                }
            }

            ShoppingRequest request = getRequest(userId, shoppingRequestId);
            if (request == null)
                throw new SQLException("ShoppingRequest is null");

            return getRequest(userId, shoppingRequestId);
        }
    }

    public void cancelRequest(long userId, long shoppingRequestId) throws SQLException {
        try (Connection con = source.getConnection()) {
            String update = "UPDATE ShoppingRequests SET State = ? WHERE ShoppingRequestId = ? AND State IN (?,?) AND UserId = ?";
            int count = databaseHelper.update(con, update, SHOPPING_REQUEST_STATE_CANCELED, shoppingRequestId,
                    SHOPPING_REQUEST_STATE_PENDING, SHOPPING_REQUEST_STATE_ACCEPTED, userId);
            if (count != 1)
                throw new SQLException("Request not cancelled: userId=" + userId + ", shoppingRequestId=" + shoppingRequestId);
        }
    }

    public void acceptRequest(long userId, long shoppingRequestId, long shoppingOfferId) throws SQLException {
        ShoppingOfferService shoppingOfferService = new ShoppingOfferService(source);
        ShoppingOffer shoppingOffer = shoppingOfferService.getOffer(userId, shoppingOfferId);
        if (!shoppingOffer.getState().equals(ShoppingOfferService.SHOPPING_OFFER_STATE_PENDING))
            throw new IllegalArgumentException("Invalid shoppingOfferId");

        try (Connection con = source.getConnection()) {
            String update = "UPDATE ShoppingRequests SET State = ? WHERE State = ? AND UserId = ? AND ShoppingRequestId = ? ";
            int count = databaseHelper.update(con, update, SHOPPING_REQUEST_STATE_ACCEPTED, SHOPPING_REQUEST_STATE_PENDING, userId, shoppingRequestId);
            if (count != 1)
                throw new SQLException("Request not accepted: userId=" + userId + ", shoppingRequestId=" + shoppingRequestId);
        }
    }

    public ShoppingRequest getRequest(long userId, long shoppingRequestId) throws SQLException {
        try (Connection con = source.getConnection()) {
            String retrieve = "SELECT * FROM ShoppingRequests WHERE ShoppingRequestId = ? AND UserId = ?";
            DatabaseResult databaseResult = databaseHelper.query(con, retrieve, shoppingRequestId, userId);

            if (databaseResult.isEmpty())
                return null;

            return ShoppingRequest.from(databaseResult.get(0));
        }
    }

    /**
     *
     * @param userId
     * @param requestId ignore on <= 0
     * @param states
     * @return
     * @throws SQLException
     */
    public List<ShoppingRequest> searchRequests(long userId, long requestId, List<String> states) throws SQLException {
        try (Connection con = source.getConnection()) {
            StringBuilder query = new StringBuilder("SELECT * FROM ShoppingRequests WHERE UserId = ? ");
            List<Object> parameters = new ArrayList<>();
            parameters.add(userId);

            if (requestId > 0) {
                query.append(" AND ShoppingRequestId = ? ");
                parameters.add(requestId);
            }

            if (states != null && states.size() > 0) {
                query.append(" AND State IN (");
                for (int i = 0; i < states.size(); i++)
                    query.append(i == 0 ? "?" : ",?");
                query.append(")");
            }

            try (PreparedStatement ps = con.prepareStatement(query.toString())) {
                for (int i = 1; i < parameters.size(); i++)
                    ps.setObject(i, parameters.get(i));

                DatabaseResult result = databaseHelper.query(ps);
                List<ShoppingRequest> shoppingRequests = new ArrayList<>();
                for (DatabaseRow row : result)
                    shoppingRequests.add(ShoppingRequest.from(row));
                return shoppingRequests;
            }
        }
    }

}
