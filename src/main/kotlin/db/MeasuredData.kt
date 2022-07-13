package db

import java.sql.Timestamp

/**
 * Simple data class | time | temp | pv |
 */
data class MeasuredData(val time: Timestamp, val temp: Double, val pv: Double)
