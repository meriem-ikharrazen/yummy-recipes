package com.example.yummyrecipes.Service;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.yummyrecipes.R;

import java.util.List;

public class InstructionListAdapter extends ArrayAdapter<String> {
    List<String> instructions;
    Context context;

    public InstructionListAdapter(Context context, List<String> instructions) {
        super(context, 0, instructions);
        this.context = context;
        this.instructions = instructions;
    }

    @Override
    public int getCount() {
        return instructions.size();
    }

    @Override
    public String getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.instruction_item, parent, false);
            TextView text = (TextView) convertView.findViewById(R.id.instruction);

            text.setText(instructions.get(position));

        return convertView;
    }

}