fun main(args: Array<out String>) {
    val upload = args.isNotEmpty() && args[0] == "upload"
    if(upload) {
        println("> Started application for uploading")
    } else {
        println("> Started application for generating")
    }
    Application().run(!upload)
}

