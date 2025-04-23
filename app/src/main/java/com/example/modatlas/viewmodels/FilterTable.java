package com.example.modatlas.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class FilterTable extends ViewModel {
    private final MutableLiveData<List<String>> selectedTag = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Boolean> isChange = new MutableLiveData<Boolean>(false);

    public LiveData<List<String>> getSelectedVersions() {
        return selectedTag;
    }
    
    public void addVersion(String tag) {
        List<String> current = selectedTag.getValue();
        if (current != null && !current.contains(tag)) {
            current.add(tag);
            selectedTag.setValue(current); // Trigger observers
        }
    }

    public void removeVersion(String tag) {
        List<String> current = selectedTag.getValue();
        if (current != null && current.contains(tag)) {
            current.remove(tag);
            selectedTag.setValue(current); // Trigger observers
        }
    }

    public boolean haveVersion(String tag){
        List<String> current = selectedTag.getValue();
        if (current != null && current.contains(tag)){
            return true;
        } else{
            return false;
        }
    }

    public String getVersionAt(int position) {
        List<String> current = selectedTag.getValue();
        if (current != null && position >= 0 && position < current.size()) {
            return current.get(position);
        }
        return null; // or throw new IndexOutOfBoundsException if needed
    }

    public void clearVersions() {
        selectedTag.setValue(new ArrayList<>());
    }

    public void setIsChange(boolean isChange){
        this.isChange.setValue(isChange);
    }

    public boolean getIsChange(){
        return Boolean.TRUE.equals(isChange.getValue());
    }
}
