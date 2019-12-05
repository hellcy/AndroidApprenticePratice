package au.com.unicard.tafenswwallet.ui.topup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.com.unicard.tafenswwallet.R;

public class TopupFragment extends Fragment {

    private TopupViewModel topupViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        topupViewModel = ViewModelProviders.of(this).get(TopupViewModel.class);
        View root = inflater.inflate(R.layout.fragment_topup, container, false);
        final TextView textView = root.findViewById(R.id.text_topup);

        topupViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
