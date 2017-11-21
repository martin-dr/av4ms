package net.droegemueller.av4ms;

import android.os.Build;
import android.text.Html;
import android.widget.TextView;

public class Util {
    private Util() {
    }

    public static void setHtmlTextview(TextView t, String htmlText) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N)
            t.setText(Html.fromHtml(htmlText));
        else
            t.setText(Html.fromHtml(htmlText, Html.FROM_HTML_MODE_COMPACT));
    }
}
