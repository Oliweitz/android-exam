package sicnu.edu.cn.yangwencai2024110144.examapplication

/**
 * 一条计算历史记录。
 */
data class HistoryRecord(
    val expression: String,
    val result: String,
    val timestamp: Long
)
