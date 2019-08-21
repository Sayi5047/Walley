package io.hustler.wallzy.MVVM;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

public class CategoryViewModel extends ViewModelProvider {
    public CategoryViewModel(@NonNull ViewModelStoreOwner owner, @NonNull Factory factory) {
        super(owner, factory);
    }
}
