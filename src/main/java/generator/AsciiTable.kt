package generator

import kotlin.math.max
import kotlin.math.min


class AsciiTable(
    var maxColumnWidth: Int = Integer.MAX_VALUE,
    private val columns: MutableList<Column> = mutableListOf(),
    private val data: MutableList<Row> = mutableListOf()
) {
    fun addColumn(name: String) {
        columns.add(Column(name))
    }

    fun addRow(vararg text: String) {
        val element = Row(text.toMutableList())
        data.add(element)
    }

    private fun calculateColumnWidth() {

        for (column in columns) {
            column.width = column.name.length + 1
        }

        for (row in data) {
            for ((colIdx, value) in row.values.withIndex()) {
                val column = columns[colIdx]

                column.width = max(column.width, value.length + 1)
            }
        }

        for (column in columns) {
            column.width = min(column.width, maxColumnWidth)
        }
    }

    fun render() {
        calculateColumnWidth()
        val sb = StringBuilder()

        writeSeparator(columns, sb)
        writeColumnNames(columns, sb)
        writeSeparator(columns, sb)

        // values
        writeValues(columns, data, sb)

        writeSeparator(columns, sb)

        println(sb.toString())
    }

    private fun writeColumnNames(columns: List<Column>, sb: StringBuilder) {
        sb.append("|")
        for (column in columns) {
            sb.append(String.format(" %-" + column.width + "s", column.name))
            sb.append("|")
        }
        sb.append("\n")
    }

    private fun writeSeparator(columns: List<Column>, sb: StringBuilder) {
        sb.append("+")
        for (column in columns) {
            sb.append(String.format("%-" + (column.width + 1) + "s", "").replace(' ', '-'))
            sb.append("+")
        }
        sb.append("\n")
    }

    private fun writeValues(columns: List<Column>, rows: List<Row>, sb: StringBuilder) {
        for (row in rows) {
            sb.append("|")
            for ((columnIdx, value) in row.values.withIndex()) {
                var temp = value
                if (temp.length > maxColumnWidth)
                    temp = value.substring(0, maxColumnWidth - 1)

                sb.append(String.format(" %-" + columns[columnIdx].width + "s", temp))
                sb.append("|")

            }
            sb.append("\n")
        }
    }

    data class Column(val name: String, var width: Int = 0)

    data class Row(val values: MutableList<String> = mutableListOf())
}