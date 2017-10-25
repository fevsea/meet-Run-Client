package edu.upc.fib.meetnrun.utils;

import java.util.Date;

/**
 * Created by Awais Iqbal on 25/10/2017.
 */

public class FormContainers {

    public static class CreateMeetingForm{
        public Integer id;
        public String title;
        public String description;
        public Boolean _public;
        public Integer level;
        public String date;
        public String latitude;
        public String longitude;

        public CreateMeetingForm(Integer id, String title, String description, Boolean _public, Integer level, String date, String latitude, String longitude) {
            this.id = id;
            this.title = title;
            this.description = description;
            this._public = _public;
            this.level = level;
            this.date = date;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

}
