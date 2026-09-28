package com.example.qlkhuyenmai.cskh;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SearchView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.baicuoiki.R;

import java.util.ArrayList;

public class CSKHActivity extends AppCompatActivity {

    RecyclerView rcv;
    YeuCauDAO dao;
    ArrayList<YeuCauHoTro> list;
    YeuCauAdapter adapter;

    Button btnAdd, btnDelete, btnEdit;

    int selectedIndex = -1;
    Spinner spTrangThai;
    SearchView searchView;

    String[] arr = {"Tất cả", "Chờ xử lý", "Đã giải quyết"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cskh);

        // Ánh xạ
        rcv = findViewById(R.id.rcv);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnEdit = findViewById(R.id.btnEdit);
        spTrangThai = findViewById(R.id.spTrangThai);
        searchView = findViewById(R.id.searchView);

        rcv.setLayoutManager(new LinearLayoutManager(this));

        dao = new YeuCauDAO(this);

        // Spinner trạng thái
        ArrayAdapter<String> ad = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                arr
        );
        spTrangThai.setAdapter(ad);

        // Load dữ liệu
        loadData();

        // Filter theo trạng thái
        spTrangThai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String key = arr[position];

                ArrayList<YeuCauHoTro> all = dao.getAll();
                ArrayList<YeuCauHoTro> filtered = new ArrayList<>();

                for (YeuCauHoTro yc : all) {
                    if (key.equals("Tất cả") || yc.trangThai.equals(key)) {
                        filtered.add(yc);
                    }
                }

                setAdapter(filtered);
                selectedIndex = -1; // reset chọn
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // ADD
        btnAdd.setOnClickListener(v -> showDialogAdd());

        // DELETE
        btnDelete.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                dao.delete(list.get(selectedIndex).maYeuCau);
                loadData();
            }
        });

        // EDIT
        btnEdit.setOnClickListener(v -> {
            if (selectedIndex != -1) {
                showDialogEdit(list.get(selectedIndex));
            }
        });
    }

    // ================= LOAD DATA =================
    void loadData() {
        list = dao.getAll();
        setAdapter(list);
        selectedIndex = -1;
    }

    // ================= SET ADAPTER =================
    void setAdapter(ArrayList<YeuCauHoTro> data) {
        adapter = new YeuCauAdapter(this, data, position -> {
            selectedIndex = position; // chỉ chọn
        });

        rcv.setAdapter(adapter);
    }

    // ================= ADD =================
    void showDialogAdd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_yc, null);
        builder.setView(v);

        // Spinner loại
        Spinner spLoai = v.findViewById(R.id.spLoai);
        String[] loaiArr = {"Khiếu nại", "Hỗ trợ", "Đổi trả", "Bảo hành"};

        ArrayAdapter<String> adapterLoai = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                loaiArr
        );
        spLoai.setAdapter(adapterLoai);

        EditText edMa = v.findViewById(R.id.edMa);
        EditText edKH = v.findViewById(R.id.edKH);
        EditText edND = v.findViewById(R.id.edNoiDung);
        EditText edPhanHoi = v.findViewById(R.id.edPhanHoi);

        // Ẩn phản hồi khi thêm
        edPhanHoi.setVisibility(View.GONE);

        builder.setPositiveButton("Thêm", (dialog, which) -> {

            if (edMa.getText().toString().isEmpty()) return;

            YeuCauHoTro yc = new YeuCauHoTro();
            yc.maYeuCau = edMa.getText().toString();
            yc.loaiYeuCau = spLoai.getSelectedItem().toString();
            yc.maKhachHang = edKH.getText().toString();
            yc.noiDungKH = edND.getText().toString();
            yc.trangThai = "Chờ xử lý";

            dao.insert(yc);
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    // ================= EDIT =================
    void showDialogEdit(YeuCauHoTro yc) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.dialog_yc, null);
        builder.setView(v);

        // Spinner loại
        Spinner spLoai = v.findViewById(R.id.spLoai);
        String[] loaiArr = {"Khiếu nại", "Hỗ trợ", "Đổi trả", "Bảo hành"};

        ArrayAdapter<String> adapterLoai = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                loaiArr
        );
        spLoai.setAdapter(adapterLoai);

        // Set giá trị cũ
        for (int i = 0; i < loaiArr.length; i++) {
            if (loaiArr[i].equals(yc.loaiYeuCau)) {
                spLoai.setSelection(i);
                break;
            }
        }

        EditText edMa = v.findViewById(R.id.edMa);
        EditText edKH = v.findViewById(R.id.edKH);
        EditText edND = v.findViewById(R.id.edNoiDung);
        EditText edPhanHoi = v.findViewById(R.id.edPhanHoi);

        edMa.setText(yc.maYeuCau);
        edKH.setText(yc.maKhachHang);
        edND.setText(yc.noiDungKH);

        edMa.setEnabled(false);

        // Nếu đã xử lý
        if (yc.trangThai.equals("Đã giải quyết")) {
            edPhanHoi.setText(yc.noiDungPhanHoi);
            edPhanHoi.setEnabled(false);
        }

        builder.setPositiveButton("Cập nhật", (dialog, which) -> {

            yc.loaiYeuCau = spLoai.getSelectedItem().toString();
            yc.maKhachHang = edKH.getText().toString();
            yc.noiDungKH = edND.getText().toString();

            String phanHoi = edPhanHoi.getText().toString();

            if (!phanHoi.isEmpty()) {
                yc.noiDungPhanHoi = phanHoi;
                yc.trangThai = "Đã giải quyết";
                yc.maNVCSKH = "NV01";
            }

            dao.update(yc);
            loadData();
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }
}
