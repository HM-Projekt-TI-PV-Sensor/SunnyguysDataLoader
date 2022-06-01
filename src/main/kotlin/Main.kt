import java.io.File
import ui.SunnyWindow
import util.DataGenerator

fun main() {
    SunnyWindow()
}

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
