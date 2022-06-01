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
        this.iconImage = ImageIO.read(this::class.java.classLoader.getResource("icon.png"))
        setLocationRelativeTo(null)

        preferredSize = Dimension(720, 480)
        defaultCloseOperation = EXIT_ON_CLOSE

        val rootPanel = JPanel(BorderLayout())
        add(rootPanel, BorderLayout.CENTER)
        val infoPanel = InfoPanel()
        add(infoPanel, BorderLayout.SOUTH)

        val tabbedPanel = JTabbedPane()
        rootPanel.add(tabbedPanel)

        tabbedPanel.addTab("Database", DatabasePanel(infoPanel))
        tabbedPanel.addTab("Upload", UploadPanel())

        pack()
        isResizable = false
        isVisible = true
    }

}