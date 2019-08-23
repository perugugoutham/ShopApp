package com.goutham.perugu.shopapp.viewmodel

data class ShoppingUIState (val pageType: PageType = Listing)

sealed class PageType
object Listing: PageType()
data class Details(val itemId: String): PageType()
object Cart : PageType()