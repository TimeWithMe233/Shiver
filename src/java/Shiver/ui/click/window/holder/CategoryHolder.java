package Shiver.ui.click.window.holder;

import Shiver.module.Category;
import Shiver.ui.click.dropdown.holder.ModuleHolder;
import java.util.ArrayList;

public class CategoryHolder {
    private final Category category;
    private final ArrayList<ModuleHolder> modules;

    public CategoryHolder(Category category, ArrayList<ModuleHolder> modules) {
        this.category = category;
        this.modules = modules;
    }

    public Category getCategory() {
        return this.category;
    }

    public ArrayList<ModuleHolder> getModules() {
        return this.modules;
    }
}

