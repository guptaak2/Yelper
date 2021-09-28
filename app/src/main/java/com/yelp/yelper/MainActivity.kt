package com.yelp.yelper

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yelp.yelper.adapter.BusinessAdapter
import com.example.yelp_td.databinding.ActivityMainBinding
import com.yelp.yelper.model.BusinessUiModel
import com.yelp.yelper.viewmodel.BusinessViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val businessViewModel: BusinessViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val businessAdapter = BusinessAdapter()

        initSearchBar()

        binding.businessGridView.apply {
            adapter = businessAdapter
        }

        businessViewModel.businesses.observe(this) { model ->
            handleBusinessResponse(model, businessAdapter)
        }
    }

    private fun handleBusinessResponse(model: BusinessUiModel, businessAdapter: BusinessAdapter) {
        when (model) {
            is BusinessUiModel.Businesses -> {
                businessAdapter.setItems(model.business)
                businessAdapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
                binding.businessGridView.visibility = View.VISIBLE
            }
            is BusinessUiModel.Error -> {
                binding.progressBar.visibility = View.GONE
                binding.businessGridView.visibility = View.GONE
                Toast.makeText(this, model.errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initSearchBar() {
        binding.searchEditText.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(p0: TextView?, p1: Int, p2: KeyEvent?): Boolean {
                if (EditorInfo.IME_ACTION_SEARCH == p1) {
                    p0?.hideKeyboard()
                    binding.businessGridView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                    businessViewModel.getBusinesses(p0?.text.toString())
                    return true
                }

                return false
            }
        })
    }

    fun View.hideKeyboard() {
        val inputManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(windowToken, 0)
    }
}