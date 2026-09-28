package com.example.qlkhuyenmai;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.SearchView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;

import java.util.ArrayList;
import java.util.Arrays;

public class KhuyenMaiMainActivity extends AppCompatActivity {

    RecyclerView rcv;
    KhuyenMaiDAO dao;
    ArrayList<KhuyenMai> list = new ArrayList<>(); // Khởi tạo list ngay từ đầu
    ArrayList<KhuyenMai> listFull = new ArrayList<>();
    KhuyenMaiAdapter adapter;

    Button btnAdd, btnDelete, btnEdit;
    SearchView searchView;

    int selectedIndex = -1;
    String[] loaiArr = {"Phần trăm", "Tiền mặt", "Freeship"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khuyenmai_main);

        rcv = findViewById(R.id.rcv_km);
        btnAdd = findViewById(R.id.btnAdd_km);
        btnDelete = findViewById(R.id.btnDelete_km);
        btnEdit = findViewById(R.id.btnEdit_km);
        searchView = findViewById(R.id.searchView_km);

        rcv.setLayoutManager(new LinearLayoutManager(this));
        dao = new KhuyenMaiDAO(this);

        // Khởi tạo adapter với list đã có
        adapter = new KhuyenMaiAdapter(this, list, position -> {
            selectedIndex = position;
            Toast.makeText(this, "Đã chọn: " + list.get(position).maKhuyenMai, Toast.LENGTH_SHORT).show();
        });
        rcv.setAdapter(adapter);

        loadData(); // Gọi loadData sau khi đã gán adapter

        btnAdd.setOnClickListener(v -> showDialogAdd());
        
        btnDelete.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                confirmDelete();
            } else {
                Toast.makeText(this, "Vui lòng chọn một mục để xóa", Toast.LENGTH_SHORT).show();
            }
        });

        btnEdit.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                showDialogEdit(list.get(selectedIndex));
            } else {
                Toast.makeText(this, "Vui lòng chọn một mục để sửa", Toast.LENGTH_SHORT).show();
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });
    }

    void loadData() {
        // Thay vì gán list = dao.getAll(), ta dùng clear() và addAll() 
        // để giữ nguyên tham chiếu mà Adapter đang giữ
        ArrayList<KhuyenMai> newData = dao.getAll();
        list.clear();
        list.addAll(newData);
        
        listFull.clear();
        listFull.addAll(list);
        
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    void search(String text) {
        ArrayList<KhuyenMai> result = new ArrayList<>();
        for (KhuyenMai km : listFull) {
            if (km.maKhuyenMai.toLowerCase().contains(text.toLowerCase())
                    || km.loaiMa.toLowerCase().contains(text.toLowerCase())) {
                result.add(km);
            }
        }
        list.clear();
        list.addAll(result);
        adapter.notifyDataSetChanged();
    }

    void confirmDelete() {
        KhuyenMai km = list.get(selectedIndex);
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa mã " + km.maKhuyenMai + "?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    dao.delete(km.maKhuyenMai);
                    loadData();
                    selectedIndex = -1;
                    Toast.makeText(this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_km, null);
        builder.setView(view);

        EditText edMa = view.findViewById(R.id.edMa);
        Spinner spLoai = view.findViewById(R.id.spLoai);
        EditText edGia = view.findViewById(R.id.edGia);
        EditText edDon = view.findViewById(R.id.edDon);
        EditText edNgay = view.findViewById(R.id.edNgay);

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loaiArr);
        spLoai.setAdapter(ad);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String ma = edMa.getText().toString().trim();
            String giaStr = edGia.getText().toString().trim();
            String donStr = edDon.getText().toString().trim();
            String ngay = edNgay.getText().toString().trim();

            if (ma.isEmpty() || giaStr.isEmpty() || donStr.isEmpty() || ngay.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                KhuyenMai km = new KhuyenMai(
                        ma,
                        spLoai.getSelectedItem().toString(),
                        Double.parseDouble(giaStr),
                        Double.parseDouble(donStr),
                        ngay
                );
                dao.insert(km);
                loadData(); // Cập nhật lại list và notify adapter
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá trị giảm và đơn tối thiểu phải là số", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    void showDialogEdit(KhuyenMai km) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_km, null);
        builder.setView(view);

        EditText edMa = view.findViewById(R.id.edMa);
        Spinner spLoai = view.findViewById(R.id.spLoai);
        EditText edGia = view.findViewById(R.id.edGia);
        EditText edDon = view.findViewById(R.id.edDon);
        EditText edNgay = view.findViewById(R.id.edNgay);

        ArrayAdapter<String> ad = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loaiArr);
        spLoai.setAdapter(ad);

        edMa.setText(km.maKhuyenMai);
        edMa.setEnabled(false);
        edGia.setText(String.valueOf(km.giaTriGiam));
        edDon.setText(String.valueOf(km.donToiThieu));
        edNgay.setText(km.ngayKetThuc);
        
        int pos = Arrays.asList(loaiArr).indexOf(km.loaiMa);
        if (pos >= 0) spLoai.setSelection(pos);

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận thay đổi")
                    .setMessage("Bạn có chắc chắn muốn cập nhật thông tin cho mã " + km.maKhuyenMai + "?")
                    .setPositiveButton("Đồng ý", (dialogConfirm, whichConfirm) -> {
                        try {
                            km.loaiMa = spLoai.getSelectedItem().toString();
                            km.giaTriGiam = Double.parseDouble(edGia.getText().toString());
                            km.donToiThieu = Double.parseDouble(edDon.getText().toString());
                            km.ngayKetThuc = edNgay.getText().toString();
                            
                            dao.update(km);
                            loadData();
                            selectedIndex = -1;
                            Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } catch (NumberFormatException e) {
                            Toast.makeText(this, "Lỗi định dạng số", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });
        builder.setNegativeButton("Quay lại", null);
        builder.show();
    }
}
