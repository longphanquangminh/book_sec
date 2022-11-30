package com.tranthikieutrinh.models;

import android.graphics.Bitmap;

public class Book {
    int bookId;
    String bookName;
    int bookEdition;
    String publisher;
    double Price;
    Bitmap bookThumb;

    public Book(int bookId, String bookName, int bookEdition, String publisher, double price, Bitmap bookThumb) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.bookEdition = bookEdition;
        this.publisher = publisher;
        Price = price;
        this.bookThumb = bookThumb;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getBookEdition() {
        return bookEdition;
    }

    public void setBookEdition(int bookEdition) {
        this.bookEdition = bookEdition;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public Bitmap getBookThumb() {
        return bookThumb;
    }

    public void setBookThumb(Bitmap bookThumb) {
        this.bookThumb = bookThumb;
    }
}
