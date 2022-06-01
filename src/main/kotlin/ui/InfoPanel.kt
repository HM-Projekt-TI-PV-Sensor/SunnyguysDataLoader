package ui

import db.DatabaseAccess
import java.awt.Dimension
import java.util.Timer
import java.util.concurrent.CompletableFuture
import javax.swing.JLabel
import javax.swing.JPanel
import kotlin.concurrent.scheduleAtFixedRate

class InfoPanel: JPanel() {

    private val dotLabel = JLabel()
    private val textLabel = JLabel()

    init {
        preferredSize = Dimension(720, 32)
        add(dotLabel)
        add(textLabel)
        val timer = Timer()
        timer.scheduleAtFixedRate(1000, 4000) {
            update(false)
        }
    }

    fun update(preText: Boolean) {
        if(preText) {
            textLabel.text = "Pinging Database..."
            dotLabel.text = "<html><font color='#9E9E9E'>⏺ </font>"
        }
        CompletableFuture.runAsync {
            if(DatabaseAccess.ping()) {
                textLabel.text = "Database reachable"
                dotLabel.text = "<html><font color='#1AD21A'>⏺ </font>"
            } else {
                textLabel.text = "Database unreachable"
                dotLabel.text = "<html><font color='#FF1B1B'>⏺ </font>"
            }
        }
    }

}