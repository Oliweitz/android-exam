package sicnu.edu.cn.yangwencai2024110144.examapplication

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * 负责两个操作数的加、减、乘、除运算。
 */
class Calculator {

    fun add(first: Double, second: Double): Double = first + second

    fun subtract(first: Double, second: Double): Double = first - second

    fun multiply(first: Double, second: Double): Double = first * second

    fun divide(first: Double, second: Double): Double {
        if (second == 0.0) {
            throw ArithmeticException("Cannot divide by zero")
        }
        return first / second
    }

    fun calculate(first: Double, second: Double, operator: Char): Double = when (operator) {
        '+' -> add(first, second)
        '-' -> subtract(first, second)
        '*' -> multiply(first, second)
        '/' -> divide(first, second)
        else -> throw IllegalArgumentException("Unsupported operator: $operator")
    }

    /**
     * 将计算结果格式化为便于阅读的十进制字符串，最多保留 10 位小数。
     */
    fun format(result: Double): String {
        require(result.isFinite()) { "Result is not finite" }
        return BigDecimal.valueOf(result)
            .setScale(MAX_FRACTION_DIGITS, RoundingMode.HALF_UP)
            .stripTrailingZeros()
            .toPlainString()
    }

    private companion object {
        const val MAX_FRACTION_DIGITS = 10
    }
}
