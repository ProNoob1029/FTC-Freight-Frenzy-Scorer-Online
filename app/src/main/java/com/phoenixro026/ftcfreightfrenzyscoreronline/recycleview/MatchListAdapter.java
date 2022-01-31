package com.phoenixro026.ftcfreightfrenzyscoreronline.recycleview;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.phoenixro026.ftcfreightfrenzyscoreronline.database.Match;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MatchListAdapter extends ListAdapter<Match, MatchViewHolder> {

    public MatchListAdapter(@NonNull DiffUtil.ItemCallback<Match> diffCallback) {
        super(diffCallback);

    }

    @Override
    public void submitList(final List<Match> list) {
        super.submitList(list != null ? new ArrayList<>(list) : null);
    }

    @NonNull
    @Override
    public MatchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return MatchViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(MatchViewHolder holder, int position) {
        Match current = getItem(position);
        holder.bind(String.format(Locale.US, "%d. %s", position + 1, current.teamName), current.createTime, current.id, current.points);
    }

    public static class MatchDiff extends DiffUtil.ItemCallback<Match> {

        @Override
        public boolean areItemsTheSame(@NonNull Match oldItem, @NonNull Match newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Match oldItem, @NonNull Match newItem) {
            return oldItem.teamName.equals(newItem.teamName);
        }
    }
}
