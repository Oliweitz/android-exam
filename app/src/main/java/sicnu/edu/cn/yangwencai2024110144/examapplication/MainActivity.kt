package sicnu.edu.cn.yangwencai2024110144.examapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import sicnu.edu.cn.yangwencai2024110144.examapplication.databinding.ActivityMainBinding

/**
 * 第一个 Activity：输入操作数、选择运算符并显示计算结果。
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var historyStore: HistoryStore
    private val calculator = Calculator()
    private var currentResult: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        configureSystemBars()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = getString(R.string.student_title)

        historyStore = HistoryStore(this)
        binding.calculateButton.setOnClickListener { calculateResult() }
        binding.inputSecond.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calculateResult()
                true
            } else {
                false
            }
        }

        currentResult = savedInstanceState?.getString(STATE_RESULT)
        renderResult()
        if (currentResult != null) {
            binding.scrollView.post { binding.scrollView.fullScroll(View.FOCUS_DOWN) }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_history -> {
                startActivity(Intent(this, HistoryActivity::class.java))
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(STATE_RESULT, currentResult)
        super.onSaveInstanceState(outState)
    }

    private fun calculateResult() {
        binding.inputFirst.error = null
        binding.inputSecond.error = null

        val first = readNumber(
            input = binding.inputFirst,
            requiredMessage = R.string.error_first_required
        ) ?: return

        val second = readNumber(
            input = binding.inputSecond,
            requiredMessage = R.string.error_second_required
        ) ?: return

        val operator = selectedOperator()
        if (operator == null) {
            binding.resultValue.text = getString(R.string.error_choose_operation)
            return
        }

        if (operator == '/' && second == 0.0) {
            binding.inputSecond.error = getString(R.string.error_divide_by_zero)
            return
        }

        val result = try {
            calculator.calculate(first, second, operator)
        } catch (exception: RuntimeException) {
            val message = if (exception is ArithmeticException) {
                getString(R.string.error_divide_by_zero)
            } else {
                getString(R.string.error_calculation)
            }
            binding.inputSecond.error = message
            return
        }

        if (!result.isFinite()) {
            binding.resultValue.text = getString(R.string.error_calculation)
            return
        }

        val firstText = binding.inputFirst.text?.toString()?.trim().orEmpty()
        val secondText = binding.inputSecond.text?.toString()?.trim().orEmpty()
        val expression = getString(R.string.expression_format, firstText, operator, secondText)
        val formattedResult = calculator.format(result)

        currentResult = formattedResult
        renderResult()
        historyStore.add(
            HistoryRecord(
                expression = expression,
                result = formattedResult,
                timestamp = System.currentTimeMillis()
            )
        )

        hideKeyboard()
        binding.scrollView.post { binding.scrollView.fullScroll(View.FOCUS_DOWN) }
    }

    private fun readNumber(input: EditText, requiredMessage: Int): Double? {
        val rawValue = input.text?.toString()?.trim().orEmpty()
        if (rawValue.isEmpty()) {
            input.error = getString(requiredMessage)
            return null
        }

        val value = rawValue.toDoubleOrNull()
        if (value == null || !value.isFinite()) {
            input.error = getString(R.string.error_invalid_number)
            return null
        }
        return value
    }

    private fun selectedOperator(): Char? {
        return when (binding.operationGroup.checkedRadioButtonId) {
            R.id.operation_add -> '+'
            R.id.operation_subtract -> '-'
            R.id.operation_multiply -> '*'
            R.id.operation_divide -> '/'
            else -> null
        }
    }

    private fun renderResult() {
        binding.resultValue.text = currentResult ?: getString(R.string.result_placeholder)
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(InputMethodManager::class.java)
        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
        binding.inputFirst.clearFocus()
        binding.inputSecond.clearFocus()
    }

    private fun configureSystemBars() {
        WindowCompat.getInsetsController(window, window.decorView).apply {
            isAppearanceLightStatusBars = true
            isAppearanceLightNavigationBars = true
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val ime = insets.getInsets(WindowInsetsCompat.Type.ime())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                maxOf(systemBars.bottom, ime.bottom)
            )
            insets
        }
    }

    private companion object {
        const val STATE_RESULT = "state_result"
    }
}
