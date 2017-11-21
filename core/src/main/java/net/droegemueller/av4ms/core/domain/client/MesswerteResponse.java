package net.droegemueller.av4ms.core.domain.client;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@SuppressWarnings({"unused", "WeakerAccess"})
@Root(strict = false)
public class MesswerteResponse {

    @Element(required = false, name = "spannung")
    public Spannung spannung;
    @Element(required = false, name = "strom")
    public Strom strom;
    @Element(required = false, name = "kapazitaet")
    public Kapazitaet kapazitaet;
    @Element(required = false, name = "zeit")
    public Zeit zeit;
    @Element(required = false, name = "energie")
    public Energie energie;
    @Element(required = false, name = "status")
    public Status status;
    @Element(required = false, name = "log")
    public Log log;

    @SuppressWarnings("WeakerAccess")
    @Root(strict = false)
    public static class Spannung {
        @Element(required = false, name = "u1e")
        public String u1e;
        @Element(required = false, name = "u1l")
        public String u1l;
        @Element(required = false, name = "u2e")
        public String u2e;
        @Element(required = false, name = "u2l")
        public String u2l;
        @Element(required = false, name = "u3e")
        public String u3e;
        @Element(required = false, name = "u3l")
        public String u3l;
        @Element(required = false, name = "u4e")
        public String u4e;
        @Element(required = false, name = "u4l")
        public String u4l;
        @Element(required = false, name = "u1eavg")
        public String u1eavg;
        @Element(required = false, name = "u2eavg")
        public String u2eavg;
        @Element(required = false, name = "u3eavg")
        public String u3eavg;
        @Element(required = false, name = "u4eavg")
        public String u4eavg;
        @Element(required = false, name = "u1lavg")
        public String u1lavg;
        @Element(required = false, name = "u2lavg")
        public String u2lavg;
        @Element(required = false, name = "u3lavg")
        public String u3lavg;
        @Element(required = false, name = "u4lavg")
        public String u4lavg;
    }

    @Root(strict = false)
    public static class Strom {
        @Element(required = false, name = "i1e")
        public String i1e;
        @Element(required = false, name = "i1l")
        public String i1l;
        @Element(required = false, name = "i2e")
        public String i2e;
        @Element(required = false, name = "i2l")
        public String i2l;
        @Element(required = false, name = "i3e")
        public String i3e;
        @Element(required = false, name = "i3l")
        public String i3l;
        @Element(required = false, name = "i4e")
        public String i4e;
        @Element(required = false, name = "i4l")
        public String i4l;
    }

    @Root(strict = false)
    public static class Kapazitaet {
        @Element(required = false, name = "c1nenn")
        public String c1nenn;
        @Element(required = false, name = "c1e")
        public String c1e;
        @Element(required = false, name = "c1erate")
        public String c1erate;
        @Element(required = false, name = "c1l")
        public String c1l;
        @Element(required = false, name = "c1lrate")
        public String c1lrate;
        @Element(required = false, name = "c2nenn")
        public String c2nenn;
        @Element(required = false, name = "c2e")
        public String c2e;
        @Element(required = false, name = "c2erate")
        public String c2erate;
        @Element(required = false, name = "c2l")
        public String c2l;
        @Element(required = false, name = "c2lrate")
        public String c2lrate;
        @Element(required = false, name = "c3nenn")
        public String c3nenn;
        @Element(required = false, name = "c3e")
        public String c3e;
        @Element(required = false, name = "c3erate")
        public String c3erate;
        @Element(required = false, name = "c3l")
        public String c3l;
        @Element(required = false, name = "c3lrate")
        public String c3lrate;
        @Element(required = false, name = "c4nenn")
        public String c4nenn;
        @Element(required = false, name = "c4e")
        public String c4e;
        @Element(required = false, name = "c4erate")
        public String c4erate;
        @Element(required = false, name = "c4l")
        public String c4l;
        @Element(required = false, name = "c4lrate")
        public String c4lrate;
    }

    @Root(strict = false)
    public static class Zeit {
        @Element(required = false, name = "t1e")
        public String t1e;
        @Element(required = false, name = "t1l")
        public String t1l;
        @Element(required = false, name = "t2e")
        public String t2e;
        @Element(required = false, name = "t2l")
        public String t2l;
        @Element(required = false, name = "t3e")
        public String t3e;
        @Element(required = false, name = "t3l")
        public String t3l;
        @Element(required = false, name = "t4e")
        public String t4e;
        @Element(required = false, name = "t4l")
        public String t4l;
    }

    @Root(strict = false)
    public static class Energie {
        @Element(required = false, name = "e1nenn")
        public String e1nenn;
        @Element(required = false, name = "e1e")
        public String e1e;
        @Element(required = false, name = "e1erate")
        public String e1erate;
        @Element(required = false, name = "e1l")
        public String e1l;
        @Element(required = false, name = "e1lrate")
        public String e1lrate;
        @Element(required = false, name = "e2nenn")
        public String e2nenn;
        @Element(required = false, name = "e2e")
        public String e2e;
        @Element(required = false, name = "e2erate")
        public String e2erate;
        @Element(required = false, name = "e2l")
        public String e2l;
        @Element(required = false, name = "e2lrate")
        public String e2lrate;
        @Element(required = false, name = "e3nenn")
        public String e3nenn;
        @Element(required = false, name = "e3e")
        public String e3e;
        @Element(required = false, name = "e3erate")
        public String e3erate;
        @Element(required = false, name = "e3l")
        public String e3l;
        @Element(required = false, name = "e3lrate")
        public String e3lrate;
        @Element(required = false, name = "e4nenn")
        public String e4nenn;
        @Element(required = false, name = "e4e")
        public String e4e;
        @Element(required = false, name = "e4erate")
        public String e4erate;
        @Element(required = false, name = "e4l")
        public String e4l;
        @Element(required = false, name = "e4lrate")
        public String e4lrate;
    }

    @Root(strict = false)
    public static class Status {
        @Element(required = false, name = "s1")
        public String s1;
        @Element(required = false, name = "s1txt")
        public String s1txt;
        @Element(required = false, name = "cy1")
        public String cy1;
        @Element(required = false, name = "s2")
        public String s2;
        @Element(required = false, name = "s2txt")
        public String s2txt;
        @Element(required = false, name = "cy2")
        public String cy2;
        @Element(required = false, name = "s3")
        public String s3;
        @Element(required = false, name = "s3txt")
        public String s3txt;
        @Element(required = false, name = "cy3")
        public String cy3;
        @Element(required = false, name = "s4")
        public String s4;
        @Element(required = false, name = "s4txt")
        public String s4txt;
        @Element(required = false, name = "cy4")
        public String cy4;
    }


    @Root(strict = false)
    public static class Log {
        @Element(required = false, name = "l1")
        public String l1;
        @Element(required = false, name = "l2")
        public String l2;
        @Element(required = false, name = "l3")
        public String l3;
        @Element(required = false, name = "l4")
        public String l4;
    }
}
