package com.example.quiz_app_official

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.InputStream

class AdminUploadQuestions : AppCompatActivity() {

    // Register the activity result launcher for picking the Excel file
    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            // Call the method to process the Excel file once the URI is selected
            processExcelFile(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_upload_questions)

        // Set the button click listener
        findViewById<Button>(R.id.btnUploadExcel).setOnClickListener {
            // Launch the file picker for Excel files
            getContent.launch("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
        }
    }

    // Method to process the selected Excel file
    private fun processExcelFile(uri: Uri) {
        try {
            // Open an input stream from the content resolver
            val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return

            // Create a workbook object from the input stream
            val workbook = WorkbookFactory.create(inputStream)

            // Get the first sheet from the workbook
            val sheet = workbook.getSheetAt(0)

            // Loop through the rows in the sheet
            for (row in sheet) {
                // Skip the header row (row 0)
                if (row.rowNum == 0) continue

                // Read the values from the cells (question, options, answer)
                val firstQuestion = row.getCell(0)?.stringCellValue ?: ""
                val optionA = row.getCell(1)?.stringCellValue ?: ""
                val optionB = row.getCell(2)?.stringCellValue ?: ""
                val optionC = row.getCell(3)?.stringCellValue ?: ""
                val optionD = row.getCell(4)?.stringCellValue ?: ""
                val answer = row.getCell(5)?.stringCellValue ?: ""

                // Log the extracted data for debugging
                Log.d("ExcelData", "Question: $firstQuestion, Options: A=$optionA, B=$optionB, C=$optionC, D=$optionD, Answer=$answer")
            }

            // Close the workbook to release resources
            workbook.close()

            // Show a success message to the user
            Toast.makeText(this, "Questions imported successfully", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            // Catch any exceptions and print the stack trace
            e.printStackTrace()

            // Show a failure message to the user
            Toast.makeText(this, "Failed to import questions", Toast.LENGTH_SHORT).show()
        }
    }
}
