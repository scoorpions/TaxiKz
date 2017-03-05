package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class ClientInfo {
    @SerializedName("address")
    private String address;
    @SerializedName("balance")
    private int balance;
    @SerializedName("birthday")
    private String birthday;
    @SerializedName("bonus_balance")
    private int bonus_balance;
    @SerializedName("client_id")
    private int clientId;
    @SerializedName("client_group_id")
    private int client_group_id;
    @SerializedName("gender")
    private String gender;
    @SerializedName("login")
    private String login;
    @SerializedName("name")
    private String name;
    @SerializedName("number")
    private String number;
    @SerializedName("password")
    private String password;
    @SerializedName("phones")
    private String[] phones;

    public int getClientId() {
        return this.clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String[] getPhones() {
        return this.phones;
    }

    public void setPhones(String[] phones) {
        this.phones = phones;
    }

    public int getClient_group_id() {
        return this.client_group_id;
    }

    public void setClient_group_id(int client_group_id) {
        this.client_group_id = client_group_id;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getBonus_balance() {
        return this.bonus_balance;
    }

    public void setBonus_balance(int bonus_balance) {
        this.bonus_balance = bonus_balance;
    }

    public int getBalance() {
        return this.balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClientInfo)) {
            return false;
        }
        ClientInfo that = (ClientInfo) o;
        if (getClientId() != that.getClientId() || getClient_group_id() != that.getClient_group_id() || getBonus_balance() != that.getBonus_balance() || getBalance() != that.getBalance()) {
            return false;
        }
        if (getName() != null) {
            if (!getName().equals(that.getName())) {
                return false;
            }
        } else if (that.getName() != null) {
            return false;
        }
        if (getNumber() != null) {
            if (!getNumber().equals(that.getNumber())) {
                return false;
            }
        } else if (that.getNumber() != null) {
            return false;
        }
        if (getAddress() != null) {
            if (!getAddress().equals(that.getAddress())) {
                return false;
            }
        } else if (that.getAddress() != null) {
            return false;
        }
        if (getGender() != null) {
            if (!getGender().equals(that.getGender())) {
                return false;
            }
        } else if (that.getGender() != null) {
            return false;
        }
        if (getBirthday() != null) {
            if (!getBirthday().equals(that.getBirthday())) {
                return false;
            }
        } else if (that.getBirthday() != null) {
            return false;
        }
        if (!Arrays.equals(getPhones(), that.getPhones())) {
            return false;
        }
        if (getLogin() != null) {
            if (!getLogin().equals(that.getLogin())) {
                return false;
            }
        } else if (that.getLogin() != null) {
            return false;
        }
        if (getPassword() != null) {
            z = getPassword().equals(that.getPassword());
        } else if (that.getPassword() != null) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int hashCode;
        int i = 0;
        int clientId = ((getClientId() * 31) + (getName() != null ? getName().hashCode() : 0)) * 31;
        if (getNumber() != null) {
            hashCode = getNumber().hashCode();
        } else {
            hashCode = 0;
        }
        clientId = (clientId + hashCode) * 31;
        if (getAddress() != null) {
            hashCode = getAddress().hashCode();
        } else {
            hashCode = 0;
        }
        clientId = (clientId + hashCode) * 31;
        if (getGender() != null) {
            hashCode = getGender().hashCode();
        } else {
            hashCode = 0;
        }
        clientId = (clientId + hashCode) * 31;
        if (getBirthday() != null) {
            hashCode = getBirthday().hashCode();
        } else {
            hashCode = 0;
        }
        clientId = (((((clientId + hashCode) * 31) + Arrays.hashCode(getPhones())) * 31) + getClient_group_id()) * 31;
        if (getLogin() != null) {
            hashCode = getLogin().hashCode();
        } else {
            hashCode = 0;
        }
        hashCode = (clientId + hashCode) * 31;
        if (getPassword() != null) {
            i = getPassword().hashCode();
        }
        return ((((hashCode + i) * 31) + getBonus_balance()) * 31) + getBalance();
    }

    public String toString() {
        return "ClientInfo{clientId=" + this.clientId + ", name='" + this.name + '\'' + ", number='" + this.number + '\'' + ", address='" + this.address + '\'' + ", gender='" + this.gender + '\'' + ", birthday='" + this.birthday + '\'' + ", phones=" + Arrays.toString(this.phones) + ", client_group_id=" + this.client_group_id + ", login='" + this.login + '\'' + ", password='" + this.password + '\'' + ", bonus_balance=" + this.bonus_balance + ", balance=" + this.balance + '}';
    }
}
