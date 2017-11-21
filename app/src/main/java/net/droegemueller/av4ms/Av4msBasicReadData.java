package net.droegemueller.av4ms;

import android.os.Parcel;
import android.os.Parcelable;

import net.droegemueller.av4ms.core.domain.client.MesswerteResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Av4msBasicReadData implements Parcelable {

    public Av4msBasicReadData() {
    }

    public enum SlotState implements Parcelable {
        Charging, Discharging, Full, Error, EmptySlot;

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(toString(this));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SlotState> CREATOR = new Creator<SlotState>() {
            @Override
            public SlotState createFromParcel(Parcel in) {
                return SlotState.fromString(in.readString());
            }

            @Override
            public SlotState[] newArray(int size) {
                return new SlotState[size];
            }
        };

        public static SlotState fromString(String s) {
            s = s == null ? "" : s.trim().toUpperCase();
            char ch = s.length() == 1 ? s.charAt(0) : '\0';
            switch (ch) {
                case 'F': return Error;
                case 'E': return Discharging;
                case 'L': return Charging;
                case 'V': return Full;
                case '-': return EmptySlot;
                default: return null;
            }
        }
        public static String toString(Av4msBasicReadData.SlotState data) {
            if (data == null) {
                return "";
            }
            else switch (data) {
                case Error: return "F";
                case Discharging: return "E";
                case Charging: return "L";
                case Full: return "V";
                case EmptySlot: return "-";
                default: return "";
            }
        }
    }

    public static class ChargerSlotStandardData implements Parcelable{
        public SlotState slotState;
        public Integer chargingVoltage;
        public Integer chargingAverageVoltage;
        public Integer dischargingVoltage;
        public Integer dischargingAverageVoltage;

        public Integer chargingCurrent;
        public Integer dischargingCurrent;

        public Integer chargeCapacity;
        public Integer dischargeCapacity;

        public Integer chargeTime;
        public Integer dischargeTime;

        public Integer chargeEnergy;
        public Integer dischargeEnergy;

        public Integer cycleCount;
        public String statusText;

        public String logText;

        public boolean connected;

        private static Pattern parseInt = Pattern.compile("^\\s*(\\d+).*$");
        private static Pattern parseFloat = Pattern.compile("^\\s*(\\d+)(?:\\.(\\d{1,3}))?.*$");
        private static Pattern parseTime = Pattern.compile("^\\s*(\\d+):(\\d+):(\\d+).*$");

        protected ChargerSlotStandardData(Parcel in) {
            slotState = in.readParcelable(SlotState.class.getClassLoader());

            if (in.readByte() == 0) chargingVoltage = null;
            else chargingVoltage = in.readInt();
            if (in.readByte() == 0) chargingAverageVoltage = null;
            else chargingAverageVoltage = in.readInt();
            if (in.readByte() == 0) dischargingVoltage = null;
            else dischargingVoltage = in.readInt();
            if (in.readByte() == 0) dischargingAverageVoltage = null;
            else dischargingAverageVoltage = in.readInt();

            if (in.readByte() == 0) chargingCurrent = null;
            else chargingCurrent = in.readInt();
            if (in.readByte() == 0) dischargingCurrent = null;
            else dischargingCurrent = in.readInt();

            if (in.readByte() == 0) chargeCapacity = null;
            else chargeCapacity = in.readInt();
            if (in.readByte() == 0) dischargeCapacity = null;
            else dischargeCapacity = in.readInt();

            if (in.readByte() == 0) chargeTime = null;
            else chargeTime = in.readInt();
            if (in.readByte() == 0) dischargeTime = null;
            else dischargeTime = in.readInt();

            if (in.readByte() == 0) chargeEnergy = null;
            else chargeEnergy = in.readInt();
            if (in.readByte() == 0) dischargeEnergy = null;
            else dischargeEnergy = in.readInt();

            if (in.readByte() == 0) cycleCount = null;
            else cycleCount = in.readInt();

            statusText = in.readString();
            logText = in.readString();
            connected = in.readByte() != 0;
        }

        public ChargerSlotStandardData() {
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(slotState, flags);

            if (chargingVoltage == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(chargingVoltage); }
            if (chargingAverageVoltage == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(chargingAverageVoltage); }
            if (dischargingVoltage == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(dischargingVoltage); }
            if (dischargingAverageVoltage == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(dischargingAverageVoltage); }

            if (chargingCurrent == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(chargingCurrent); }
            if (dischargingCurrent == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(dischargingCurrent); }

            if (chargeCapacity == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(chargeCapacity); }
            if (dischargeCapacity == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(dischargeCapacity); }

            if (chargeTime == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(chargeTime); }
            if (dischargeTime == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(dischargeTime); }

            if (chargeEnergy == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(chargeEnergy); }
            if (dischargeEnergy == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(dischargeEnergy); }

            if (cycleCount == null) dest.writeByte((byte)0);
            else { dest.writeByte((byte)1); dest.writeInt(cycleCount); }
            dest.writeString(statusText);
            dest.writeString(logText);
            dest.writeByte((byte) (connected ? 1 : 0));
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<ChargerSlotStandardData> CREATOR = new Creator<ChargerSlotStandardData>() {
            @Override
            public ChargerSlotStandardData createFromParcel(Parcel in) {
                return new ChargerSlotStandardData(in);
            }

            @Override
            public ChargerSlotStandardData[] newArray(int size) {
                return new ChargerSlotStandardData[size];
            }
        };

        private static Integer getTime(String s) {
            if (s == null) return null;
            Matcher m = parseTime.matcher(s);
            if (m.matches()) {
                int hh = Integer.parseInt(m.group(1));
                int mm = Integer.parseInt(m.group(2));
                int ss = Integer.parseInt(m.group(3));
                return hh * 3600 + mm * 60 + ss;
            }
            return null;
        }
        private static Integer getDoubleTimes1000(String s) {
            if (s == null) return null;
            Matcher m = parseFloat.matcher(s);
            if (m.matches()) {
                Integer val = Integer.parseInt(m.group(1));
                String dec = m.group(2) != null && m.group(2).length() > 0 ? m.group(2) : "000";
                if (dec.length() < 3) dec = dec + "0";
                if (dec.length() < 3) dec = dec + "0";
                return 1000 * val + Integer.parseInt(dec);
            }
            return null;
        }
        private static Integer getInteger(String s) {
            if (s == null) return null;
            Matcher m = parseFloat.matcher(s);
            if (m.matches()) {
                return Integer.parseInt(m.group(1));
            }
            return null;
        }
        private static ChargerSlotStandardData fromValues(String ue, String ul, String ueavg, String ulavg,
                                                          String ie, String il, String ce, String cl,
                                                          String te, String tl, String ee, String el,
                                                          String cy, String status, String statusText,
                                                          String logText) {
            ChargerSlotStandardData d = new ChargerSlotStandardData();
            d.dischargingVoltage = getDoubleTimes1000(ue);
            d.chargingVoltage = getDoubleTimes1000(ul);
            d.dischargingAverageVoltage = getDoubleTimes1000(ueavg);
            d.chargingAverageVoltage = getDoubleTimes1000(ulavg);
            d.dischargingCurrent = getInteger(ie);
            d.chargingCurrent = getInteger(il);
            d.dischargeCapacity = getInteger(ce);
            d.chargeCapacity = getInteger(cl);
            d.dischargeTime = getTime(te);
            d.chargeTime = getTime(tl);
            d.dischargeEnergy = getInteger(ee);
            d.chargeEnergy = getInteger(el);
            d.cycleCount = getInteger(cy);
            d.slotState = SlotState.fromString(status);
            d.statusText = statusText == null ? null : statusText.trim();
            d.logText = logText == null ? null : logText.trim();

            d.connected = !(d.statusText != null && d.statusText.matches("^.*Keine.Verbindung.zum.Ladeger.*$"));

            return d;
        }
    }

    // ZellTyp:
    // "Micro Zelle" oder "Mignon Zelle"

    // Status-Text:
//    if   s == 0:                            msg = "Keine Zelle erkannt"
//    elif s == 5:
//    msg = self.ZellTyp(f) + " formieren"
//    elif s == 10 or s == 15:
//    msg = self.ZellTyp(f) + " entl<C3><A4>dt"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//    elif s == 25:
//    msg = self.ZellTyp(f) + " pr<C3><BC>ft Entladeende"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//    elif s == 35:
//    msg = self.ZellTyp(f) + " entladen beendet"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//    elif s == 40:
//    msg = self.ZellTyp(f) + " l<C3><A4>dt"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//
//    elif s > 40 and s < 100:
//    msg = self.ZellTyp(f) + " l<C3><A4>dt\nVollerkennung aktiv"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//    elif s >= 100 and s < 180:
//    msg = self.ZellTyp(f) + " laden beendet"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//    elif s == 180 or s == 185:
//    msg = self.ZellTyp(f) + " Voll\nLadeerhaltung aktiv"
//
//    elif s == 200:
//            if self.bit(f,self.DIS):
//    msg = self.ZellTyp(f) + " pausiert beim Entladen"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//    elif self.bit(f,self.CHG):
//    msg = self.ZellTyp(f) + " pausiert beim Laden"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//    elif s == 225:
//    msg = "<C3><9C>bertemperatur: Laden einer " + self.ZellTyp(f) + " pausiert"
//            if self.bit(f,self.CYC): msg += '\nRecycle aktiv'
//
//    elif s == 230 or s == 235:
//    msg = "Zelle wegen fehlgeschlagenem HOT-Recycle gesperrt"
//
//    elif s == 240:
//            if   self.bit(f,self.PAU):      msg = "Spanung an " + self.ZellTyp(f) + " w<C3><A4>hrend Pause am wegbrechen"
//    elif self.bit(f,self.ERR):      msg = "Zelle wegen zu niedriger Spannung bei der Bearbeitung gesperrt"
//            else:                                           msg = "Spanung an " + self.ZellTyp(f) + " am wegbrechen oder vom Formatieren noch zu wenig"
//            if self.bit(f,self.CYC):        msg += '\nRecycle aktiv'
//
//    elif s == 245:
//            if self.bit(f,self.ERR):        msg = "Zelle wegen zu hoher Spannung bei der Bearbeitung gesperrt"
//            else:                                           msg = "Spanung an " + self.ZellTyp(f) + " im Moment sehr hoch"
//            if self.bit(f,self.CYC):        msg += '\nRecycle aktiv'
//
//    elif s == 250:
//    msg = "Zelle wegen zu hoher Spannung beim Einschalten / Einlegen gesperrt"


    public ChargerSlotStandardData[] slots;

    protected Av4msBasicReadData(Parcel in) {
        slots = in.createTypedArray(ChargerSlotStandardData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(slots, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Av4msBasicReadData> CREATOR = new Creator<Av4msBasicReadData>() {
        @Override
        public Av4msBasicReadData createFromParcel(Parcel in) {
            return new Av4msBasicReadData(in);
        }

        @Override
        public Av4msBasicReadData[] newArray(int size) {
            return new Av4msBasicReadData[size];
        }
    };

    public static Av4msBasicReadData fromResponse(MesswerteResponse r) {
        Av4msBasicReadData d = new Av4msBasicReadData();
        d.slots = new ChargerSlotStandardData[4];
        d.slots[0] = ChargerSlotStandardData.fromValues(r.spannung.u1e, r.spannung.u1l,
                r.spannung.u1eavg, r.spannung.u1lavg, r.strom.i1e, r.strom.i1l,
                r.kapazitaet.c1e, r.kapazitaet.c1l, r.zeit.t1e, r.zeit.t1l, r.energie.e1e, r.energie.e1l, 
                r.status.cy1, r.status.s1, r.status.s1txt, r.log.l1);
        d.slots[1] = ChargerSlotStandardData.fromValues(r.spannung.u2e, r.spannung.u2l,
                r.spannung.u2eavg, r.spannung.u2lavg, r.strom.i2e, r.strom.i2l,
                r.kapazitaet.c2e, r.kapazitaet.c2l, r.zeit.t2e, r.zeit.t2l, r.energie.e2e, r.energie.e2l, 
                r.status.cy2, r.status.s2, r.status.s2txt, r.log.l2);
        d.slots[2] = ChargerSlotStandardData.fromValues(r.spannung.u3e, r.spannung.u3l,
                r.spannung.u3eavg, r.spannung.u3lavg, r.strom.i3e, r.strom.i3l,
                r.kapazitaet.c3e, r.kapazitaet.c3l, r.zeit.t3e, r.zeit.t3l, r.energie.e3e, r.energie.e3l, 
                r.status.cy3, r.status.s3, r.status.s3txt, r.log.l3);
        d.slots[3] = ChargerSlotStandardData.fromValues(r.spannung.u4e, r.spannung.u4l,
                r.spannung.u4eavg, r.spannung.u4lavg, r.strom.i4e, r.strom.i4l,
                r.kapazitaet.c4e, r.kapazitaet.c4l, r.zeit.t4e, r.zeit.t4l, r.energie.e4e, r.energie.e4l, 
                r.status.cy4, r.status.s4, r.status.s4txt, r.log.l4);
        return d;
    }
}
