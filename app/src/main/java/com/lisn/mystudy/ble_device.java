package com.lisn.mystudy;

/**
 * @author : lishan
 * @e-mail : cnlishan@163.com
 * @date : 2020/10/30 9:23 AM
 * @desc :
 */
public class ble_device {
    public String ble_address;

    //由于自带equals函数不能满足我们的需求，所以我们需要重写euals，可以理解为扩充
    @Override
    public boolean equals(Object obj){
        if (obj instanceof ble_device){
            if(ble_address.equals(((ble_device) obj).ble_address)){
                return true;
            }
            else {
                return false;
            }
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode(){
        return ble_address.hashCode();
    }
}
