package sicnu.edu.cn.yangwencai2024110144.examapplication

import android.content.Context
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

/**
 * 使用 SharedPreferences 持久化保存计算历史。
 */
class HistoryStore(context: Context) {

    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun add(record: HistoryRecord) {
        val records = buildList {
            add(record)
            addAll(getAll())
        }
        val jsonArray = JSONArray()
        records.forEach { entry ->
            jsonArray.put(
                JSONObject()
                    .put(KEY_EXPRESSION, entry.expression)
                    .put(KEY_RESULT, entry.result)
                    .put(KEY_TIMESTAMP, entry.timestamp)
            )
        }
        preferences.edit { putString(KEY_RECORDS, jsonArray.toString()) }
    }

    fun getAll(): List<HistoryRecord> {
        val rawHistory = preferences.getString(KEY_RECORDS, null) ?: return emptyList()
        return runCatching {
            val jsonArray = JSONArray(rawHistory)
            buildList {
                for (index in 0 until jsonArray.length()) {
                    val item = jsonArray.optJSONObject(index) ?: continue
                    val expression = item.optString(KEY_EXPRESSION)
                    val result = item.optString(KEY_RESULT)
                    val timestamp = item.optLong(KEY_TIMESTAMP)
                    if (expression.isNotEmpty() && result.isNotEmpty()) {
                        add(HistoryRecord(expression, result, timestamp))
                    }
                }
            }
        }.getOrDefault(emptyList())
    }

    private companion object {
        const val PREFERENCES_NAME = "calculator_history"
        const val KEY_RECORDS = "records"
        const val KEY_EXPRESSION = "expression"
        const val KEY_RESULT = "result"
        const val KEY_TIMESTAMP = "timestamp"
    }
}
