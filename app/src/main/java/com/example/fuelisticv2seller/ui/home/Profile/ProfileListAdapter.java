package com.example.fuelisticv2seller.ui.home.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelisticv2seller.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    private List<ItemAdapter> mList;
    private Context mContext;
    public ProfileListAdapter(List<ItemAdapter> list, Context context)
    {
        super();
        this.mList = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_listitem,
                parent, false);
        ViewHolder viewH = new ViewHolder(v);

        return viewH;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        ItemAdapter itemAdapter = mList.get(position);
        ((ViewHolder) holder).desc.setHint(itemAdapter.getInputDesc());
        ((ViewHolder) holder).val.setText(itemAdapter.getInputValue());
        ((ViewHolder) holder).img.setImageResource(itemAdapter.getImg());
    }

    @Override
    public int getItemCount()
    {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextInputLayout desc;
        public TextInputEditText val;
        public ImageView img;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            desc = (TextInputLayout)itemView.findViewById(R.id.inputDesc);
            val = (TextInputEditText)itemView.findViewById(R.id.inputValue);
            img = (ImageView)itemView.findViewById(R.id.img_item);
            val.setEnabled(false);
        }
    }
}
