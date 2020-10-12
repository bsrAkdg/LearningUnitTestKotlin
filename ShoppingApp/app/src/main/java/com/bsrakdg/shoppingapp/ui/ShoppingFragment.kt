package com.bsrakdg.shoppingapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bsrakdg.shoppingapp.R

class ShoppingFragment : Fragment(R.layout.fragment_shopping) {

    val shoppingViewModel: ShoppingViewModel by activityViewModels()

}