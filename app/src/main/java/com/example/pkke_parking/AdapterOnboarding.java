package com.example.pkke_parking;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class AdapterOnboarding extends PagerAdapter {

    private Context context;
    private LinearLayout linearLayout;
    private LayoutInflater layoutInflater;

    public AdapterOnboarding(Context context){
        this.context = context;
    }

    public String[] content_title = {
            "Memudahkan PArkirmu",
            "Selamat Tinggal Titip Sticker",
            "Efisiensi Sistem"
    };

    public int [] content_gambar = {
            R.drawable.illustrationonboarding2,
            R.drawable.illustrationonboarding,
            R.drawable.illustrationonboarding2
    };

    public String [] content_deskripsi = {
            "Dengan adanya E - Parking anda tidak akan perlu asdasdasdasdasdasdasdasdasdasd",
            "Dengan adanya E - Parking anda tidak akan perlu asdasdasdasdasdasdasdasdasdasd",
            "Dengan adanya E - Parking anda tidak akan perlu asdasdasdasdasdasdasdasdasdasd"
    };

    @Override
    public int getCount() {
        return content_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.format_onboarding, container, false);

        ImageView gambar = view.findViewById(R.id.ilustrasi);
        TextView judul = view.findViewById(R.id.title);
        TextView deksripsi = view.findViewById(R.id.descripsi);

        gambar.setImageResource(content_gambar[position]);
        judul.setText(content_title[position]);
        deksripsi.setText(content_deskripsi[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//        super.destroyItem(container, position, object);
    }
}