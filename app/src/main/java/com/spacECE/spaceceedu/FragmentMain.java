package com.spacECE.spaceceedu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.spacECE.spaceceedu.ConsultUS.ConsultUs_SplashScreen;
import com.spacECE.spaceceedu.LearnOnApp.LearnOn_List_SplashScreen;
import com.spacECE.spaceceedu.LibForSmall.library_splash_screen;
import com.spacECE.spaceceedu.VideoLibrary.VideoLibrary_Activity_SplashScreen;
import com.spacece.milestonetracker.ui.activity.StartupActivity;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends Fragment {

    private final int[] mImages = new int[]{
            R.drawable.view1, R.drawable.view2, R.drawable.view3,
            R.drawable.view4, R.drawable.view5
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        ImageCarousel carousel = v.findViewById(R.id.MainFragement_NewsCarousel);
        carousel.registerLifecycle(getLifecycle());

        List<CarouselItem> list = new ArrayList<>();
        for (int image : mImages) {
            list.add(new CarouselItem(image));
        }
        carousel.setData(list);

        CardView cv_videoLibrary = v.findViewById(R.id.CardView_VideoLibrary);
        CardView cv_consultation = v.findViewById(R.id.CardView_Consultation);
        CardView cv_dailyActivities = v.findViewById(R.id.CardView_MyActivities);
        CardView cv_libraryBooks = v.findViewById(R.id.CardView_Library);
        CardView cv_learnOnApp = v.findViewById(R.id.CardView_LearnOnApp);
        CardView cv_milestoneTracker = v.findViewById(R.id.CardView_Milestonetracker);

        cv_videoLibrary.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), VideoLibrary_Activity_SplashScreen.class);
            startActivity(intent);
        });

        cv_consultation.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ConsultUs_SplashScreen.class);
            startActivity(intent);
        });

        cv_dailyActivities.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), ActivitiesListActivity.class);
            startActivity(intent);
        });

        cv_libraryBooks.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), library_splash_screen.class);
            startActivity(intent);
        });

        cv_learnOnApp.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), LearnOn_List_SplashScreen.class);
            startActivity(intent);
        });

        cv_milestoneTracker.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), StartupActivity.class);
            startActivity(intent);
        });

        return v;
    }
}
