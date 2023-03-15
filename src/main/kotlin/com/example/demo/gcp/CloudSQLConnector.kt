package com.example.demo.gcp

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*


@Service
class CloudSQLConnector {

    private val logger = LoggerFactory.getLogger(this::class.java)

    companion object {
        private const val CONNECTION_NAME = "tour_address"
        private const val HOST = "your_host"
        private const val USER = "postgres"
        private const val PASSWORD = "postgres"
    }

    init {
        logger.info("Loading PostgreSQL driver ...")
        Class.forName("org.postgresql.Driver")
    }

    fun connect() {
        logger.info("Connecting to the CloudSQL database ...")

        // Set up URL parameters
        val jdbcURL = "jdbc:postgresql://$HOST/test"
        val connProps = Properties()
        connProps.setProperty("user", USER)
        connProps.setProperty("password", PASSWORD)
        connProps.setProperty("socketFactory", "com.google.cloud.sql.postgres.SocketFactory")
        connProps.setProperty("cloudSqlInstance", CONNECTION_NAME)

        // Initialize connection pool
        val config = HikariConfig()
        config.jdbcUrl = jdbcURL
        config.dataSourceProperties = connProps
        config.connectionTimeout = 10000

        val connectionPool = HikariDataSource(config)

        // Execute create table operation
        logger.info("Creating table ...")
        connectionPool.connection.use { conn ->
            val stmt = """
                    CREATE TABLE test (
                        ID CHAR(20) NOT NULL,
                        TITLE TEXT NOT NULL
                    );
                    """;
            conn.prepareStatement(stmt).use { createTableStatement -> createTableStatement.execute() }
        }

        logger.info("Table was created successfully!")
    }
}