package com.example.securenotifyvault;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {

    private ArrayList<DatabaseHelper.NotificationItem> notificationsList;


    private final String[] DANGEROUS_WORDS = {
            "password", "pass", "code", "otp", "bank", "money", "urgent",
            "סיסמה", "קוד", "בנק", "אשראי", "כסף", "דחוף", "העברה", "סודי"
    };

    public NotificationsAdapter(ArrayList<DatabaseHelper.NotificationItem> list) {
        this.notificationsList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DatabaseHelper.NotificationItem item = notificationsList.get(position);

        boolean isCritical = false;
        String lowerCaseText = item.text.toLowerCase();

        for (String word : DANGEROUS_WORDS) {
            if (lowerCaseText.contains(word)) {
                isCritical = true;
                break;
            }
        }

        if (isCritical) {
            holder.tvTitle.setTextColor(Color.RED);
            holder.tvTitle.setText(item.title + " [SENSITIVE]");
        } else {
            holder.tvTitle.setTextColor(Color.GREEN);
            holder.tvTitle.setText(item.title);
        }

        holder.tvContent.setText(item.text);

        java.text.DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(holder.itemView.getContext());
        String dateString = dateFormat.format(new java.util.Date(Long.parseLong(item.time)));
        holder.tvApp.setText(item.app + " • " + dateString);
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent, tvApp;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvContent = itemView.findViewById(R.id.tvContent);
            tvApp = itemView.findViewById(R.id.tvApp);
        }
    }
}