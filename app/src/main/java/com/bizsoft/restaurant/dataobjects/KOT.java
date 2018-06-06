package com.bizsoft.restaurant.dataobjects;

/**
 * Created by GopiKing on 02-12-2017.
 */

public class KOT {

    Long id;



    public String status;
    String note;
    Long tableId;
    Item item;

    String tableDetailsFDB;

    public Long getKotChannelId() {
        return kotChannelId;
    }

    public void setKotChannelId(Long kotChannelId) {
        this.kotChannelId = kotChannelId;
    }

    Long kotChannelId;

    public String getTableDetailsFDB() {
        return tableDetailsFDB;
    }

    public void setTableDetailsFDB(String tableDetailsFDB) {
        this.tableDetailsFDB = tableDetailsFDB;
    }

    public Long getTableId() {
        return tableId;
    }

    public void setTableId(Long tableId) {
        this.tableId = tableId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Item getItem() {
        return item;

    }

    public void setItem(Item item) {
        this.item = item;
    }

    String waiter_id;
    String item_id;






    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getWaiter_id() {
        return waiter_id;
    }

    public void setWaiter_id(String waiter_id) {
        this.waiter_id = waiter_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }






}
