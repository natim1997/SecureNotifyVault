package com.example.securenotifyvault;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.io.File;
import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private List<File> imageFiles;

    public GalleryAdapter(List<File> imageFiles) {
        this.imageFiles = imageFiles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_intruder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        File imgFile = imageFiles.get(position);

        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.ivPhoto.setImageBitmap(myBitmap);


            try {
                String name = imgFile.getName();
                String timeStr = name.replace("intruder_", "").replace(".jpg", "");
                long time = Long.parseLong(timeStr);

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateTimeInstance();              holder.tvDate.setText(dateFormat.format(new java.util.Date(time)));
            } catch (Exception e) {
                holder.tvDate.setText("Unknown Time");
            }
        }
    }

    @Override
    public int getItemCount() {
        return imageFiles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}