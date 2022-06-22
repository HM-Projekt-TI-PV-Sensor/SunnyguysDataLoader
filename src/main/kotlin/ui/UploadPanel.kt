package ui

import db.DatabaseAccess
import db.MeasuredData
import java.awt.BorderLayout
import java.io.File
import java.nio.charset.StandardCharsets
import java.sql.Timestamp
import java.util.concurrent.CompletableFuture
import javax.swing.JButton
import javax.swing.JFileChooser
import javax.swing.JPanel
import javax.swing.JProgressBar

class UploadPanel : JPanel(BorderLayout()) {

    private val fileChooser = JFileChooser()
    private val button = JButton("Upload")
    private val progressBar = JProgressBar()

    init {
        val upper = JPanel()
        add(upper, BorderLayout.BEFORE_FIRST_LINE)

        val lower = JPanel(BorderLayout())
        add(lower, BorderLayout.AFTER_LAST_LINE)

        upper.add(fileChooser)

        button.addActionListener {
            upload()
        }
        lower.add(button, BorderLayout.BEFORE_FIRST_LINE)
        lower.add(progressBar, BorderLayout.AFTER_LAST_LINE)
    }

    private fun upload() {
        val selectedFile = fileChooser.selectedFile ?: return
        if (!selectedFile.exists() || selectedFile.isDirectory) {
            return
        }
        button.text = "Uploading..."
        button.isEnabled = false
        fileChooser.isEnabled = false
        CompletableFuture.runAsync {
            val measuredData = loadData(selectedFile)
            button.text = "Found ${measuredData.size} entries"
            pushData(measuredData)
            Thread.sleep(2000)
            button.isEnabled = true
            fileChooser.isEnabled = true
            button.text = "Upload"
        }
    }

    private fun loadData(file: File): List<MeasuredData> {
        button.text = "Loading data from ${file.name}"
        return translateToData(file)
    }

    private fun translateToData(file: File): List<MeasuredData> {
        val lines = file.readLines(StandardCharsets.UTF_8)
        return lines.mapNotNull(this::translateLine)
    }

    private fun translateLine(line: String): MeasuredData? {
        return try {
            val split = line.split(" > ")
            val stamp = split[0].toLong()
            val temp = split[1].split(" | ")[0].toDouble()
            val pv = split[1].split(" | ")[1].toDouble()
            MeasuredData(Timestamp(stamp * 1000), temp, pv)
        } catch (exception: Exception) {
            null
        }
    }

    private fun pushData(data: List<MeasuredData>) {
        if (data.isEmpty()) {
            button.text = "Nothing to push"
            return
        }
        progressBar.maximum = data.size + 1
        progressBar.minimum = 0
        progressBar.value = 0
        val batchSize = 10
        DatabaseAccess.withConnection { connection ->
            val sql = "INSERT INTO DATA (STAMP, TEMP, PV) VALUES(?, ?, ?) ON CONFLICT DO NOTHING"
            var currentStatement = connection.prepareStatement(sql)
            for ((count, entry) in data.withIndex()) {
                progressBar.value = count
                button.text = "Uploading [${count} / ${data.size}]"
                currentStatement.setTimestamp(1, entry.time)
                currentStatement.setDouble(2, entry.temp)
                currentStatement.setDouble(3, entry.pv)
                currentStatement.addBatch()
                if (count > 0 && count % batchSize == 0) {
                    currentStatement.executeBatch()
                    currentStatement = connection.prepareStatement(sql)
                }
            }
            try {
                currentStatement.executeBatch()
            } catch (_: Exception) {

            }
            progressBar.value = progressBar.maximum
            button.text = "Done"
        }
    }

}