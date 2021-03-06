package cordova.plugin.extdeviceinfo;

import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;
import java.util.TimeZone;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class ExtDeviceInfo extends CordovaPlugin {

//     public void initialize(CordovaInterface cordova, CordovaWebView webView) {
//         super.initialize(cordova, webView);
//     }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("getRAMSize")) {
            
             JSONObject r = new JSONObject();
            r.put("memory", this.getMemorySize());
            r.put("cpumhz", this.getCpuMhz());
            r.put("totalstorage", this.getTotalSystemStorage());
            callbackContext.success(r);
            
            return true;
        }
        return false;
    }


    //--------------------------------------------------------------------------
    // LOCAL METHODS
    //--------------------------------------------------------------------------

    public String getTotalSystemStorage(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() * (long)stat.getBlockCount() / 1048576;
        return Long.toString(bytesAvailable);
    }

    public String getMemorySize() {

        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
            }
            reader.close();

            totRam = Double.parseDouble(value);

            double mb = totRam / 1024.0;
            lastValue = twoDecimalForm.format(mb);



        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return lastValue;
    }

    public Number getCpuMhz() {
      Number cpuMaxFreq = null;
        try {
            RandomAccessFile reader = new RandomAccessFile("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq", "r");
            cpuMaxFreq = Long.parseLong(reader.readLine());
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuMaxFreq;
    }

}
