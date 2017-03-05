package kz.taxikz.helper;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import kz.taxikz.TaxiKzApp;
import kz.taxikz.data.api.pojo.ParamData;
import kz.taxikz.data.api.pojo.TMOrderParam;
import kz.taxikz.data.api.pojo.TMParam;
import kz.taxikz.ui.widget.item.City;
import kz.taxikz.ui.widget.item.Note;

public class DataHelper {

    public static List<City> getCityList() {
        List<TMParam> tmParamList = new Gson().fromJson(TaxiKzApp.storage.getParams(), ParamData.class).getTmParams();
        List<City> cityList = new ArrayList<>();
        for (TMParam param : tmParamList) {
            City city = new City();
            city.setCrewGroupId(param.getCrewGroupId());
            city.setId(param.getCityId());
            city.setName(param.getCityName());
            city.setUdsId(param.getUdsId());
            cityList.add(city);
        }
        return cityList;
    }

    public static List<Integer> getMasks() {
        List<Integer> masks = new ArrayList<>();
        masks.add(700);
        masks.add(701);
        masks.add(702);
        masks.add(705);
        masks.add(707);
        masks.add(708);
        masks.add(747);
        masks.add(771);
        masks.add(775);
        masks.add(776);
        masks.add(777);
        masks.add(778);
        return masks;
    }

    public static List<Note> getNotes() {
        List<TMOrderParam> tmOrderParamList = new Gson().fromJson(TaxiKzApp.storage.getParams(), ParamData.class).getTmOrderParams();
        List<Note> noteList = new ArrayList<>();
        for (TMOrderParam param : tmOrderParamList) {
            if (param.isUseTaxikz()) {
                noteList.add(new Note(param.getId(), param.getOptionId(), param.getName()));
            }
        }
        return noteList;
    }

}
