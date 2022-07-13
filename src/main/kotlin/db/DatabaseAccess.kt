package db

import java.sql.Connection
import java.sql.DriverManager

/**
 * This class is used for database credentials and access.
 */
object DatabaseAccess {

    init {
        Class.forName("org.postgresql.Driver")
    }

    var address = "localhost"
    var port = 3001
    var table = "postgres"
    var user = "postgres"
    var password = "admin"

    fun connectionUrl(): String {
        return "jdbc:postgresql://${address}:${port}/${table}"
    }

    fun withConnection(action: (Connection) -> Unit) {
        DriverManager.getConnection(connectionUrl(), user, password).use { action.invoke(it) }
    }

    fun ping(): Boolean {
        try {
            DriverManager.getConnection(connectionUrl(), user, password).use {  }
        } catch (exception: Exception) {
            return false
        }
        return true
    }

}