package com.groceryecommerce.fragment;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.groceryecommerce.adepter.BannerAdapter;
import com.groceryecommerce.model.BannerItem;
import com.groceryecommerce.model.DynamicData;
import com.groceryecommerce.model.Home;
import com.groceryecommerce.model.ProductItem;
import com.groceryecommerce.model.User;
import com.groceryecommerce.retrofit.APIClient;
import com.groceryecommerce.retrofit.GetResult;
import com.groceryecommerce.R;
import com.groceryecommerce.activity.HomeActivity;
import com.groceryecommerce.activity.ItemDetailsActivity;
import com.groceryecommerce.adepter.CategoryAdp;
import com.groceryecommerce.adepter.ReletedItemAdp;
import com.groceryecommerce.adepter.ReletedItemDaynamicAdp;
import com.groceryecommerce.model.CatItem;
import com.groceryecommerce.utils.AutoScrollViewPager;
import com.groceryecommerce.utils.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.groceryecommerce.activity.HomeActivity.homeActivity;
import static com.groceryecommerce.activity.HomeActivity.txtNoti;
import static com.groceryecommerce.utils.SessionManager.aboutUs;
import static com.groceryecommerce.utils.SessionManager.contactUs;
import static com.groceryecommerce.utils.SessionManager.currncy;
import static com.groceryecommerce.utils.SessionManager.iscart;
import static com.groceryecommerce.utils.SessionManager.oMin;
import static com.groceryecommerce.utils.SessionManager.privacy;
import static com.groceryecommerce.utils.SessionManager.razKey;
import static com.groceryecommerce.utils.SessionManager.tax;
import static com.groceryecommerce.utils.SessionManager.tremcodition;
import static com.groceryecommerce.utils.Utiles.productItems;


public class HomeFragment extends Fragment implements CategoryAdp.RecyclerTouchListener, ReletedItemAdp.ItemClickListener, GetResult.MyListener, ReletedItemDaynamicAdp.ItemClickListener {
    @BindView(R.id.viewPager)
    RecyclerView viewPager;
    @BindView(R.id.tabview)
    TabLayout tabview;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.recycler_releted)
    RecyclerView recyclerReleted;
    @BindView(R.id.lvl_selected)
    LinearLayout lvlSelected;

    @BindView(R.id.iv_promo)
    ImageView ivpromo;

    Unbinder unbinder;
    private Context mContext;
    CategoryAdp adapter;
    ReletedItemAdp adapterReletedi;
    List<CatItem> categoryList;
    BannerAdapter myCustomPagerAdapter;
    List<BannerItem> bannerDatumList;
    public  HomeFragment homeListFragment;
    SessionManager sessionManager;
    User user;
    int position;
    Timer timer;
    TimerTask timerTask;
    LinearLayoutManager layoutManager;
    List<DynamicData> dynamicDataList = new ArrayList<>();
    ReletedItemAdp reletedItemAdp;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        unbinder = ButterKnife.bind(this, view);
        bannerDatumList = new ArrayList<>();
        sessionManager = new SessionManager(mContext);
        homeListFragment = this;

        Glide.with(getContext()).load(R.drawable.promo_ban).into(ivpromo);

        // to set category linear horizontal
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        viewPager.setLayoutManager(layoutManager);
        setbanner();

        LinearLayoutManager mLayoutManager1 = new LinearLayoutManager(mContext);
        mLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerReleted.setLayoutManager(mLayoutManager1);

        // change related adapter rcv to grid
        //recyclerReleted.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        categoryList = new ArrayList<>();
        adapter = new CategoryAdp(mContext, categoryList, this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapterReletedi = new ReletedItemAdp(mContext, productItems, this);
        recyclerReleted.setItemAnimator(new DefaultItemAnimator());
        recyclerReleted.setAdapter(adapterReletedi);
        user = sessionManager.getUserDetails("");

        getHome();
        return view;
    }

    private void stopAutoScrollBanner() {
        if (timer != null && timerTask != null) {
            timerTask.cancel();
            timer.cancel();
            timer = null;
            timerTask = null;
            position = layoutManager.findFirstCompletelyVisibleItemPosition();
        }
    }

    private void runAutoScrollBanner() {
        if (timer == null && timerTask == null) {
            timer = new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (position == viewPager.getAdapter().getItemCount() - 1) {
                            position = 0;
                            viewPager.smoothScrollBy(5, 0);
                            viewPager.smoothScrollToPosition(position);
                        } else {
                            position++;
                            viewPager.smoothScrollToPosition(position);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

            };
            timer.schedule(timerTask, 4000, 4000);
        }

    }


    private void setbanner() {
        position = 0;
        viewPager.scrollToPosition(position);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(viewPager);
        viewPager.smoothScrollBy(5, 0);

        viewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == 1) {
                    stopAutoScrollBanner();
                } else if (newState == 0) {
                    position = layoutManager.findFirstCompletelyVisibleItemPosition();
                    runAutoScrollBanner();
                }
            }
        });
    }

    private void setJoinPlayrList(LinearLayout lnrView, List<DynamicData> dataList) {

        lnrView.removeAllViews();
        for (int i = 0; i < dataList.size(); i++) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_home_item, null);
            TextView itemTitle = view.findViewById(R.id.itemTitle);
            RecyclerView recycler_view_list = view.findViewById(R.id.recycler_view_list);
            itemTitle.setText(dataList.get(i).getTitle());
            ReletedItemDaynamicAdp itemAdp = new ReletedItemDaynamicAdp(mContext, dataList.get(i).getDynamicItems(), this);
            recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            recycler_view_list.setAdapter(itemAdp);
            lnrView.addView(view);
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClickItem(String titel, int position) {
        homeActivity.showMenu();
        Bundle args = new Bundle();
        args.putInt("id", position);
        args.putString("titel", titel);
        Fragment fragment = new SubCategoryFragment();
        fragment.setArguments(args);
        HomeActivity.getInstance().callFragment(fragment);
    }
    @Override
    public void onLongClickItem(View v, int position) {
        Log.e("posiotn",""+position);
    }
    @Override
    public void onItemClick(ProductItem productItem, int position) {
        mContext.startActivity(new Intent(mContext, ItemDetailsActivity.class).putExtra("MyClass", productItem).putParcelableArrayListExtra("MyList", productItem.getPrice()));
    }
    @OnClick({R.id.txt_viewll, R.id.txt_viewllproduct})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_viewll:
                CategoryFragment fragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("arraylist", (Serializable) categoryList);
                fragment.setArguments(bundle);
                HomeActivity.getInstance().callFragment(fragment);
                break;
            case R.id.txt_viewllproduct:
                PopularFragment fragmentp = new PopularFragment();
                HomeActivity.getInstance().callFragment(fragmentp);
                break;
            default:

                break;
        }
    }


    public class MyCustomPagerAdapter extends PagerAdapter {
        Context context;
        List<BannerItem> bannerDatumList;
        LayoutInflater layoutInflater;

        public MyCustomPagerAdapter(Context context, List<BannerItem> bannerDatumList) {
            this.context = context;
            this.bannerDatumList = bannerDatumList;
            layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public int getCount() {
            return bannerDatumList.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = layoutInflater.inflate(R.layout.item_banner, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

            Log.d("banner",bannerDatumList.get(position).getBimg());

            Glide.with(mContext).load(APIClient.baseUrl  + bannerDatumList.get(position).getBimg()).placeholder(R.drawable.empty).into(imageView);
            container.addView(itemView);
            imageView.setOnClickListener(new View.OnClickListener() {
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
            return itemView;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
    private void getHome() {
        HomeActivity.custPrograssbar.prograssCreate(getActivity());
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uid", user.getId());
            Log.d("postUid",user.getId());
            JsonParser jsonParser = new JsonParser();
            Call<JsonObject> call = APIClient.getInterface().getHome((JsonObject) jsonParser.parse(jsonObject.toString()));
            GetResult getResult = new GetResult();
            getResult.setMyListener(this);
            getResult.callForLogin(call, "homepage");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        HomeActivity.getInstance().setdata();
        HomeActivity.getInstance().setFrameMargin(60);
        HomeActivity.getInstance().serchviewShow();
        if (user != null)
            HomeActivity.getInstance().titleChange("Hello " + user.getName());

        if (dynamicDataList != null) {
            setJoinPlayrList(lvlSelected, dynamicDataList);
        }
        if (reletedItemAdp != null) {
            reletedItemAdp.notifyDataSetChanged();
        }
        if (iscart) {
            iscart = false;
            CardFragment fragment = new CardFragment();
            HomeActivity.getInstance().callFragment(fragment);
        }

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void  callback(JsonObject result, String callNo) {
        try {
            if(callNo.equalsIgnoreCase("homepage"))
            {
                HomeActivity.custPrograssbar.closePrograssBar();

                bannerDatumList=new ArrayList<>();
                categoryList=new ArrayList<>();
                Gson gson = new Gson();
                Log.d("ResultHome",result.toString());
                Home home = gson.fromJson(result.toString(), Home.class);

                categoryList.addAll(home.getResultHome().getCatItems());
                for (CatItem bannerItem : categoryList)
                {
                   Log.d("BannerTest","test"+bannerItem.getCatimg()+bannerItem.getId()+bannerItem.getCatname()+bannerItem.getCount());
                }
                adapter = new CategoryAdp(mContext, categoryList, this);
                recyclerView.setAdapter(adapter);

                bannerDatumList.addAll(home.getResultHome().getBannerItems());

                myCustomPagerAdapter = new BannerAdapter(getActivity(), bannerDatumList);
                viewPager.setAdapter(myCustomPagerAdapter);

//                for (BannerItem bannerItem : bannerDatumList)
//                {
//                   Log.d("BannerTest","test"+bannerItem.getBimg()+bannerItem.getId()+bannerItem.getCid());
//                }
//                MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(mContext, bannerDatumList);
//                viewPager.setAdapter(myCustomPagerAdapter);
//                viewPager.startAutoScroll();
//                viewPager.setInterval(3000);
//                viewPager.setCycle(true);
//                viewPager.setStopScrollWhenTouch(true);
//                tabview.setupWithViewPager(viewPager, true);

                reletedItemAdp = new ReletedItemAdp(mContext, home.getResultHome().getProductItems(), this);
                recyclerReleted.setAdapter(reletedItemAdp);
                if (home.getResultHome().getRemainNotification() <= 0) {
                    txtNoti.setVisibility(View.GONE);
                } else {
                    txtNoti.setVisibility(View.VISIBLE);
                    txtNoti.setText("" + home.getResultHome().getRemainNotification());
                }

                dynamicDataList = home.getResultHome().getDynamicData();
                for (DynamicData data : dynamicDataList)
                {
                    Log.d("dynamic_Data","hi"+data.getTitle());
                }
                setJoinPlayrList(lvlSelected, dynamicDataList);

                sessionManager.setStringData(currncy, home.getResultHome().getMainData().getCurrency());
                sessionManager.setStringData(privacy, home.getResultHome().getMainData().getPrivacyPolicy());
                sessionManager.setStringData(aboutUs, home.getResultHome().getMainData().getAboutUs());
                sessionManager.setStringData(contactUs, home.getResultHome().getMainData().getContactUs());
                sessionManager.setStringData(tremcodition, home.getResultHome().getMainData().getTerms());
                sessionManager.setIntData(oMin, home.getResultHome().getMainData().getoMin());
                sessionManager.setStringData(razKey, home.getResultHome().getMainData().getRazKey());
                sessionManager.setStringData(tax, home.getResultHome().getMainData().getTax());

                productItems = home.getResultHome().getProductItems();
//                dynamicDataList = home.getResultHome().getDynamicData();
//                for (DynamicData data : dynamicDataList)
//                {
//                    Log.d("dynamic_Data","hi"+data.getTitle());
//                }
//                setJoinPlayrList(lvlSelected, dynamicDataList);
            }

        } catch (Exception e) {
            e.toString();
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
