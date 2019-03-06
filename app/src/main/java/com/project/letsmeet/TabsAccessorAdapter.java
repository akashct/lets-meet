package com.project.letsmeet;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabsAccessorAdapter extends FragmentPagerAdapter {

    public TabsAccessorAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        switch(i){
            case 0: EventsFragment eventsFragment = new EventsFragment();
                    return eventsFragment;
            case 1: AddEventFragment addEventFragment = new AddEventFragment();
                return addEventFragment;
            case 2: ProfileFragment profileFragment = new ProfileFragment();
                return profileFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0: return "Upcoming Events";
            case 1: return "Add Event";
            case 2: return "Profile";
            default:
                return null;
        }

    }
}
