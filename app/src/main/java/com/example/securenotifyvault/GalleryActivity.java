package com.example.securenotifyvault;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView rvGallery = findViewById(R.id.rvGallery);

        rvGallery.setLayoutManager(new GridLayoutManager(this, 2));

        List<File> files = getIntruderPhotos();

        GalleryAdapter adapter = new GalleryAdapter(files);
        rvGallery.setAdapter(adapter);
    }

    private List<File> getIntruderPhotos() {
        List<File> intruderFiles = new ArrayList<>();

        File directory = getFilesDir();
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith("intruder_") && file.getName().endsWith(".jpg")) {
                    intruderFiles.add(file);
                }
            }
        }
        Collections.sort(intruderFiles, (f1, f2) -> Long.compare(f2.lastModified(), f1.lastModified()));

        return intruderFiles;
    }
}