import java.io.File
import ui.SunnyWindow
import util.DataGenerator

/**
 * Entry point. Simply instantiates SunnyWindow.class
 */
fun main() {
    SunnyWindow()
}

/**
 * Utility method for generating random data
 */
fun gen() {
    println("Generating")
    val data = DataGenerator.generate(100)
    val builder = java.lang.StringBuilder()
    println("Mapping")
    data.forEach {
        builder.append("${it.time.time} > ${it.temp} | ${it.pv}\n")
    }
    println("Writing")
    val file = File("data.txt")
    if(!file.exists()) {
        file.createNewFile()
    }
    file.writeText(builder.toString())
}
