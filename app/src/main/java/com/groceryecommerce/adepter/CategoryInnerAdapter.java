package com.groceryecommerce.adepter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.groceryecommerce.retrofit.APIClient;
import com.groceryecommerce.R;
import com.groceryecommerce.model.CatItem;

import java.util.List;

public class CategoryInnerAdapter extends RecyclerView.Adapter<CategoryInnerAdapter.MyViewHolder> {

    private Context mContext;
    private List<CatItem> categoryList;
    private RecyclerTouchListener listener;

    public interface RecyclerTouchListener {
        public void onClickItem(String titel, int position);

        public void onLongClickItem(View v, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title,pronum;
        public ImageView thumbnail;
        public ImageView overflow;
        public LinearLayout ll_viewFull;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.txt_title);
            pronum = (TextView) view.findViewById(R.id.txt_pronum);
            thumbnail = view.findViewById(R.id.imageView);
            ll_viewFull = view.findViewById(R.id.ll_viewFull);
        }
    }

    public CategoryInnerAdapter(Context mContext, List<CatItem> categoryList, final RecyclerTouchListener listener) {
        this.mContext = mContext;
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_innercard_horizontal, parent, false);
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        CatItem category = categoryList.get(position);
        holder.title.setText(category.getCatname());
        holder.pronum.setText(category.getCount()+" Subcategories");

        String imgtest = (APIClient.baseUrl  + category.getCatimg());
        Log.d("test",imgtest);
        Glide.with(mContext).load(APIClient.baseUrl + category.getCatimg()).thumbnail(Glide.with(mContext).load(R.drawable.ezgifresize)).into(holder.thumbnail);

        holder.ll_viewFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onClickItem(category.getCatname(), Integer.parseInt(category.getId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }
}