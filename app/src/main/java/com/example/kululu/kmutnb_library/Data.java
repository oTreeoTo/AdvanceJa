package com.example.kululu.kmutnb_library;

/**
 * Created by Kululu on 8/21/2017.
 */

    public class Data {
        private int id;
        private String book_name;  //สำหรับ Title
        private String book_desc;  //สำหรับ Description
        private String book_written;      //สำหรับ Image
        private String book_type;


    public Data(int id, String book_name, String book_desc, String book_written, String book_type) {
        this.id = id;
        this.book_name = book_name;
        this.book_desc = book_desc;
        this.book_written = book_written;
        this.book_type = book_type;
    }

    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

    public String getBook_written() {
        return book_written;
    }

    public void setBook_written(String book_written) {
        this.book_written = book_written;
    }

    public String getBook_desc() {
        return book_desc;
    }

    public void setBook_desc(String book_desc) {
        this.book_desc = book_desc;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
