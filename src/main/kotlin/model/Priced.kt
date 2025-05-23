package com.example.model

interface PriceReadable {
    val price: Double
}

interface Priced : PriceReadable {
    override var price: Double
}

interface PricedConst : PriceReadable {
    override val price: Double
}
