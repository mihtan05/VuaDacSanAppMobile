package com.example.baicuoiki.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.baicuoiki.R;
import com.example.baicuoiki.model.Product;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import database.DatabaseHelper;

public class ProductActivity extends AppCompatActivity {

    private ListView lvProducts;
    private EditText edtSearch;
    private Button btnSearch, btnAddNew;
    private ImageView btnBack;
    
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ArrayList<Product> productList;
    private ProductAdapter adapter;

    private String currentSelectedImageUri = "";
    private ImageView imgPreviewInDialog;
    private EditText edtImageInDialog;

    private static final int PICK_IMAGE_REQUEST = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);

        btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        dbHelper = new DatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        lvProducts = findViewById(R.id.lvProducts);
        edtSearch = findViewById(R.id.edtSearch);
        btnSearch = findViewById(R.id.btnSearch);
        btnAddNew = findViewById(R.id.btnAddNew);

        productList = new ArrayList<>();
        loadDataFromDatabase("");

        btnSearch.setOnClickListener(v -> {
            String keyword = edtSearch.getText().toString().trim();
            loadDataFromDatabase(keyword);
        });

        btnAddNew.setOnClickListener(v -> showProductDialog(null));

        lvProducts.setOnItemClickListener((parent, view, position, id) -> {
            Product selected = productList.get(position);
            showProductDialog(selected);
        });

        lvProducts.setOnItemLongClickListener((parent, view, position, id) -> {
            confirmDelete(productList.get(position));
            return true;
        });
    }

    private void loadDataFromDatabase(String keyword) {
        productList.clear();
        Cursor cursor = null;
        try {
            String sql = "SELECT sp.maSanpham, sp.tenSanpham, sp.hinhAnh, sp.motaSanpham, " +
                    "sp.donViTinh, sp.giaDon, sp.soLuongTon, sp.hanSuDung, sp.maNCC, " +
                    "ncc.tenNCC AS tenNhaCungCap " +
                    "FROM SAN_PHAM sp LEFT JOIN NHA_CUNG_CAP ncc ON sp.maNCC = ncc.maNCC";

            if (keyword.isEmpty()) {
                cursor = db.rawQuery(sql, null);
            } else {
                sql += " WHERE sp.tenSanpham LIKE ? OR sp.maSanpham LIKE ?";
                cursor = db.rawQuery(sql, new String[]{"%" + keyword + "%", "%" + keyword + "%"});
            }

            if (cursor != null) {
                int idxId = cursor.getColumnIndexOrThrow("maSanpham");
                int idxName = cursor.getColumnIndexOrThrow("tenSanpham");
                int idxImage = cursor.getColumnIndexOrThrow("hinhAnh");
                int idxDesc = cursor.getColumnIndexOrThrow("motaSanpham");
                int idxUnit = cursor.getColumnIndexOrThrow("donViTinh");
                int idxPrice = cursor.getColumnIndexOrThrow("giaDon");
                int idxStock = cursor.getColumnIndexOrThrow("soLuongTon");
                int idxExpiry = cursor.getColumnIndexOrThrow("hanSuDung");
                int idxSuppId = cursor.getColumnIndexOrThrow("maNCC");
                int idxSuppName = cursor.getColumnIndexOrThrow("tenNhaCungCap");

                while (cursor.moveToNext()) {
                    Product p = new Product();
                    p.setId(cursor.getString(idxId));
                    p.setName(cursor.getString(idxName));
                    p.setImage(cursor.getString(idxImage));
                    p.setDescription(cursor.getString(idxDesc));
                    p.setUnit(cursor.getString(idxUnit));
                    p.setPrice(cursor.getDouble(idxPrice));
                    p.setStock(cursor.getInt(idxStock));
                    p.setExpiryDate(cursor.getString(idxExpiry));
                    p.setSupplierId(cursor.getString(idxSuppId));
                    p.setSupplierName(cursor.getString(idxSuppName));
                    productList.add(p);
                }
                cursor.close();
            }

            if (adapter == null) {
                adapter = new ProductAdapter(this, productList);
                lvProducts.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void showProductDialog(Product product) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_add_edit_product);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        EditText edtId = dialog.findViewById(R.id.edtId);
        EditText edtName = dialog.findViewById(R.id.edtName);
        edtImageInDialog = dialog.findViewById(R.id.edtImage);
        EditText edtDesc = dialog.findViewById(R.id.edtDescription);
        EditText edtUnit = dialog.findViewById(R.id.edtUnit);
        EditText edtPrice = dialog.findViewById(R.id.edtPrice);
        EditText edtStock = dialog.findViewById(R.id.edtStock);
        EditText edtExpiry = dialog.findViewById(R.id.edtExpiry);
        imgPreviewInDialog = dialog.findViewById(R.id.imgPreview);
        Button btnChooseImage = dialog.findViewById(R.id.btnChooseImage);
        Button btnSave = dialog.findViewById(R.id.btnSave);
        android.widget.Spinner spnSupplier = dialog.findViewById(R.id.spnSupplier);

        // --- CÀI ĐẶT DATE PICKER CHO HẠN SỬ DỤNG ---
        edtExpiry.setFocusable(false);
        edtExpiry.setClickable(true);
        edtExpiry.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(ProductActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        calendar.set(selectedYear, selectedMonth, selectedDay);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                        edtExpiry.setText(sdf.format(calendar.getTime()));
                    }, year, month, day);
            datePickerDialog.show();
        });

        ArrayList<String> supplierNames = new ArrayList<>();
        ArrayList<String> supplierIds = new ArrayList<>();
        try {
            Cursor cursorNcc = db.rawQuery("SELECT maNCC, tenNCC FROM NHA_CUNG_CAP", null);
            while (cursorNcc.moveToNext()) {
                supplierIds.add(cursorNcc.getString(0));
                supplierNames.add(cursorNcc.getString(1));
            }
            cursorNcc.close();
        } catch (Exception ignored) {}

        spnSupplier.setAdapter(new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, supplierNames));

        boolean isEdit = (product != null);
        currentSelectedImageUri = isEdit ? product.getImage() : "";

        if (isEdit) {
            tvTitle.setText("SỬA SẢN PHẨM");
            edtId.setText(product.getId());
            edtId.setEnabled(false);
            edtName.setText(product.getName());
            edtImageInDialog.setText(product.getImage());
            edtDesc.setText(product.getDescription());
            edtUnit.setText(product.getUnit());
            edtPrice.setText(String.valueOf(product.getPrice()));
            edtStock.setText(String.valueOf(product.getStock()));
            edtExpiry.setText(product.getExpiryDate());

            if (currentSelectedImageUri != null && !currentSelectedImageUri.isEmpty()) {
                Glide.with(this).load(currentSelectedImageUri).into(imgPreviewInDialog);
            }
            int position = supplierIds.indexOf(product.getSupplierId());
            if (position >= 0) spnSupplier.setSelection(position);
        }

        btnChooseImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnSave.setOnClickListener(v -> {
            String id = edtId.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            if (id.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập Mã và Tên!", Toast.LENGTH_SHORT).show();
                return;
            }

            ContentValues values = new ContentValues();
            values.put("maSanpham", id);
            values.put("tenSanpham", name);
            values.put("hinhAnh", currentSelectedImageUri); 
            values.put("motaSanpham", edtDesc.getText().toString().trim());
            values.put("donViTinh", edtUnit.getText().toString().trim());
            try {
                values.put("giaDon", Double.parseDouble(edtPrice.getText().toString()));
                values.put("soLuongTon", Integer.parseInt(edtStock.getText().toString()));
            } catch (Exception e) {
                values.put("giaDon", 0);
                values.put("soLuongTon", 0);
            }
            values.put("hanSuDung", edtExpiry.getText().toString().trim());

            int selPos = spnSupplier.getSelectedItemPosition();
            if (selPos >= 0) values.put("maNCC", supplierIds.get(selPos));

            if (isEdit) {
                db.update("SAN_PHAM", values, "maSanpham=?", new String[]{id});
            } else {
                db.insert("SAN_PHAM", null, values);
            }
            dialog.dismiss();
            loadDataFromDatabase("");
        });

        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                String savedPath = saveImageToInternalStorage(imageUri);
                if (savedPath != null) {
                    currentSelectedImageUri = savedPath;
                    if (imgPreviewInDialog != null) {
                        Glide.with(this).load(savedPath).into(imgPreviewInDialog);
                    }
                    if (edtImageInDialog != null) {
                        edtImageInDialog.setText(savedPath);
                    }
                }
            }
        }
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            String fileName = "prod_" + System.currentTimeMillis() + ".jpg";
            InputStream is = getContentResolver().openInputStream(uri);
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            byte[] buffer = new byte[1024];
            int read;
            while ((read = is.read(buffer)) != -1) fos.write(buffer, 0, read);
            is.close();
            fos.close();
            return getFilesDir() + "/" + fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void confirmDelete(Product p) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa sản phẩm")
                .setMessage("Bạn chắc chắn muốn xóa " + p.getName() + "?")
                .setPositiveButton("Xóa", (d, w) -> {
                    db.delete("SAN_PHAM", "maSanpham=?", new String[]{p.getId()});
                    loadDataFromDatabase("");
                })
                .setNegativeButton("Hủy", null).show();
    }

    private class ProductAdapter extends BaseAdapter {
        private Context context;
        private ArrayList<Product> list;
        private DecimalFormat formatter = new DecimalFormat("###,###,###");

        public ProductAdapter(Context context, ArrayList<Product> list) {
            this.context = context;
            this.list = list;
        }
        @Override public int getCount() { return list.size(); }
        @Override public Object getItem(int pos) { return list.get(pos); }
        @Override public long getItemId(int pos) { return pos; }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) convertView = LayoutInflater.from(context).inflate(R.layout.item_product_list, parent, false);
            Product p = list.get(position);
            
            ImageView img = convertView.findViewById(R.id.imgProduct);
            TextView txtName = convertView.findViewById(R.id.txtName);
            TextView txtPrice = convertView.findViewById(R.id.txtPrice);
            ImageView btnEdit = convertView.findViewById(R.id.btnEdit);

            txtName.setText(p.getName());
            txtPrice.setText(formatter.format(p.getPrice()) + "đ");

            Glide.with(context)
                    .load(p.getImage())
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .error(android.R.drawable.ic_menu_report_image)
                    .into(img);

            btnEdit.setOnClickListener(v -> showProductDialog(p));
            return convertView;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null && db.isOpen()) db.close();
    }
}
