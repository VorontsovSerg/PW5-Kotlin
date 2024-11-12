package com.example.pw5kotlin

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.pw5kotlin.data.ProductEntity

class ProductListActivity : AppCompatActivity() {

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_list)

        val tableLayout = findViewById<TableLayout>(R.id.tableLayoutProducts)

        // Наблюдаем за изменениями в базе данных и обновляем таблицу
        productViewModel.allProducts.observe(this, Observer { products ->
            if (products != null) {
                tableLayout.removeViews(1, tableLayout.childCount - 1)

                for (product in products) {
                    val row = TableRow(this)
                    row.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    val idTextView = createTextView(product.id.toString(), 0.1f, Gravity.CENTER)
                    val titleTextView = createTextView(product.title, 0.5f, Gravity.START)
                    val priceTextView = createTextView("${product.price}", 0.2f, Gravity.CENTER)

                    val imageView = ImageView(this).apply {
                        layoutParams = TableRow.LayoutParams(
                            150,
                            150,
                            0.2f
                        ).apply {
                            gravity = Gravity.CENTER
                        }
                        scaleType = ImageView.ScaleType.FIT_CENTER
                    }

                    // Загружаем изображение с помощью Glide
                    Glide.with(this)
                        .load(product.thumbnail)
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.error_image)
                        .into(imageView)

                    row.addView(idTextView)
                    row.addView(titleTextView)
                    row.addView(priceTextView)
                    row.addView(imageView)

                    tableLayout.addView(row)
                }
            }
        })
    }

    // Функция для создания текстового элемента для ячейки таблицы с заданным весом и выравниванием
    private fun createTextView(text: String, weight: Float, gravity: Int): TextView {
        return TextView(this).apply {
            this.text = text
            this.setPadding(8, 8, 8, 8)
            this.gravity = gravity
            this.layoutParams = TableRow.LayoutParams(
                0, // Используем вес для ширины
                TableRow.LayoutParams.WRAP_CONTENT,
                weight
            )
        }
    }
}
