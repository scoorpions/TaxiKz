package kz.taxikz.ui.misc;

import android.view.MenuItem;

public class UiEvents {

    public static class NavigationMenuClicked {
        private final MenuItem mMenuItem;

        public MenuItem getMenuItem() {
            return this.mMenuItem;
        }

        public NavigationMenuClicked(MenuItem menuItem) {
            this.mMenuItem = menuItem;
        }
    }
}
