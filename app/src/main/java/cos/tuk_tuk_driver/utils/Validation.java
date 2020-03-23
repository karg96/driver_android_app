package cos.tuk_tuk_driver.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*

@@ Designed & Developed By Jai Bahadur

20-Nov-2018

*/
public  class Validation {

    // validating email id
    public static boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    public static boolean isValidPassword(String pass) {
        if (pass != null && pass.length() >= 6) {
            return true;
        }
        return false;
    }

  /*  public  static void  isInternet(Context context, final View view){

        IntentFilter intentFilter = new IntentFilter(NetworkStateChangeReceiver.NETWORK_AVAILABLE_ACTION);
        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean isNetworkAvailable = intent.getBooleanExtra(IS_NETWORK_AVAILABLE,false);
                String networkStatus = isNetworkAvailable ? "Back Online Refresh!" : "Sorry! No Internet connection";
                Snackbar.make(view,  networkStatus, Snackbar.LENGTH_LONG).show();
            }
        }, intentFilter);
    }*/


}
