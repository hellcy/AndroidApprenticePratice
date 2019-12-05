package au.com.unicard.tafenswwallet.ui.refund;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RefundViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public RefundViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is refund fragment!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
