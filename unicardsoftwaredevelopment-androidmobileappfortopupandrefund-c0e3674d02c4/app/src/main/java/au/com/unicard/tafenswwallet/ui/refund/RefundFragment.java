package au.com.unicard.tafenswwallet.ui.refund;

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

public class RefundFragment extends Fragment {

    private RefundViewModel refundViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        refundViewModel = ViewModelProviders.of(this).get(RefundViewModel.class);
        View root = inflater.inflate(R.layout.fragment_refund, container, false);
        final TextView textView = root.findViewById(R.id.text_refund);

        refundViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}

