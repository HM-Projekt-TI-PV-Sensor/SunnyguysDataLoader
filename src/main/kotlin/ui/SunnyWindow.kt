package ui

import java.awt.BorderLayout
import java.awt.Dimension
import javax.imageio.ImageIO
import javax.swing.ImageIcon
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.JTabbedPane

class SunnyWindow : JFrame("SunnyGuys data loader") {

    init {
        // Loads icon from "resources" directory
        this.iconImage = ImageIO.read(this::class.java.classLoader.getResource("icon.png"))

        // Centers the window
        setLocationRelativeTo(null)

        // Sets the window size
        preferredSize = Dimension(720, 480)

        // Needed for the X Button
        defaultCloseOperation = EXIT_ON_CLOSE

        // Setup panels
        val rootPanel = JPanel(BorderLayout())
        add(rootPanel, BorderLayout.CENTER)
        val infoPanel = InfoPanel()
        add(infoPanel, BorderLayout.SOUTH)

        // panel with several tabs
        val tabbedPanel = JTabbedPane()
        rootPanel.add(tabbedPanel)

        // Creates an instance of DatabasePanel and adds it as a tab
        tabbedPanel.addTab("Database", DatabasePanel(infoPanel))

        // Creates an instance of UploadPanel and adds it as a tab
        tabbedPanel.addTab("Upload", UploadPanel())

        pack()
        isResizable = false
        isVisible = true
    }

}