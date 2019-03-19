package app.waynechen.stylish.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Favorite {

    private ID data;


    public ID getData() {
        return data;
    }


    public class ID {
        private String[] id;

        public String[] getIds() {
            return id;
        }
    }
}