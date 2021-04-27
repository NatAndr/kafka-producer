package org.example.model

case class Book(
    name: String,
    author: String,
    userRating: Double,
    reviews: Int,
    price: Double,
    year: Int,
    genre: String
)
