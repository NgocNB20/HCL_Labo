package jp.co.itechh.quad.front.base.application;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Logic/Service移行のリハーサル
 * 作成日：2021/02/25
 *
 * @author Phan Tien VU (VTI Japan Co., Ltd.)
 */
public class FacesMessage implements Serializable {

    private static final long serialVersionUID = 3258125843346766128L;

    public static final String FACES_MESSAGES = "javax.faces.Messages";

    public static final Severity SEVERITY_INFO = new Severity("INFO", 1);

    public static final Severity SEVERITY_WARN = new Severity("WARN", 2);

    public static final Severity SEVERITY_ERROR = new Severity("ERROR", 3);

    public static final Severity SEVERITY_FATAL = new Severity("FATAL", 4);

    private static final Severity[] values = {SEVERITY_INFO, SEVERITY_WARN, SEVERITY_ERROR, SEVERITY_FATAL};

    public static final List VALUES;

    public static final Map VALUES_MAP;

    static {
        VALUES = Collections.unmodifiableList(Arrays.asList(values));
        Map map = new HashMap();
        for (int i = 0; i < values.length; i++) {
            map.put(values[i].toString(), values[i]);
        }
        VALUES_MAP = Collections.unmodifiableMap(map);
    }

    private String summary;

    private String detail;

    private Severity severity;

    public FacesMessage() {
        this(SEVERITY_INFO, null, null);
    }

    public FacesMessage(String summary) {
        this(SEVERITY_INFO, summary, null);
    }

    public FacesMessage(String summary, String detail) {
        this(SEVERITY_INFO, summary, detail);
    }

    public FacesMessage(Severity severity, String summary, String detail) {
        setSeverity(severity);
        this.summary = summary;
        this.detail = detail;
    }

    public String getDetail() {
        return (detail != null) ? detail : summary;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Severity getSeverity() {
        return severity;
    }

    public void setSeverity(Severity severity) {
        for (int i = 0; i < values.length; i++) {
            Severity sev = values[i];
            if (sev.compareTo(severity) == 0) {
                this.severity = severity;
                return;
            }
        }
        throw new IllegalArgumentException("severity");
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public static class Severity implements Comparable, Serializable {

        private static final long serialVersionUID = 1L;

        private String type;

        private int ordinal;

        public Severity() {
        }

        public Severity(String type, int ordinal) {
            this.type = type;
            this.ordinal = ordinal;
        }

        public int getOrdinal() {
            return ordinal;
        }

        public String getType() {
            return type;
        }

        public void setOrdinal(int ordinal) {
            this.ordinal = ordinal;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String toString() {
            return type;
        }

        public int compareTo(Object o) {
            return ordinal - ((Severity) o).ordinal;
        }
    }
}