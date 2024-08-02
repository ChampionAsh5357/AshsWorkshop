package net.ashwork.mc.ashsworkshop.client.screen;

import net.ashwork.mc.ashsworkshop.menu.WorkbenchMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;

// TODO: Document
public class WorkbenchScreen extends Screen implements MenuAccess<WorkbenchMenu> {

    private final WorkbenchMenu menu;

    public WorkbenchScreen(WorkbenchMenu menu, Component title) {
        super(title);
        this.menu = menu;
    }


    @Override
    public WorkbenchMenu getMenu() {
        return this.menu;
    }
}
