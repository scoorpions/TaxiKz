package kz.taxikz.data.api.pojo;

import com.google.gson.annotations.SerializedName;

public class TMOrderParam {
    @SerializedName("description")
    protected String description;
    @SerializedName("id")
    protected int id;
    @SerializedName("name")
    protected String name;
    @SerializedName("option_id")
    protected int option_id;
    @SerializedName("use_kzdriver")
    protected int use_kzdriver;
    @SerializedName("use_taxikz")
    protected int use_taxikz;

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public int getOptionId() {
        return this.option_id;
    }

    public boolean isUseTaxikz() {
        if (this.use_taxikz == 0) {
            return false;
        }
        return true;
    }

    public boolean isUseKzdriver() {
        if (this.use_kzdriver == 0) {
            return false;
        }
        return true;
    }

    public String getDescription() {
        return this.description;
    }
}
