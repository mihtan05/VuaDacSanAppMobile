package com.example.dangnhap.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.baicuoiki.R;
import com.example.dangnhap.adapters.NhanVienAdapter;
import com.example.dangnhap.dao.NhanVienDAO;
import com.example.dangnhap.models.NhanVien;

import java.util.ArrayList;

public class NhanVienActivity extends AppCompatActivity {

    EditText edtMa, edtTen, edtVaiTro, edtUser, edtSearch;
    Button btnThem, btnSua, btnXoa;
    ListView listView;

    NhanVienDAO dao;
    ArrayList<NhanVien> list;
    NhanVienAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhanvien);

        initViews();
        dao = new NhanVienDAO(this);
        loadData();
        setupEvents();
    }

    private void initViews() {
        edtMa = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        edtVaiTro = findViewById(R.id.edtVaiTro);
        edtUser = findViewById(R.id.edtUserNV);
        edtSearch = findViewById(R.id.edtSearchNV);
        btnThem = findViewById(R.id.btnThemNV);
        btnSua = findViewById(R.id.btnSuaNV);
        btnXoa = findViewById(R.id.btnXoaNV);
        listView = findViewById(R.id.listNV);
    }

    private void setupEvents() {
        // Sự kiện thêm
        btnThem.setOnClickListener(v -> {
            NhanVien nv = getForm();
            if (nv == null) return;
            if (dao.insert(nv)) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                loadData();
                clearForm();
            } else {
                Toast.makeText(this, "Thêm thất bại (Mã có thể đã tồn tại)", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện sửa
        btnSua.setOnClickListener(v -> {
            NhanVien nv = getForm();
            if (nv == null) return;
            if (dao.update(nv)) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                loadData();
                clearForm();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        // Sự kiện xóa
        btnXoa.setOnClickListener(v -> {
            String ma = edtMa.getText().toString().trim();
            if (ma.isEmpty()) {
                Toast.makeText(this, "Vui lòng chọn nhân viên để xóa", Toast.LENGTH_SHORT).show();
                return;
            }
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận")
                    .setMessage("Bạn có chắc chắn muốn xóa nhân viên " + ma + "?")
                    .setPositiveButton("Xóa", (dialog, which) -> {
                        if (dao.delete(ma)) {
                            Toast.makeText(this, "Đã xóa", Toast.LENGTH_SHORT).show();
                            loadData();
                            clearForm();
                        }
                    })
                    .setNegativeButton("Hủy", null)
                    .show();
        });

        // Click vào item để đổ dữ liệu lên form
        listView.setOnItemClickListener((parent, view, position, id) -> {
            NhanVien nv = list.get(position);
            edtMa.setText(nv.ma);
            edtTen.setText(nv.ten);
            edtVaiTro.setText(nv.vaiTro);
            edtUser.setText(nv.username);
            edtMa.setEnabled(false); // Không cho sửa mã nhân viên
        });

        // Tìm kiếm thời gian thực
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                search(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private NhanVien getForm() {
        String ma = edtMa.getText().toString().trim();
        String ten = edtTen.getText().toString().trim();
        String vaiTro = edtVaiTro.getText().toString().trim();
        String user = edtUser.getText().toString().trim();

        if (ma.isEmpty() || ten.isEmpty()) {
            Toast.makeText(this, "Mã và tên không được để trống", Toast.LENGTH_SHORT).show();
            return null;
        }
        NhanVien nv = new NhanVien();
        nv.ma = ma;
        nv.ten = ten;
        nv.vaiTro = vaiTro;
        nv.username = user;
        return nv;
    }

    private void search(String query) {
        ArrayList<NhanVien> fullList = dao.getAll();
        list.clear();
        for (NhanVien nv : fullList) {
            if (nv.ten.toLowerCase().contains(query.toLowerCase()) || 
                nv.ma.toLowerCase().contains(query.toLowerCase())) {
                list.add(nv);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void clearForm() {
        edtMa.setText("");
        edtTen.setText("");
        edtVaiTro.setText("");
        edtUser.setText("");
        edtMa.setEnabled(true);
    }

    void loadData() {
        list = dao.getAll();
        if (list == null) list = new ArrayList<>();
        adapter = new NhanVienAdapter(this, list);
        listView.setAdapter(adapter);
    }
}
