package sicnu.edu.cn.yangwencai2024110144.examapplication

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class CalculatorTest {

    private val calculator = Calculator()

    @Test
    fun add_returnsSum() {
        assertEquals(5.0, calculator.add(2.0, 3.0), 0.0)
    }

    @Test
    fun subtract_returnsDifference() {
        assertEquals(-1.0, calculator.subtract(2.0, 3.0), 0.0)
    }

    @Test
    fun multiply_returnsProduct() {
        assertEquals(6.0, calculator.multiply(2.0, 3.0), 0.0)
    }

    @Test
    fun divide_returnsQuotient() {
        assertEquals(2.5, calculator.divide(5.0, 2.0), 0.0)
    }

    @Test
    fun divide_byZero_throwsArithmeticException() {
        assertThrows(ArithmeticException::class.java) {
            calculator.divide(5.0, 0.0)
        }
    }

    @Test
    fun format_removesTrailingZerosAndFloatingPointArtifacts() {
        assertEquals("2.5", calculator.format(2.5))
        assertEquals("0.3", calculator.format(calculator.add(0.1, 0.2)))
    }

    @Test
    fun format_limitsRepeatingDecimal() {
        assertEquals("0.3333333333", calculator.format(1.0 / 3.0))
    }
}
