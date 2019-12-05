package au.com.unicard.tafenswwallet.ui.topup;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TopupViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public TopupViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is top up fragment!");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
