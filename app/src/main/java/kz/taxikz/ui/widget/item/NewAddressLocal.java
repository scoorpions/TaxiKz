package kz.taxikz.ui.widget.item;

import android.os.Parcel;
import android.os.Parcelable;

import kz.taxikz.data.api.pojo.AddressData;
import timber.log.BuildConfig;

public class NewAddressLocal implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        @Override
        public Object createFromParcel(Parcel source) {
            return new NewAddressLocal(source);
        }

        @Override
        public Object[] newArray(int size) {
            return new NewAddressLocal[size];
        }
    };
    private TYPE mAddressType;
    private AddressData mAutocompleteAddressData;
    private String mUserAddress;
    private String mUserHouse;
    private String mUserPorch;

    public enum TYPE {
        UNKNOWN,
        FROM,
        STAY,
        TO
    }

    public NewAddressLocal(Parcel in) {
        this.mAddressType = TYPE.UNKNOWN;
        this.mUserAddress = in.readString();
        this.mUserHouse = in.readString();
        this.mUserPorch = in.readString();
        this.mAddressType = (TYPE) in.readSerializable();
        this.mAutocompleteAddressData = (AddressData) in.readSerializable();
    }

    public NewAddressLocal(AddressData autocompleteAddressData, String mUserAddress, String mUserHouse, String mUserPorch) {
        this.mAddressType = TYPE.UNKNOWN;
        this.mAutocompleteAddressData = autocompleteAddressData;
        this.mUserAddress = mUserAddress;
        this.mUserHouse = mUserHouse;
        this.mUserPorch = mUserPorch;
    }

    public NewAddressLocal(NewAddressLocal address) {
        this.mAddressType = TYPE.UNKNOWN;
        this.mAutocompleteAddressData = address.getAutocompleteAddressData();
        this.mUserAddress = address.getUserAddress();
        this.mUserHouse = address.getUserHouse();
        this.mUserPorch = address.getUserPorch();
    }

    public NewAddressLocal() {
        this.mAddressType = TYPE.UNKNOWN;
        clear();
    }

    public void clear() {
        this.mAutocompleteAddressData = null;
        this.mUserAddress = BuildConfig.VERSION_NAME;
        this.mUserHouse = BuildConfig.VERSION_NAME;
        this.mUserPorch = BuildConfig.VERSION_NAME;
    }

    public AddressData getAutocompleteAddressData() {
        return this.mAutocompleteAddressData;
    }

    public void setAutocompleteAddressData(AddressData autocompleteAddressData) {
        this.mAutocompleteAddressData = autocompleteAddressData;
    }

    public String getUserHouse() {
        return this.mUserHouse;
    }

    public void setUserHouse(String house) {
        if (house != null) {
            this.mUserHouse = house;
        } else {
            this.mUserHouse = BuildConfig.VERSION_NAME;
        }
    }

    public String getUserPorch() {
        return this.mUserPorch;
    }

    public void setUserPorch(String porch) {
        if (porch != null) {
            this.mUserPorch = porch;
        } else {
            this.mUserPorch = BuildConfig.VERSION_NAME;
        }
    }

    public String getUserAddress() {
        return this.mUserAddress;
    }

    public void setUserAddress(String address) {
        if (address != null) {
            this.mUserAddress = address;
        } else {
            this.mUserAddress = BuildConfig.VERSION_NAME;
        }
    }

    public void setAddressType(TYPE type) {
        this.mAddressType = type;
    }

    public TYPE getAddressType() {
        return this.mAddressType;
    }

    public boolean isEmpty() {
        return this.mUserAddress.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NewAddressLocal that = (NewAddressLocal) o;
        if (this.mUserAddress == null ? that.mUserAddress != null : !this.mUserAddress.equals(that.mUserAddress)) {
            return false;
        }
        if (this.mUserHouse == null ? that.mUserHouse != null : !this.mUserHouse.equals(that.mUserHouse)) {
            return false;
        }
        if (this.mUserPorch == null ? that.mUserPorch != null : !this.mUserPorch.equals(that.mUserPorch)) {
            return false;
        }
        if (this.mAutocompleteAddressData != null) {
            if (this.mAutocompleteAddressData.equals(that.mAutocompleteAddressData)) {
                return true;
            }
        } else if (that.mAutocompleteAddressData == null) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result;
        int hashCode;
        int i = 0;
        if (this.mUserAddress != null) {
            result = this.mUserAddress.hashCode();
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (this.mUserHouse != null) {
            hashCode = this.mUserHouse.hashCode();
        } else {
            hashCode = 0;
        }
        i2 = (i2 + hashCode) * 31;
        if (this.mUserPorch != null) {
            hashCode = this.mUserPorch.hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (i2 + hashCode) * 31;
        if (this.mAutocompleteAddressData != null) {
            i = this.mAutocompleteAddressData.hashCode();
        }
        return hashCode + i;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AddressLocal {");
        sb.append("mUserAddress='");
        sb.append(mUserAddress);
        sb.append("' ");
        sb.append("mUserHouse='");
        sb.append(mUserHouse);
        sb.append("' ");
        sb.append("mUserPorch='");
        sb.append(mUserPorch);
        sb.append("' ");
        sb.append("autocompleteAddressData {");
        sb.append(mAutocompleteAddressData.toString());
        sb.append("}}");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mUserAddress);
        dest.writeString(this.mUserHouse);
        dest.writeString(this.mUserPorch);
        dest.writeSerializable(this.mAddressType);
        dest.writeSerializable(this.mAutocompleteAddressData);
    }
}
