package util

import db.MeasuredData
import java.sql.Timestamp
import java.util.concurrent.ThreadLocalRandom

class DataGenerator {

    companion object {
        fun generate(amount: Int): List<MeasuredData> {
            println("> Generating $amount data entries...")
            val list: MutableList<MeasuredData> = mutableListOf()
            var temp = 20.0
            var pv = 500.0
            for(i in 0 until amount) {
                temp += ThreadLocalRandom.current().nextDouble(-0.1, 0.1)
                pv += ThreadLocalRandom.current().nextDouble(-1.0, 1.0)
                if(ThreadLocalRandom.current().nextBoolean()) {
                    temp *= ThreadLocalRandom.current().nextDouble(0.85, 0.95)
                }
                if(ThreadLocalRandom.current().nextBoolean()) {
                    temp *= ThreadLocalRandom.current().nextDouble(1.05, 1.15)
                }
                if(ThreadLocalRandom.current().nextBoolean()) {
                    pv *= ThreadLocalRandom.current().nextDouble(0.85, 0.95)
                }
                if(ThreadLocalRandom.current().nextBoolean()) {
                    pv *= ThreadLocalRandom.current().nextDouble(1.05, 1.15)
                }
                list.add(MeasuredData(Timestamp(System.currentTimeMillis()), temp, pv))
                Thread.sleep(10)
            }
            println("> Done")
            return list
        }
    }

}