package kz.taxikz.ui.address.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.squareup.otto.Subscribe;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kz.taxikz.TaxiKzApp;
import kz.taxikz.controllers.address.AddressEvents.AddressFailed;
import kz.taxikz.controllers.address.AddressEvents.AddressSuccess;
import kz.taxikz.data.api.pojo.AddressData;
import kz.taxikz.data.db.TinyDB;
import kz.taxikz.taxi4.R;
import kz.taxikz.ui.address.adapter.AutocompleteAdapter;
import kz.taxikz.ui.address.adapter.FavoriteAdapter;
import kz.taxikz.ui.widget.item.NewAddressLocal;
import kz.taxikz.ui.widget.item.NewAddressLocal.TYPE;
import kz.taxikz.utils.DividerItemDecoration;
import timber.log.BuildConfig;

public class AddressFragment extends BaseAddressFragment {
    public static final String EXTRA_ADDRESS = "EXTRA_ADDRESS";
    public static final String FAVORITE_LIST = "NEW_FAVORITE_LIST";
    public static final int MIN_CHARACTERS_FOR_AUTOCOMPLETE = 2;
    private NewAddressLocal mAddress;
    @BindView(R.id.address_details_divider)
    View mAddressDetailsDividerView;
    @BindView(R.id.address_house_editText)
    EditText mAddressDetailsHouseEditText;
    @BindView(R.id.address_details_layout)
    LinearLayout mAddressDetailsLayout;
    @BindView(R.id.address_porch_editText)
    EditText mAddressDetailsPorchEditText;
    @BindView(R.id.address_porch_icon_imageView)
    ImageView mAddressDetailsPorchIconImageView;
    @BindView(R.id.address_editText)
    EditText mAddressEditText;
    private TYPE mAddressType;
    @BindView(R.id.addresses_recyclerView)
    RecyclerView mAddressesRecyclerView;
    private AutocompleteAdapter mAutocompleteAdapter;
    private List<AddressData> mAutocompleteAddresses;
    @BindView(R.id.autocomplete_loading)
    AVLoadingIndicatorView mAutocompleteLoading;
    private String mAutocompleteSearchString;
    private Callbacks mCallbacks;
    @BindView(R.id.clear_address_imageView)
    ImageView mClearAddressImageView;
    private FavoriteAdapter mFavoriteAdapter;
    private List<NewAddressLocal> mFavoriteAddresses;
    private ItemTouchHelper mFavoriteAddressesItemTouchHelper;
    private Gson mGson;
    @BindView(R.id.select_address_button)
    Button mSelectAddressButton;
    private TinyDB mTinyDB;

    public interface Callbacks {
        void onAddressReady(NewAddressLocal newAddressLocal);
    }

    public static Fragment newInstance(NewAddressLocal addressLocal, Callbacks callbacks) {
        AddressFragment addressFragment = new AddressFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_ADDRESS, addressLocal);
        addressFragment.setArguments(bundle);
        addressFragment.setCallbacks(callbacks);
        return addressFragment;
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(this.mAddress.getAutocompleteAddressData() != null || this.mAutocompleteSearchString.isEmpty() || this.mAddressEditText.getText().toString().equals(this.mAutocompleteSearchString))) {
            requestAutocomplete(this.mAddressEditText.getText().toString());
        }
        updateUI();
    }

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address_new, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mGson = new Gson();
        this.mTinyDB = new TinyDB(getContext());
        this.mFavoriteAddresses = getFavoriteAddressesFromCache();
        this.mAutocompleteAddresses = new ArrayList<>();
        this.mAutocompleteSearchString = BuildConfig.VERSION_NAME;
        this.mAddress = getArguments().getParcelable(EXTRA_ADDRESS);
        if (this.mAddress != null) {
            this.mAddressType = this.mAddress.getAddressType();
        } else {
            this.mAddressType = TYPE.UNKNOWN;
        }
        this.mAddressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (AddressFragment.this.mAddress.getAutocompleteAddressData() == null) {
                    if (!AddressFragment.this.requestAutocomplete(s.toString())) {
                        AddressFragment.this.clearAutocomplete();
                    }
                    AddressFragment.this.updateUI();
                } else if (!AddressFragment.this.mAddress.getAutocompleteAddressData().getName().equals(s.toString())) {
                    AddressFragment.this.mAddress.clear();
                    if (!AddressFragment.this.requestAutocomplete(s.toString())) {
                        AddressFragment.this.clearAutocomplete();
                    }
                    AddressFragment.this.updateUI();
                }
            }
        });
        this.mAddressDetailsHouseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                AddressFragment.this.mAddress.setUserHouse(s.toString());
                if (s.toString().isEmpty()) {
                    AddressFragment.this.hideSelectAddressButton();
                } else {
                    AddressFragment.this.showSelectAddressButton();
                }
            }
        });
        this.mAddressDetailsPorchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAddress.setUserPorch(s.toString());
            }
        });
        this.mClearAddressImageView.setOnClickListener(v -> {
            clearAutocomplete();
            this.mAddress.clear();
            this.mAddressEditText.setText(BuildConfig.VERSION_NAME);
            updateUI();
        });
        this.mSelectAddressButton.setOnClickListener(v -> {
            if (this.mCallbacks != null && this.mAddress.getAutocompleteAddressData() != null) {
                this.mAddress.setUserAddress(this.mAddress.getAutocompleteAddressData().getName());
                addAddressToFavorite();
                saveFavoriteAddressesToCache();
                this.mCallbacks.onAddressReady(this.mAddress);
            }
        });
        this.mFavoriteAdapter = new FavoriteAdapter(this.mFavoriteAddresses, i -> {
            this.mAddress = new NewAddressLocal(this.mFavoriteAddresses.get(i));
            updateUI();
        });
        this.mAutocompleteAdapter = new AutocompleteAdapter(this.mAutocompleteAddresses, i -> {
            this.mAddress.setAutocompleteAddressData(this.mAutocompleteAddresses.get(i));
            updateUI();
        });
        this.mAddressesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.mAddressesRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), 1));
        this.mFavoriteAddressesItemTouchHelper = new ItemTouchHelper(new Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
                return Callback.makeMovementFlags(0, 48);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                mFavoriteAddresses.remove(position);
                mFavoriteAdapter.notifyItemRemoved(position);
                mFavoriteAdapter.notifyDataSetChanged();
                saveFavoriteAddressesToCache();
            }

        });
        updateUI();
    }

    private List<NewAddressLocal> getFavoriteAddressesFromCache() {
        List<NewAddressLocal> addresses = new ArrayList<>();
        for (String jsonAddress : this.mTinyDB.getListString(FAVORITE_LIST + TaxiKzApp.storage.getCityId() + TaxiKzApp.storage.getPhone())) {
            addresses.add(this.mGson.fromJson(jsonAddress, NewAddressLocal.class));
        }
        return addresses;
    }

    private void saveFavoriteAddressesToCache() {
        ArrayList<String> jsonAddresses = new ArrayList<>();
        for (NewAddressLocal address : mFavoriteAddresses) {
            jsonAddresses.add(this.mGson.toJson(address));
        }
        this.mTinyDB.putListString(FAVORITE_LIST + TaxiKzApp.storage.getCityId() + TaxiKzApp.storage.getPhone(), jsonAddresses);
    }

    private void addAddressToFavorite() {
        for (int i = 0; i < this.mFavoriteAddresses.size(); i++) {
            if (mFavoriteAddresses.get(i).equals(mAddress)) {
                mFavoriteAddresses.remove(i);
                break;
            }
        }
        mFavoriteAddresses.add(0, mAddress);
    }

    private boolean requestAutocomplete(String searchString) {
        if (searchString.length() < MIN_CHARACTERS_FOR_AUTOCOMPLETE) {
            return false;
        }
        mAddress.clear();
        autoCompleteController.autoComplete(searchString);
        showAutocompleteLoadingIcon();
        return true;
    }

    private void clearAutocomplete() {
        mAutocompleteAddresses.clear();
        mAutocompleteSearchString = BuildConfig.VERSION_NAME;
    }

    private void showFavoriteAddresses() {
        mAddressesRecyclerView.setAdapter(mFavoriteAdapter);
        mFavoriteAddressesItemTouchHelper.attachToRecyclerView(mAddressesRecyclerView);
    }

    private void showAutocompleteAddresses() {
        mAddressesRecyclerView.setAdapter(mAutocompleteAdapter);
        mFavoriteAddressesItemTouchHelper.attachToRecyclerView(null);
    }

    private void updateUI() {
        AddressData addressData = mAddress.getAutocompleteAddressData();
        if (addressData != null) {
            mAddressEditText.setText(addressData.getName());
            mAddressEditText.setSelection(mAddressEditText.getText().length());
            showClearButton();
            if (addressData.getType().equals(AddressData.TYPE_STREET)) {
                if (this.mAddressType == TYPE.FROM) {
                    showAddressDetails(true);
                } else {
                    showAddressDetails(false);
                }
                updateUserDetails();
            } else if (addressData.getType().equals(AddressData.TYPE_POINT)) {
                hideAddressDetails();
                showSelectAddressButton();
            }
        } else {
            hideAddressDetails();
            hideSelectAddressButton();
            if (isAutocompleteLoading()) {
                hideClearButton();
            } else {
                showClearButton();
            }
        }
        if (this.mAutocompleteAddresses.isEmpty()) {
            showFavoriteAddresses();
            return;
        }
        showAutocompleteAddresses();
        mAutocompleteAdapter.notifyDataSetChanged();
    }

    private void updateUserDetails() {
        mAddressDetailsHouseEditText.setText(mAddress.getUserHouse());
        mAddressDetailsPorchEditText.setText(mAddress.getUserPorch());
        mAddressDetailsHouseEditText.setSelection(mAddressDetailsHouseEditText.getText().length());
        mAddressDetailsHouseEditText.requestFocus();
    }

    private void showSelectAddressButton() {
        mSelectAddressButton.setVisibility(View.VISIBLE);
    }

    private void hideSelectAddressButton() {
        mSelectAddressButton.setVisibility(View.GONE);
    }

    private void showAddressDetails(boolean showPorch) {
        mAddressDetailsDividerView.setVisibility(View.VISIBLE);
        mAddressDetailsLayout.setVisibility(View.VISIBLE);
        if (showPorch) {
            mAddressDetailsPorchIconImageView.setVisibility(View.VISIBLE);
            mAddressDetailsPorchEditText.setVisibility(View.VISIBLE);
            return;
        }
        mAddressDetailsPorchIconImageView.setVisibility(View.GONE);
        mAddressDetailsPorchEditText.setVisibility(View.GONE);
    }

    private void hideAddressDetails() {
        mAddressDetailsDividerView.setVisibility(View.GONE);
        mAddressDetailsLayout.setVisibility(View.GONE);
    }

    private void showAutocompleteLoadingIcon() {
        mAutocompleteLoading.setVisibility(View.VISIBLE);
    }

    private void hideAutocompleteLoadingIcon() {
        mAutocompleteLoading.setVisibility(View.INVISIBLE);
    }

    private boolean isAutocompleteLoading() {
        return mAutocompleteLoading.getVisibility() == View.VISIBLE;
    }

    private void showClearButton() {
        if (mAddressEditText.getText().toString().isEmpty()) {
            mClearAddressImageView.setVisibility(View.INVISIBLE);
        } else {
            mClearAddressImageView.setVisibility(View.VISIBLE);
        }
    }

    private void hideClearButton() {
        mClearAddressImageView.setVisibility(View.INVISIBLE);
    }

    @Subscribe
    public void onAutocompleteSuccess(AddressSuccess addressSuccess) {
        hideAutocompleteLoadingIcon();
        showClearButton();
        if (addressSuccess.getSearchString().equals(mAddressEditText.getText().toString())) {
            clearAutocomplete();
            mAutocompleteAddresses.addAll(addressSuccess.getAddressList());
            mAutocompleteSearchString = addressSuccess.getSearchString();
            updateUI();
        }
    }

    @Subscribe
    public void onAutocompleteFailed(AddressFailed addressFailed) {
        hideAutocompleteLoadingIcon();
        showClearButton();
        clearAutocomplete();
        updateUI();
    }
}
