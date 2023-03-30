package com.example.quizapp19

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoryActivity : AppCompatActivity(), CategoryAdapter.OnCategoryClickListener {

    private lateinit var categoryRecyclerView: RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        // Initialize the RecyclerView
        categoryRecyclerView = findViewById(R.id.category_recycler_view)
        categoryRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch categories from the database
        val dbHelper = QuizDatabaseHelper(this)
        val categories = dbHelper.getCategories()

        // Set up the adapter with the fetched categories
        categoryAdapter = CategoryAdapter(categories, this)
        categoryRecyclerView.adapter = categoryAdapter
    }

    override fun onCategoryClick(category: Category) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("categoryId", category.id)
        startActivity(intent)
    }
}