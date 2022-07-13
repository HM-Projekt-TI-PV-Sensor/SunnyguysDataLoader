package ui

import db.DatabaseAccess
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.Font
import javax.swing.JButton
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JPasswordField
import javax.swing.JTextField

/**
 * Panel that displays the credentials of DatabaseAccess.class
 */
class DatabasePanel(private val infoPanel: InfoPanel) : JPanel(BorderLayout()) {

    private val connectionUrlLabel = JLabel()

    /**
     * Initializes every property and creates an update button for every line.
     */
    init {
        this.preferredSize = Dimension(680, 300)

        val inPanel = JPanel()
        this.add(inPanel, BorderLayout.CENTER)

        val connectionUrlPanel = JPanel(BorderLayout())
        connectionUrlPanel.preferredSize = Dimension(720, 32)
        inPanel.add(connectionUrlPanel)
        connectionUrlPanel.add(connectionUrlLabel)
        updateUrlLabel()

        val addressPanel = JPanel(BorderLayout())
        addressPanel.preferredSize = Dimension(720, 30)
        inPanel.add(addressPanel)
        initPropertyPanel("Address", "localhost", addressPanel) { DatabaseAccess.address = it }

        val portPanel = JPanel(BorderLayout())
        portPanel.preferredSize = Dimension(720, 30)
        inPanel.add(portPanel)
        initPropertyPanel("Port", "3001", portPanel) { DatabaseAccess.port = it.toIntOrNull() ?: 3001 }

        val tablePanel = JPanel(BorderLayout())
        tablePanel.preferredSize = Dimension(720, 30)
        inPanel.add(tablePanel)
        initPropertyPanel("DB Table", "postgres", tablePanel) { DatabaseAccess.table = it }

        val userPanel = JPanel(BorderLayout())
        userPanel.preferredSize = Dimension(720, 30)
        inPanel.add(userPanel)
        initPropertyPanel("DB User", "postgres", userPanel) { DatabaseAccess.user = it }

        val passwordPanel = JPanel(BorderLayout())
        passwordPanel.preferredSize = Dimension(720, 30)
        inPanel.add(passwordPanel)
        initPropertyPanel("DB Password", "admin", passwordPanel, true) { DatabaseAccess.password = it }
    }

    /**
     * Initializes a property panel
     */
    private fun initPropertyPanel(labelName: String, defaultValue: String, parent: JPanel, pw: Boolean = false, action: (String) -> Unit) {
        val label = JLabel(labelName)
        label.font = Font("Arial", Font.BOLD, 14)
        label.preferredSize = Dimension(128, 30)
        parent.add(label, BorderLayout.EAST)
        val inputPanel = if (!pw) JTextField() else JPasswordField()
        inputPanel.font = Font("Arial", Font.BOLD, 13)
        inputPanel.preferredSize = Dimension(380, 30)
        inputPanel.text = defaultValue
        parent.add(inputPanel, BorderLayout.CENTER)
        val button = JButton("Apply")
        button.font = Font("Arial", Font.BOLD, 14)
        button.addActionListener {
            action.invoke(inputPanel.text)
            updateUrlLabel()
            infoPanel.update(true)
        }
        button.preferredSize = Dimension(80, 30)
        parent.add(button, BorderLayout.WEST)
    }

    /**
     * Updates the JDBC String at the top of the panel
     */
    private fun updateUrlLabel() {
        connectionUrlLabel.font = Font("Arial", Font.BOLD, 16)
        connectionUrlLabel.text = "    ${DatabaseAccess.connectionUrl()}"
    }

}