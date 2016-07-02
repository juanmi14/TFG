package jmvdeveloper.eucast.logic;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Juanmi on 14/06/2016.
 */
public class SensitivityQuery implements Parcelable {
    private String sensitivity;
    private String family;
    private String antibioticName;
    private String diameter;
    private AntibioticFamily antibioticFamily;
    private Antibiotic antibiotic;

    public static final String SENSIBILITY = "Sensibility";
    public static final String FAMILY = "Family";
    public static final String DIAMETER = "Diameter";
    public static final String ANTIBIOTIC_NAME = "AntibioticName";
    public static final String ANTIBIOTIC_FAMILY = "AntibioticFamily";
    public static final String ANTIBIOTIC = "Antibiotic";

    public SensitivityQuery(){}

    protected SensitivityQuery(Parcel in) {
        sensitivity = in.readString();
        family = in.readString();
        antibioticName = in.readString();
        diameter = in.readString();
    }

    public static final Creator<SensitivityQuery> CREATOR = new Creator<SensitivityQuery>() {
        @Override
        public SensitivityQuery createFromParcel(Parcel in) {
            return new SensitivityQuery(in);
        }

        @Override
        public SensitivityQuery[] newArray(int size) {
            return new SensitivityQuery[size];
        }
    };

    public String getSensitivity() {
        return sensitivity;
    }

    public void setSensitivity(String sensitivity) {
        this.sensitivity = sensitivity;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public String getAntibioticName() {
        return antibioticName;
    }

    public void setAntibioticName(String antibioticName) {
        this.antibioticName = antibioticName;
    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public AntibioticFamily getAntibioticFamily() {
        return antibioticFamily;
    }

    public void setAntibioticFamily(AntibioticFamily antibioticFamily) {
        this.antibioticFamily = antibioticFamily;
    }

    public Antibiotic getAntibiotic() {
        return antibiotic;
    }

    public void setAntibiotic(Antibiotic antibiotic) {
        this.antibiotic = antibiotic;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sensitivity);
        parcel.writeString(family);
        parcel.writeString(antibioticName);
        parcel.writeString(diameter);
    }
}
