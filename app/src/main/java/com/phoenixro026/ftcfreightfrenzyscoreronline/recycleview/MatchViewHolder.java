/*
 * Copyright (C) 2020 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.phoenixro026.ftcfreightfrenzyscoreronline.recycleview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.phoenixro026.ftcfreightfrenzyscoreronline.R;
import com.phoenixro026.ftcfreightfrenzyscoreronline.ScorerActivityView;

class MatchViewHolder extends RecyclerView.ViewHolder {
    private final TextView matchItemView;
    private final TextView dateItemView;
    private int mMatch;

    private MatchViewHolder(View itemView) {
        super(itemView);
        matchItemView = itemView.findViewById(R.id.list_team_name);
        dateItemView = itemView.findViewById(R.id.list_date);
        Context context = itemView.getContext();
        itemView.setOnClickListener(view -> {
            String value="edit";
            context.startActivity(new Intent(context, ScorerActivityView.class).putExtra("key", value).putExtra("id", mMatch));
        });
    }

    public void bind(String text, String date, int match) {
        matchItemView.setText(text);
        dateItemView.setText(date);
        mMatch = match;
    }

    static MatchViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new MatchViewHolder(view);
    }
}
