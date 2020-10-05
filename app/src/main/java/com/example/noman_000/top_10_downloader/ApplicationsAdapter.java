package com.example.noman_000.top_10_downloader;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;



public class ApplicationsAdapter extends ArrayAdapter {
    private ArrayList<FeedEntry> applications;
    private final LayoutInflater layoutInflater;
    private final int resource;

    public ApplicationsAdapter(@NonNull Context context, int resource, @NonNull List<FeedEntry> applications) {
        super(context, resource, applications);
        this.layoutInflater = LayoutInflater.from(context);
        this.resource = resource;
        this.applications = (ArrayList<FeedEntry>) applications;
    }

    @Override
    public int getCount() {
        return applications.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder = null;
        if(row == null){
            row = layoutInflater.inflate(resource, parent, false);
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) row.getTag();
        }
        FeedEntry entry = applications.get(position);
        viewHolder.tvNaMe.setText(entry.getNaMe());
        viewHolder.tvArtist.setText(entry.getArtist());
        viewHolder.tvSuMMary.setText(entry.getSuMMary());
        return row;
    }
    private class ViewHolder{
        private TextView tvArtist;
        private TextView tvSuMMary;
        private TextView tvNaMe;
        ViewHolder(View v){
            tvArtist = (TextView) v.findViewById(R.id.tvArtist);
            tvNaMe = (TextView) v.findViewById(R.id.tvNaMe);
            tvSuMMary = (TextView) v.findViewById(R.id.tvSuMMary);
        }
    }
}
