package com.example.zafar.sbusiness.other;

        import android.os.Bundle;
        import android.support.design.widget.TabLayout;
        import android.support.v4.app.Fragment;
        import android.support.v4.app.FragmentManager;
        import android.support.v4.app.FragmentStatePagerAdapter;
        import android.util.Log;
        import android.view.ViewGroup;

        import com.example.zafar.sbusiness.Models.Category;
        import com.example.zafar.sbusiness.fragments.HomeFragment;
        import com.example.zafar.sbusiness.fragments.Orders;
        import com.example.zafar.sbusiness.fragments.ProductsWithProductFragment;

        import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();
    FragmentManager fm;
    TabLayout tabLayout;
    ArrayList<Category> category_list;
    Category category;
    ProductsWithProductFragment fragment = null;

    public PagerAdapter(FragmentManager fm, int NumOfTabs , ArrayList<Category> category_list , TabLayout tabLayout) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.category_list = category_list;
        this.fm = fm;
        this.tabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int position) {

//        fragment = new ProductsWithProductFragment();
//        category = category_list.get(position);
//        Bundle args = new Bundle();
//        args.putString("cat_id", category.getCat_id());
//        fragment.setArguments(args);
//        fm.beginTransaction().commit();
        Log.d("poso" , "" + position);
        return mFragmentList.get(position);
    }


    public void addFrag(Fragment fragment) {
        Log.d("Frago" , "Added");
        mFragmentList.add(fragment);
    }


    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
