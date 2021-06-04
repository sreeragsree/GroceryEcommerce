package com.groceryecommerce.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.groceryecommerce.model.Home;
import com.groceryecommerce.model.ProductItem;
import com.groceryecommerce.model.User;
import com.groceryecommerce.R;
import com.groceryecommerce.activity.HomeActivity;
import com.groceryecommerce.activity.ItemDetailsActivity;
import com.groceryecommerce.adepter.ReletedItemAllAdp;
import com.groceryecommerce.retrofit.GetResult;
import com.groceryecommerce.utils.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.groceryecommerce.utils.SessionManager.iscart;
import static com.groceryecommerce.utils.Utiles.productItems;

public class PopularFragment extends Fragment implements ReletedItemAllAdp.ItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView reyCategory;

    SessionManager sessionManager;
    User userData;
    ReletedItemAllAdp itemAdp;

    public PopularFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        ButterKnife.bind(this, view);

        sessionManager = new SessionManager(getActivity());
        userData = sessionManager.getUserDetails("");
        HomeActivity.getInstance().setFrameMargin(0);
        reyCategory.setHasFixedSize(true);

        reyCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        itemAdp = new ReletedItemAllAdp(getActivity(), productItems, this);
        reyCategory.setAdapter(itemAdp);

        return view;
    }
    @Override
    public void onItemClick(ProductItem productItem, int position) {
        getActivity().startActivity(new Intent(getActivity(), ItemDetailsActivity.class).putExtra("MyClass", productItem).putParcelableArrayListExtra("MyList", productItem.getPrice()));
    }
    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().titleChange();
        HomeActivity.getInstance().setFrameMargin(60);
        if(itemAdp!=null){
            itemAdp.notifyDataSetChanged();
        }
        if (iscart) {
            iscart = false;
            CardFragment fragment = new CardFragment();
            HomeActivity.getInstance().callFragment(fragment);
        }
    }

//    @Override
//    public void callback(JsonObject result, String callNo) {
//
//        Gson gson = new Gson();
//        Log.d("ResultHome",result.toString());
//        Home home = gson.fromJson(result.toString(), Home.class);
//
//        productItems.addAll(home.getResultHome().getProductItems());
//        reyCategory.setLayoutManager(new GridLayoutManager(getActivity(), 2));
//        itemAdp = new ReletedItemAllAdp(getActivity(), productItems, this);
//        reyCategory.setAdapter(itemAdp);
//
//    }
}
