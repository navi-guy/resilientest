package com.tesis.resilientest.database;

import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DSLContextProvider {

    private static DSLContext dslContext;

    private DSLContextProvider() {
    }

    public static DSLContext getInstance() {
        if (dslContext == null) {
            synchronized (DSLContextProvider.class) {
                if (dslContext == null) {
                    try {
                        String dbUrl = "jdbc:sqlite:mydb.db";
                        Connection connection = DriverManager.getConnection(dbUrl);
                        dslContext = DSL.using(connection, SQLDialect.SQLITE);
                    } catch (SQLException e) {
                        throw new RuntimeException("Error while initializing DSLContext", e);
                    }
                }
            }
        }
        return dslContext;
    }

    public static void close() {
        if (dslContext != null) {
            try {
                dslContext.configuration().derive(dslContext.configuration().dialect()).connectionProvider().acquire().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
