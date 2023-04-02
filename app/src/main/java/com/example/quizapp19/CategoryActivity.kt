package com.example.quizapp19

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryList: List<Category>
    private lateinit var dbHelper: QuizDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        dbHelper = QuizDatabaseHelper(this)

        recyclerView = findViewById(R.id.categoryRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        categoryList = dbHelper.getCategories()

        val categoryAdapter = CategoryAdapter(categoryList)

        recyclerView.adapter = categoryAdapter
    }

    inner class CategoryAdapter(
        private val categories: List<Category>
    ) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

        inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val categoryTextView: TextView = itemView.findViewById(R.id.categoryTextView)

            init {
                itemView.setOnClickListener {
                    val position = absoluteAdapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val category = categories[position]
                        val intent = Intent(itemView.context, QuizActivity::class.java)
                        intent.putExtra("CATEGORY_ID", category.id)

                        itemView.context.startActivity(intent)
                    }
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.category_item, parent, false)
            return CategoryViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
            val currentCategory = categories[position]
            holder.categoryTextView.text = currentCategory.name
        }

        override fun getItemCount(): Int {
            return categories.size
        }
    }

}