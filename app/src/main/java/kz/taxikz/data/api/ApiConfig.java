package kz.taxikz.data.api;

public interface ApiConfig {
    public static final String ADDRESS_SEPARATOR = "*";
    public static final int CODE_OK = 200;
    public static final int[] ORDER_CLIENT_CONFIRMATION_STATES = new int[]{20, 28, 61};;
    public static final int[] ORDER_DRIVER_ON_PLACE_STATES = new int[]{12};;
    public static final int[] ORDER_IN_WORK_STATES = new int[]{12};;
    public static final int[] ORDER_SEARCH_STATES = new int[]{1, 21, 22, 23, 63, 64};;
    public static final int[] ORDER_WAIT_DRIVER_STATES = new int[]{29};;

}
