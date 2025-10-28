package com.operation;

import java.util.List;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Image;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;
import org.zkoss.zul.Messagebox;

public class MobileComposer extends SelectorComposer<Window> {

    @Wire
    private Listbox Itemlist;

    @Wire
    private Label l1, l2, l3, l4, l5;

    @Wire
    private Image img;

    @Wire
    private Textbox tsearchbox;

  
    private MobileServiceImplements mobileService = new MobileServiceImplements();


    @Listen("onCreate = #Itemlist")
    public void loadAllMobiles() {
        List<Mobile> list = mobileService.findAllMobile();
        Itemlist.setModel(new ListModelList<>(list));
    }

    @Listen("onClick = #bSearch")
    public void searchMobileById() {
        String text = tsearchbox.getValue().trim();
        if (text.isEmpty()) {
            Messagebox.show("Please enter an ID to search.");
            return;
        }

        try {
            int id = Integer.parseInt(text);
            Mobile mobile = mobileService.findMobileById(id);

            if (mobile != null) {
                // Update labels with data
                l1.setValue("Id: " + mobile.getId());
                l2.setValue("Brand: " + mobile.getBrand());
                l3.setValue("Model: " + mobile.getModel());
                l4.setValue("Price: " + mobile.getPrice());
                l5.setValue("Description: " + mobile.getDescription());

                // Update listbox with only that mobile
                ListModel<Mobile> singleMobile = new ListModelList<>(List.of(mobile));
                Itemlist.setModel(singleMobile);

            } else {
                Messagebox.show("No mobile found with ID: " + id);
            }

        } catch (NumberFormatException e) {
            Messagebox.show("Invalid ID. Please enter a number.");
        }
    }

    @Listen("onSelect = #Itemlist")
    public void onMobileSelect() {
        Listitem list = Itemlist.getSelectedItem();
        if (list != null) {
            Mobile selectedMobile = (Mobile) list.getValue();
            	img.setSrc(selectedMobile.getImage());
            l1.setValue("Id: " + selectedMobile.getId());
            l2.setValue("Brand: " + selectedMobile.getBrand());
            l3.setValue("Model: " + selectedMobile.getModel());
            l4.setValue("Price: " + selectedMobile.getPrice());
            l5.setValue("Description: " + selectedMobile.getDescription());


        }
    }
}
