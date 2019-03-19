package app.waynechen.stylish.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductForGson {
    private Data data;

    public Data getData() {
        return data;
    }

    public class Data implements Serializable {
        private long id;
        private String title;
        private String description;
        private int price;
        private String texture;
        private String wash;
        private String place;
        private String note;
        private String story;
        private List<Colors> colors;
        private String[] sizes;
        public List<Variant> variants;
        private String main_image;
        private String[] images;

        public long getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

        public int getPrice() {
            return price;
        }

        public String getTexture() {
            return texture;
        }

        public String getWash() {
            return wash;
        }

        public String getPlace() {
            return place;
        }

        public String getNote() {
            return note;
        }

        public String getStory() {
            return story;
        }

        public List<Colors> getColors() {
            return colors;
        }

        public String[] getSizes() {
            return sizes;
        }

        public String getMain_image() {
            return main_image;
        }

        public String[] getImages() {
            return images;
        }

        public String getTitle() {
            return title;
        }

        public List<Variant> getVariants() {
            return variants;
        }

    }

    public class Colors {
        private String code;
        private String name;

        public String getCode() {
            return code;
        }

        public String getName() {
            return name;
        }
    }

    private class Size {
        private ArrayList<String> size;
    }

    public class Variant {
        private String color_code;
        private String size;
        private int stock;

        public String getColor_code() {
            return color_code;
        }

        public String getSize() {
            return size;
        }

        public int getStock() {
            return stock;
        }
    }

    private class Images {
        private ArrayList<String> images;
    }
}

