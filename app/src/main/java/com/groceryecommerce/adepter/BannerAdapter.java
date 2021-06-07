package com.groceryecommerce.adepter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.groceryecommerce.R;
import com.groceryecommerce.activity.HomeActivity;
import com.groceryecommerce.fragment.SubCategoryFragment;
import com.groceryecommerce.model.BannerItem;
import com.groceryecommerce.retrofit.APIClient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.groceryecommerce.activity.HomeActivity.homeActivity;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerHolder> {
    Context context;
    List<BannerItem> bannerDatumList;
    public BannerAdapter(Context context, List<BannerItem> bannerDatumList) {
        this.context = context;
        this.bannerDatumList = bannerDatumList;

    }

    @NonNull
    @Override
    public BannerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_banner, parent, false);
        return new BannerHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BannerHolder holder, int position)
    {

        Glide.with(context).load(APIClient.baseUrl + "/" + bannerDatumList.get(position).getBimg()).thumbnail(Glide.with(context).load(R.drawable.ezgifresize)).centerCrop().into(holder.imgBanner);
        holder.imgBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bannerDatumList.get(position).getCid().equalsIgnoreCase("0")) {
                    homeActivity.showMenu();
                    Bundle args = new Bundle();
                    args.putInt("id", Integer.parseInt(bannerDatumList.get(position).getCid()));
                    Fragment fragment = new SubCategoryFragment();
                    fragment.setArguments(args);
                    HomeActivity.getInstance().callFragment(fragment);
                }
            }
        });



    }
    @Override
    public int getItemCount() {
        return bannerDatumList.size();
    }

    public class BannerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView)
        ImageView imgBanner;

        public BannerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
