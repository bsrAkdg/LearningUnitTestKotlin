package com.bsrakdg.shoppingapp.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bsrakdg.shoppingapp.R
import com.bsrakdg.shoppingapp.adapter.ShoppingItemAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_shopping.*
import javax.inject.Inject

class ShoppingFragment
@Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter,
    var viewModel: ShoppingViewModel? = null
) : Fragment(R.layout.fragment_shopping) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
            We set this viewModel to the testViewModel in our test cases
            the change will only happen after all of our lifecycle functions were called.
            So after we already observed on the viewModel liveData from our real viewModel and not
            from our fake viewModel and this will just not work so in our ShoppingItemFragment we also
            had a viewModel which we said in our test cases but there this was not a problem
            because that was basically a user triggered an event when we added a shoppingItem and
            hat was not an event that was immediately triggered in onViewCreated. But here that is
            the case so what we need to do is? We need to have a way to pass this viewModel in the constructor
            of the fragment. So once we construct this fragment object we already want to tell that
            we want to have a viewModel with a fake shopping repository in our test cases. And in our
            real fragment we don't want that but since we need to use a fragment factory for that
            that is actually not that easy so what we actually need to do is we need to create a custom
            fragment factory for our tests. And in that fragment factory we will instantiate this shopping
            fragment with a viewModel that was created with a fake repository.
         */

        viewModel = viewModel ?: ViewModelProvider(requireActivity()).get(ShoppingViewModel::class.java)

        subscribeToObservers()
        setupRecyclerView()

        fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }
    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[position]
            viewModel?.deleteShoppingItem(item)

            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }

    private fun subscribeToObservers() {
        viewModel?.shoppingItems?.observe(viewLifecycleOwner, {
            shoppingItemAdapter.shoppingItems = it
        })

        viewModel?.totalPrice?.observe(viewLifecycleOwner, {
            val price = it ?: 0f
            val priceText = "Total Price : $price$"
            tvShoppingItemPrice.text = priceText
        })
    }

    private fun setupRecyclerView() {
        rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }
}