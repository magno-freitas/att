package vet.util;

import vet.dao.ConnectionPool;
import vet.exception.DatabaseException;
import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager {
    
    @FunctionalInterface
    public interface TransactionBlock {
        void execute(Connection connection) throws SQLException;
    }
    
    public static void executeInTransaction(TransactionBlock block) throws DatabaseException {
        Connection conn = null;
        try {
            conn = ConnectionPool.getConnection();
            conn.setAutoCommit(false);
            
            block.execute(conn);
            
            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseException("Failed to rollback transaction", ex);
                }
            }
            throw new DatabaseException("Transaction failed", e);
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    LogManager.logError("Failed to close connection", e);
                }
            }
        }
    }
}