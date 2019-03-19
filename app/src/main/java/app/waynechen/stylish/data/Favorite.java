package app.waynechen.stylish.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wayne Chen on Feb. 2019.
 */
public class Favorite {

    private List<ID> data;


    public List<ID> getData() {
        return data;
    }


    private class ID {
        private String[] id;

        public String[] getId() {
            return id;
        }
    }
}