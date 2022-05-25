import com.google.gson.GsonBuilder
import java.io.File
import java.nio.charset.StandardCharsets
import java.sql.Connection
import java.sql.DriverManager
import java.sql.Timestamp

class Application {

    private val jarFile = File(Application::class.java.protectionDomain.codeSource.location.toURI())
    private val configFile = File(jarFile.parentFile, "config.json")
    private val config = loadConfig()

    init {
        println("> Starting with jar location: $jarFile")
    }

    fun run(generate: Boolean) {
        val data = if(generate) DataGenerator.generate(100) else loadData()
        pushData(data)
    }

    private fun loadData(): List<MeasuredData> {
        val data = fetchNearbyFiles().map(this::translateToData).flatten()
        println("> Loaded dataset with ${data.size} entries.")
        return data
    }

    private fun fetchNearbyFiles(): Array<out File> {
        val folder = jarFile.parentFile
        return folder.listFiles()?.filter { it.name.endsWith("txt") }?.toTypedArray() ?: arrayOf()
    }

    private fun translateToData(file: File): List<MeasuredData> {
        val lines = file.readLines(StandardCharsets.UTF_8)
        return lines.map(this::translateLine)
    }

    private fun translateLine(line: String): MeasuredData {
        val split = line.split(" > ")
        val stamp = split[0].toLong()
        val temp = split[1].split(" | ")[0].toDouble()
        val pv = split[1].split(" | ")[1].toDouble()
        return MeasuredData(Timestamp(stamp), temp, pv)
    }

    private fun loadConfig(): Config {
        val gson = GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create()
        if (!configFile.exists()) {
            println("> Creating config.json")
            configFile.writeText(gson.toJson(Config()), StandardCharsets.UTF_8)
        }
        println("> Reading config.json")
        return gson.fromJson(configFile.readText(StandardCharsets.UTF_8), Config::class.java)
    }

    private fun withJDBCConnection(function: (connection: Connection) -> Unit) {
        println("> Establishing database connection")
        Class.forName("org.postgresql.Driver")
        val connectionUrl = "jdbc:postgresql://${config.dbAddress}:${config.dbPort}/${config.dbTable}"
        val connection = DriverManager.getConnection(connectionUrl, config.dbUser, config.dbPassword)
        function.invoke(connection)
        println("> Closing database connection")
        connection.close()
    }

    private fun pushData(data: List<MeasuredData>) {
        if(data.isEmpty()) {
            println("> Nothing to push")
            return
        }
        withJDBCConnection { connection ->
            val sql = "INSERT INTO DATA (STAMP, TEMP, PV) VALUES(?, ?, ?)"
            val statement = connection.prepareStatement(sql)
            println("> Preparing SQL statement")
            for (entry in data) {
                statement.setTimestamp(1, entry.time)
                statement.setDouble(2, entry.temp)
                statement.setDouble(3, entry.pv)
                statement.addBatch()
            }
            println("> Executing batch query with ${data.size} elements:")
            println("> $sql")
            statement.executeBatch()
        }
    }

    data class Config(
        var dbAddress: String = "localhost",
        var dbPort: Int = 3001,
        var dbTable: String = "postgres",
        var dbUser: String = "postgres",
        var dbPassword: String = "admin"
    )
}

