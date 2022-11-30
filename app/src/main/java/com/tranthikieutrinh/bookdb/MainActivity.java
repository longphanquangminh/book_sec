package com.tranthikieutrinh.bookdb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.tranthikieutrinh.adapters.BookAdapter;
import com.tranthikieutrinh.bookdb.databinding.ActivityMainBinding;
import com.tranthikieutrinh.models.Book;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DataBaseHelper db;
    ArrayList<Book> books;
    BookAdapter adapter;
    Book selectedBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        createDB();
        loadData();
        addEvent();
    }

    private void addEvent() {
        binding.lvBooks.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedBook = (Book) adapter.getItem(i);
                return false;
            }
        });
    }


    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
        return outputStream.toByteArray();
    }
    private Bitmap convertByteArrayToBitmap(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
    private void createDB() {
        db = new DataBaseHelper(MainActivity.this);
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.kafka_on_the_shore);
//        byte[] bytes = convertBitmapToByteArray(bitmap);
        db.insertRow(1,"Kafka on the shore",1,"'Vintage'",150750,convertBitmapToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.kafka_on_the_shore)));

        db.insertRow(2,"The Little Prince: And Letter to a Hostage",1,"Penguin Books Ltd",183000,convertBitmapToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.the_little_prince)));

        db.insertRow(3,"The Travelling Cat Chronicles",1,"Transworld s Ltd",194000,convertBitmapToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.the_travelling_cat_chronicles)));
        db.insertRow(4,"The Help",1,"Oxford",194000,convertBitmapToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.the_help)));
        db.insertRow(5,"The Catcher",1,"Transworld s Ltd",194000,convertBitmapToByteArray(BitmapFactory.decodeResource(getResources(), R.drawable.the_catcher_in_the_rye)));


    }
    private void loadData() {
        books = new ArrayList<>();

        //Load data from database
        Cursor c = db.getData("SELECT * FROM "+ DataBaseHelper.TBL_NAME );

        while (c.moveToNext()){
            books.add(new Book(c.getInt(0),c.getString(1),c.getInt(2),c.getString(3),c.getDouble(4),convertByteArrayToBitmap(c.getBlob(5))));
        }

        c.close();
        adapter = new BookAdapter(MainActivity.this,R.layout.gv_book_item,books);
        binding.lvBooks.setAdapter(adapter);
        registerForContextMenu(binding.lvBooks);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.mn_Delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Xác nhận xóa!");
            builder.setMessage("Xác nhận xóa: "+selectedBook.getBookName() + " ?");
            builder.setIcon(android.R.drawable.ic_delete);
            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    db.deleteRow(selectedBook.getBookId());
                    loadData();
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.create().show();
        }
        if(item.getItemId()==R.id.mn_Edit){
            Dialog dialog = new Dialog(MainActivity.this);

            dialog.setContentView(R.layout.item_info);

            EditText edtName = dialog.findViewById(R.id.edt_Name);
            EditText edtPublisher = dialog.findViewById(R.id.edt_Publisher);
            EditText edtPrice = dialog.findViewById(R.id.edt_Price);
            EditText edtEdition = dialog.findViewById(R.id.edt_Edition);
            ImageView imvThumbEdit = dialog.findViewById(R.id.imv_ThumbEdit);
            Button btnSave = dialog.findViewById(R.id.btn_Save);
            Button btnCancel = dialog.findViewById(R.id.btn_Cancel);
            //Load item info to dialog

            edtName.setText(selectedBook.getBookName());
            edtPublisher.setText(selectedBook.getPublisher());
            edtPrice.setText(selectedBook.getPrice()+"");
            edtEdition.setText(selectedBook.getBookEdition()+"");
            imvThumbEdit.setImageBitmap(selectedBook.getBookThumb());

            //Click
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BitmapDrawable drawable = (BitmapDrawable) imvThumbEdit.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    db.insertRow(selectedBook.getBookId(),edtName.getText().toString(),Integer.parseInt(edtEdition.getText().toString()),edtPublisher.getText().toString(),Double.parseDouble(edtPrice.getText().toString()),convertBitmapToByteArray(bitmap));
                    dialog.dismiss();
                    loadData();
                }

            });
            dialog.show();


        }



        return super.onContextItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu,menu);

        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mn_Add) {
            Dialog dialog = new Dialog(MainActivity.this);

            dialog.setContentView(R.layout.item_info);

            EditText edtName = dialog.findViewById(R.id.edt_Name);
            EditText edtPublisher = dialog.findViewById(R.id.edt_Publisher);
            EditText edtPrice = dialog.findViewById(R.id.edt_Price);
            EditText edtEdition = dialog.findViewById(R.id.edt_Edition);
            ImageView imvThumbEdit = dialog.findViewById(R.id.imv_ThumbEdit);
            Button btnSave = dialog.findViewById(R.id.btn_Save);
            Button btnCancel = dialog.findViewById(R.id.btn_Cancel);
            //Load item info to dialog

            edtName.setText("");
            edtPublisher.setText("");
            edtPrice.setText("");
            edtEdition.setText("");
            imvThumbEdit.setImageResource(R.drawable.kafka_on_the_shore);

            //Click
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BitmapDrawable drawable = (BitmapDrawable) imvThumbEdit.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    db.insertRow(books.size() + 1, edtName.getText().toString(), Integer.parseInt(edtEdition.getText().toString()), edtPublisher.getText().toString(), Double.parseDouble(edtPrice.getText().toString()), convertBitmapToByteArray(bitmap));
                    dialog.dismiss();
                    loadData();
                }

            });
            dialog.show();

        }
        return super.onOptionsItemSelected(item);

    }
}