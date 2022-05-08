package com.app.dsmovieticket.common;

public class Status {
    public static class Cart{
        public static final String PENDING = "PENDING";
        public static final String PAYMENTDONE = "PAYMENTDONE";
        public static final String PENDINGFORCONF = "PENDINGFORCONF";
        public static final String DISPATCH = "DISPATCH";
    }

    public static class Delivery{
        public static final String COURIERSAMEADDRESS = "COURIERSAMEADDRESS";
        public static final String COURIERANOTHERADDRESS = "COURIERANOTHERADDRESS";
        public static final String PICKUP = "PICKUP";
    }
}
