package kz.taxikz.data.api.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    public static final Creator CREATOR;
    @SerializedName("bonus_sum")
    protected int bonus_sum;
    @SerializedName("car_color")
    public String car_color;
    @SerializedName("car_mark")
    public String car_mark;
    @SerializedName("car_num")
    public String car_num;
    @SerializedName("customer")
    protected String customer;
    @SerializedName("destination")
    protected String destination;
    @SerializedName("driver_phone")
    public String driver_phone;
    @SerializedName("driver_sourcetime")
    public String driver_sourcetime;
    @SerializedName("driver_time")
    public String driver_time;
    @SerializedName("id")
    protected int id;
    @SerializedName("name")
    protected String name;
    @SerializedName("phone")
    protected String phone;
    @SerializedName("source")
    public String source;
    @SerializedName("starttime")
    protected String starttime;
    @SerializedName("state")
    public int state;
    @SerializedName("stops")
    protected List<String> stops;
    @SerializedName("summ")
    protected int summ;

    /* renamed from: kz.taxikz.data.api.pojo.Order.1 */
    static class C04971 implements Creator {
        C04971() {
        }

        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        public Order[] newArray(int size) {
            return new Order[size];
        }
    }

    public Order(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.state = in.readInt();
        this.phone = in.readString();
        this.customer = in.readString();
        this.source = in.readString();
        this.destination = in.readString();
        this.stops = new ArrayList();
        in.readStringList(this.stops);
        this.summ = in.readInt();
        this.car_color = in.readString();
        this.car_mark = in.readString();
        this.car_num = in.readString();
        this.driver_phone = in.readString();
        this.driver_time = in.readString();
        this.driver_sourcetime = in.readString();
        this.bonus_sum = in.readInt();
        this.starttime = in.readString();
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.state);
        dest.writeString(this.phone);
        dest.writeString(this.customer);
        dest.writeString(this.source);
        dest.writeString(this.destination);
        dest.writeStringList(this.stops);
        dest.writeInt(this.summ);
        dest.writeString(this.car_color);
        dest.writeString(this.car_mark);
        dest.writeString(this.car_num);
        dest.writeString(this.driver_phone);
        dest.writeString(this.driver_time);
        dest.writeString(this.driver_sourcetime);
        dest.writeInt(this.bonus_sum);
        dest.writeString(this.starttime);
    }

    public int describeContents() {
        return 0;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getState() {
        return this.state;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getCustomer() {
        return this.customer;
    }

    public String getSource() {
        return this.source;
    }

    public String getDestination() {
        return this.destination;
    }

    public List<String> getStops() {
        return this.stops;
    }

    public int getSumm() {
        return this.summ;
    }

    public String getCarColor() {
        return this.car_color;
    }

    public String getCarMark() {
        return this.car_mark;
    }

    public String getCarNum() {
        return this.car_num;
    }

    public String getDriverPhone() {
        return this.driver_phone;
    }

    public String getDriverTime() {
        return this.driver_time;
    }

    public String getDriverSourceTime() {
        return this.driver_sourcetime;
    }

    public int getBonusSum() {
        return this.bonus_sum;
    }

    public String getStarttime() {
        return this.starttime;
    }

    static {
        CREATOR = new C04971();
    }
}
