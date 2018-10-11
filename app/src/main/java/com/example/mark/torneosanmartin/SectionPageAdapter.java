package com.example.mark.torneosanmartin;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class SectionPageAdapter extends  FragmentPagerAdapter{

    //EquiposFragment equiposFragment;
    //TorneosFragment torneosFragment;
    NoticiasFragment noticiasfragment;

        public SectionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).


                     return noticiasfragment=new NoticiasFragment();



        }

        @Override
        public int getCount() {
            return 1;
        }
    }

