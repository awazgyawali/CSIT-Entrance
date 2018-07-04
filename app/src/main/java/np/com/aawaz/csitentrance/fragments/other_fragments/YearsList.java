package np.com.aawaz.csitentrance.fragments.other_fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import np.com.aawaz.csitentrance.R;
import np.com.aawaz.csitentrance.adapters.YearListAdapter;
import np.com.aawaz.csitentrance.objects.YearItem;

public class YearsList extends Fragment {

    private RecyclerView recyclerview;

    public YearsList() {
        // Required empty public constructor
    }

    public static YearsList newInstance() {
        return new YearsList();
    }

    ArrayList<YearItem> items = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerview = view.findViewById(R.id.yearListRecy);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        prepareCollegeModelQuestion();

        prepareOldQuestions();

        prepareModelQuestion();


        recyclerview.setAdapter(new YearListAdapter(getContext(), items));
    }

    private void prepareCollegeModelQuestion() {

        YearItem modelHeader = new YearItem();
        modelHeader.setType(YearItem.Companion.getSECTION_TITLE());
        modelHeader.setTitle("College Model Questions");
        items.add(modelHeader);

        YearItem item7 = new YearItem();
        item7.setType(YearItem.Companion.getCOLLEGE_MODEL());
        item7.setTitle("Samriddhi Model Paper");
        item7.setLogo(R.drawable.samriddhi_logo);
        item7.setPaperCode(12);
        items.add(item7);

    }

    private void prepareModelQuestion() {
        YearItem modelHeader = new YearItem();
        modelHeader.setType(YearItem.Companion.getSECTION_TITLE());
        modelHeader.setTitle("Model Questions");
        items.add(modelHeader);

        YearItem item7 = new YearItem();
        item7.setType(YearItem.Companion.getYEAR_SET());
        item7.setTitle("Model 1");
        item7.setPaperCode(6);
        items.add(item7);

        YearItem item8 = new YearItem();
        item8.setType(YearItem.Companion.getYEAR_SET());
        item8.setTitle("Model 2");
        item8.setPaperCode(7);
        items.add(item8);

        YearItem item9 = new YearItem();
        item9.setType(YearItem.Companion.getYEAR_SET());
        item9.setTitle("Model 3");
        item9.setPaperCode(8);
        items.add(item9);

        YearItem item10 = new YearItem();
        item10.setType(YearItem.Companion.getYEAR_SET());
        item10.setTitle("Model 4");
        item10.setPaperCode(9);
        items.add(item10);

        YearItem item11 = new YearItem();
        item11.setType(YearItem.Companion.getYEAR_SET());
        item11.setTitle("Model 5");
        item11.setPaperCode(10);
        items.add(item11);

        YearItem item12 = new YearItem();
        item12.setType(YearItem.Companion.getYEAR_SET());
        item12.setTitle("Model 6");
        item12.setPaperCode(11);
        items.add(item12);

        YearItem item13 = new YearItem();
        item13.setType(YearItem.Companion.getYEAR_SET());
        item13.setTitle("Model 7");
        item13.setPaperCode(13);
        items.add(item13);
    }

    private void prepareOldQuestions() {
        YearItem item = new YearItem();
        item.setType(YearItem.Companion.getSECTION_TITLE());
        item.setTitle("Old Questions");
        items.add(item);

        YearItem item1 = new YearItem();
        item1.setType(YearItem.Companion.getYEAR_SET());
        item1.setTitle("2069 TU Examination");
        item1.setPaperCode(0);
        items.add(item1);

        YearItem item2 = new YearItem();
        item2.setType(YearItem.Companion.getYEAR_SET());
        item2.setTitle("2070 TU Examination");
        item2.setPaperCode(1);
        items.add(item2);

        YearItem item3 = new YearItem();
        item3.setType(YearItem.Companion.getYEAR_SET());
        item3.setTitle("2071 TU Examination");
        item3.setPaperCode(2);
        items.add(item3);

        YearItem item4 = new YearItem();
        item4.setType(YearItem.Companion.getYEAR_SET());
        item4.setTitle("2072 TU Examination");
        item4.setPaperCode(3);
        items.add(item4);

        YearItem item5 = new YearItem();
        item5.setType(YearItem.Companion.getYEAR_SET());
        item5.setTitle("2073 TU Examination");
        item5.setPaperCode(4);
        items.add(item5);

        YearItem item6 = new YearItem();
        item6.setType(YearItem.Companion.getYEAR_SET());
        item6.setTitle("2074 TU Examination");
        item6.setPaperCode(5);
        items.add(item6);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_years_list, container, false);
    }
}
